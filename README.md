# Jakarta Expression Language

This repository contains the source for the Jakarta Expression Language API and associated specification document.

Releases of both the API and the specification document, along with a details of the compatible implementations for each
release, are listed on the Jakarta EE [specification page](https://jakarta.ee/specifications/expression-language/) for
the Jakarta Expression Language.


## Building

### API

Jakarta Expression Language API can be built by executing the following from the project root:

```
cd api
mvn clean package
```
The API jar can then be found in `/impl/target`.

### Specification

Jakarta Expression Language specification can be built by executing the following from the project root:

```
cd spec
mvn clean package
```
The API jar can then be found in `/spec/target`.


## Making Changes

To make changes, fork this repository, make your changes, and submit a pull request.

## About Jakarta Expression Language

Jakarta Expression Language defines an expression language for Java applications.

## TCK Challenges

All test challenges for Jakarta Expression Language must be resolved by Active Resolution.
Please see the TCK Users guide for more details.