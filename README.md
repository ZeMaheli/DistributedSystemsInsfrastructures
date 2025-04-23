# Project
This is an academic project for IESD("Infra-structures on Distributed Systems)@[ISEL](https://www.isel.pt).

The structure can be divided into multiple pieces:
* [CL - Client](#cl---client)
* [VS - Vector Service](#vs---vector-service)
* [TM - Transaction Manager](#tm---transaction-manager)
* [TPLM - Two Phase Lock Manager](#tplm---two-phase-lock-manager)
* [RM - Resource Manager](#rm---resource-manager)
* [RNM - Reliable Node Manager](#rnm---reliable-node-manager)

To run the project, follow the [instructions](#Instructions),
or if you are ready have compiled the sources check [dependencies](#dependencies).

## CL - Client
The client is an application created to access the [VS](#vs---vector-service)
to read and write values into its vectors. For it, the client must interact with
the [TM](#tm---transaction-manager) and the [TPLM](#tplm---two-phase-lock-manager)
to ensure data integrity.

### Implementation
The implementation of the client is based on the following pseudocode:
```
Lock resources
Initiate Transaction
Do reads and writes on onwed resources
Commit or rollback transaction
Release resources
```

## VS - Vector Service
The Vector Service allows to interact with multiple elements of the service for
read and write.

## TM - Transaction Manager
The transaction Manager is responsible to guarantee that the access to the resources
don't result on invalid states.

For that, the transaction manager enables the following requests:

### Begin
The *begin* procedure, starts a transaction. This procedure has as parameters...

## TPLM - Two Phase Lock Manager

## RM - Resource Manager

## RNM - Reliable Node Manager


# Communication
The communication between the pieces are described as follows:

## CL <-> TM
The [TM](#tm---transaction-manager) exposes the following methods to the [CL](#cl---client):
* [begin]();
* [commit]();
* [rollback]().

And optionally:
* [info]().

## TM <-> RNM
To cooperate with the [RNM](#rnm---reliable-node-manager) the [TM](#tm---transaction-manager)
as at is disposition these methods:
* [add transaction]()
* [close transaction]()

## CL <-> TPLM
The [TPLM](#tplm---two-phase-lock-manager) exposes the following method to the [CL](#cl---client):
* [get locks]()

## TM <-> TPLM

## TM <-> RM

## TPLM <-> RNM


# Instructions
Install dependencies, build using the following commands:

``[Insert command to build]``

Scripts to build the project, parameters for each project...

# Dependencies
In this part of the document there should be described all the general
dependencies to run the system, like, for instance, the containers for
Zookeeper and RabbitMQ, with scripts to clean install these dependencies.