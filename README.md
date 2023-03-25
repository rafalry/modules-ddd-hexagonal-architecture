# ddd-hexagonal architecture example

ddd-hexagonal architecture project contains isolated, self-contained modules.

## Setup development environment
Install and use required Java version:

```shell
source ./setup.sh
```

## Building

### Sources
```shell
mvn clean install
```

### Docker
```shell
mvn jib:dockerBuild -pl
```

## Running
```shell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
