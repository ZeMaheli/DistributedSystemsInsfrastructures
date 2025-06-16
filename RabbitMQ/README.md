# To start the local instance of RabbitMQ:
```bash
$ podman run -d --name rabbitmq -p 5672:5672 -p 15672:15672 docker.io/library/rabbitmq:3-management
```
# To Run RabbitMQ in a Kubernetes context:
```bash
$ kubectl apply -f rabbitmq-deployment.yaml
```
```bash
$ kubectl apply -f rabbitmq-service.yaml
```