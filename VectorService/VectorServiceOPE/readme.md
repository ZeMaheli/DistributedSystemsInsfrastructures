# The implementation VectorServiceOPE

## To run
``$ java -jar target\VectorServiceOPE-0.1.0-jar-with-dependencies.jar``

## To validate
Validate service availability opening a browser with : http://localhost:2060/Vector?wsdl


# Service VectorService in a OCI container

## Containerfile method

The Containerfile exists in the project directory
``$ podman build  -t vectorservice-oci -f ./Containerfile``

Check the created container image:
``$ podman image list``

Now, start the container in the detached mode (--detach or -d):
``$ podman run --name vectorservice-oci --detach -p 2060:2060 vectorservice-oci:latest``


Validate service opening a browser with : http://localhost:2060/Vector?wsdl


## Jib method

``$ mvn clean package jib:dockerBuild -Djib.dockerClient.executable=$(which podman)``

The image localhost/ can be seen at local container registry
$ podman image list

To run the container:
``$ podman run -td --name=vectorservice-oci-jib -p 2060:2060 localhost/vectorservice-oci-jib``

