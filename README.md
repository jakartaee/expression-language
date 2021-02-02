# Jakarta Expression Language

This repository contains the source for:

 - the Jakarta Expression Language [API](https://javadoc.io/doc/jakarta.el/jakarta.el-api/) (/api) - 
 - the Jakarta Expression Langauge [specification](https://jakarta.ee/specifications/expression-language/4.0/jakarta-expression-language-spec-4.0.html) (/spec)
 - the Glassfish implementation of the Jakarta Expression Language API (/impl)

Note: From Jakarta EE 9 onwards the JAR containing the Glassfish implementation
of the Jakarta Expression Language API only contains the implementation classes.
It no longer contains a copy of the API classes which may be obtained from the
[API JAR](https://search.maven.org/artifact/jakarta.el/jakarta.el-api).

Note: The Glassfish implementation is expected to move to a new repository shortly.


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