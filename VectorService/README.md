# Vector Service

# Index
___
* [Exposed Interface](#exposed-interface)
  * [Write](#write)
    * [Parameters](#parameters)
    * [Return](#return)
  * [Read](#read)
    * [Parameters](#parameters-1)
    * [Return](#return-1)
  * [Get Variance](#get-variance)
    * [Parameters](#parameters-2)
    * [Return](#return-2)
* [Instructions](#instructions)


# Exposed Interface
___

## Write
### Parameters
* TransactionID - The ID that owns the current operation.
* ElementPosition - The position where the client wants to write.
* Value - The value that the client wants to write.

### Return
This function doesn't return any value.

## Read
### Parameters
* TransactionID - The ID that owns the current operation.
* ElementPosition - The position from which the client wants to read.

### Return
* Integer - The value read from the element

## Get Variance
### Parameters
This method accepts no parameters.

### Return
The Variance since the service startup, this value should be combined
with the variance from the others vector services to ensure the invariance.

# Instructions
___
To Package, execute: ``$ mvn clean package``

To run: 
``$ java -jar target/VectorServiceOPE-0.1.0-jar-with-dependencies.jar <PARAMETERS>``

Parameters:
* HOSTNAME - hostname/ip (default: 0.0.0.0)
* PORT - service listening port (default: 2060)

If you select ``http://localhost:2060/Vector?wsdl``,
you should view the WSDL interface definition
