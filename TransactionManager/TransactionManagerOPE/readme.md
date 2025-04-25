# The implementation TransactionManagerOPE

## To run
``$ java -jar target\TransactionManangerOPE-0.1.0-jar-with-dependencies.jar``

## To validate
Validate service availability opening a browser with : http://localhost:2059/Transaction?wsdl


# Service TransactionManager in a OCI container

## Containerfile method

The Containerfile exists in the project directory
``$ podman build  -t transactionmanager-oci -f ./Containerfile``

Check the created container image:
``$ podman image list``

Now, start the container in the detached mode (--detach or -d):
``$ podman run --name transactionmanager-oci --detach -p 2059:2059 transactionmanager-oci:latest``


Validate service opening a browser with : http://localhost:2059/Transaction?wsdl


## Jib method

``$ mvn clean package jib:dockerBuild -Djib.dockerClient.executable=$(which podman)``

The image localhost/ can be seen at local container registry
$ podman image list

To run the container:
``$ podman run -td --name=transactionmanager-oci-jib -p 2059:2059 localhost/transactionmanager-oci-jib``

