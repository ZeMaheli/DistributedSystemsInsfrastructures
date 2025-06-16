#!/bin/bash

set -e  # Para parar o script em caso de erro

echo "🔧 Criando rede (se necessário)..."
podman network inspect my_custom_network >/dev/null 2>&1 || \
  podman network create my_custom_network

echo "🚀 Iniciando Zookeeper..."
podman run -d \
  --name zookeeper \
  --network my_custom_network \
  -p 2181:2181 \
  -v zookeeper_data:/data \
  docker.io/library/zookeeper:latest

echo "🚀 Iniciando RabbitMQ..."
podman run -d \
  --name rabbitmq \
  --network my_custom_network \
  -p 5672:5672 \
  -p 15672:15672 \
  -e RABBITMQ_DEFAULT_USER=guest \
  -e RABBITMQ_DEFAULT_PASS=guest \
  docker.io/library/rabbitmq:3-management

# Aguarda alguns segundos para garantir que ZK e Rabbit estão prontos
sleep 5

echo "🛠️ Build RNM..."
podman build -t rnm ./ReliableNodeManager/ReliableNodeManagerOPE

echo "🚀 Iniciando RNM..."
podman run -d \
  --name rnm \
  -e ZOOKEEPER_HOST=zookeeper:2181 \
  --network my_custom_network \
  rnm

echo "🛠️ Build TPLM..."
podman build -t tplm ./TwoPhaseLockManager/TwoPhaseLockManagerOPE

echo "🚀 Iniciando TPLM..."
podman run -d \
  --name tplm \
  --network my_custom_network \
  tplm

echo "🛠️ Build TM..."
podman build -t tm ./TransactionManager/TransactionManagerOPE

echo "🚀 Iniciando TM..."
podman run -d \
  --name tm \
  --network my_custom_network \
  tm

echo "🛠️ Build VectorService..."
podman build -t vs ./VectorService/VectorServiceOPE

echo "🚀 Iniciando Vector 1..."
podman run -d \
  --name vector1 \
  --network my_custom_network \
  -p 2061:2060 \
  vs \
  1 0.0.0.0 2060

echo "🚀 Iniciando Vector 2..."
podman run -d \
  --name vector2 \
  --network my_custom_network \
  -p 2062:2060 \
  vs \
  2 0.0.0.0 2060

echo "✅ Todos os serviços foram iniciados com sucesso!"