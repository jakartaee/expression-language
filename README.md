# Jakarta Expression Language

This repository contains the source for:

 - the Jakarta Expression Language [API](https://javadoc.io/doc/jakarta.el/jakarta.el-api/) (/api) - 
 - the Jakarta Expression Language [specification](https://jakarta.ee/specifications/expression-language/4.0/jakarta-expression-language-spec-4.0.html) (/spec)


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