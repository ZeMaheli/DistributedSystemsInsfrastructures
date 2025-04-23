# Transaction Manager
___
The transaction manager is the system responsible to ensure that
all the changes made to the elements of the vectors inside any vector service
don't create an invalid state.

# Published Interfaces
___
The interfaces published to communicate with the transaction manager
are described in this section.

## Client interface
The client interface is responsible to enable the client
to make transaction that change the state of multiple elements
present in any of the vector services.


### Methods
This interface present the following methods:
* begin - begins a transaction
* commit - commit a transaction
* rollback - rollbacks a transaction

## RM interface
The resource manager interface is responsible to ensure that
the transaction manager is aware resource manager current state.

### Methods
For that purpose the interface establishes these methods:
* register - informs the TM that it is about to do work
* unregister - informs the TM that it exits the association
(no writes in the transaction, so there's no need for it to commit)

# Dependencies
___
The transaction manager is dependent on multiple systems to provide its service.

## RNM Interface
The transaction manager needs to interface with the RNM to keep its state.

## TPLM Interface
The transaction manager requires an TPLM interface to signal
when the transaction ends, so that the locks regarding a transaction are released.

## RM Interface
The transaction manager uses the RM interface to interact with the vector services
for the following purposes:
* open - prepares the resource manager to a transaction
* prepare - signal the resource manager that it should be prepared to commit
* commit - signal the resource manager to commit a transaction
* rollback - order the resource manager to rollback a transaction
