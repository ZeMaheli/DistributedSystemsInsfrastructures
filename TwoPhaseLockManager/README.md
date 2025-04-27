# Two Phase Lock Manager (TPLM)

# Index

___

* [System Overview](#system-overview)
* [Exposed Interface](#exposed-interface)
    * [Get Locks](#get-locks)
        * [Parameters](#parameters)
        * [Return](#return)
    * [Release All Locks](#release-all-locks)
        * [Parameters](#parameters-1)
        * [Return](#return-1)
* [Instructions](#instructions)
* [Dependencies](#dependencies)

# System Overview

___
The **Two Phase Lock Manager (TPLM)** provides concurrency control in a distributed system by handling lock requests
from transactions on specific vector elements.  
TPLM relies on a **Reliable Node Manager (RNM)** to interact with Zookeeper for metadata storage and to check the
availability of Resource Managers (RMs).

TPLM also integrates with **RabbitMQ** to asynchronously notify transactions when locks are granted.

# Exposed Interface

___

## Get Locks

### Parameters

* **TransactionID** - The ID that owns the lock request.
* **List of Locks** - The list of vector positions the transaction wants to lock. Each lock contains:
    * `vectorId` - The identifier of the vector.
    * `element` - The position (integer) inside the vector.

### Return

* **Integer**:
    * `0` - Success: locks were acquired or the request was queued.
    * `-1` - Failure: associated Resource Manager (RM) is inactive or an internal error occurred.

## Release All Locks

### Parameters

* **TransactionID** - The ID whose locks should be released.

### Return

This function doesn't return any value.

# Instructions

___
To package, execute:

```bash
$ mvn clean package
```

To run:

```bash
$ java -jar target/TwoPhaseLockManager-0.1.0-jar-with-dependencies.jar <RABBITMQ_HOST>
```

# Parameters

* **RABBITMQ_HOST** - Hostname/IP of the RabbitMQ broker used for sending lock-granted notifications.

# Dependencies

* **Reliable Node Manager (RNM)**: Handles all interactions with Zookeeper:
    * Storing held locks.
    * Managing pending lock requests.
    * Checking the status of Resource Managers (RMs) before granting locks.

* **RabbitMQ**: Used to asynchronously notify transactions when locks are granted.

# How it Works
___
