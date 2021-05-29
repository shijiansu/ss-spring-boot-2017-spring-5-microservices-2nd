# Migrating Custom Counters/Gauges 

- <https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.0-Migration-Guide#migrating-custom-countersgauges>

# Migrating HATEOAS 

- <https://stackoverflow.com/questions/55770163/resource-and-controllerlinkbuilder-not-found-and-deprecated>

The most fundamental change is the fact that Spring HATEOAS doesn’t create resources. That’s what Spring MVC/Spring WebFlux does. We create vendor neutral representations of hypermedia. So we renamed those core types:

LINK - https://spring.io/blog/2019/03/05/spring-hateoas-1-0-m1-released#overhaul

- ResourceSupport is now RepresentationModel
- Resource is now EntityModel
- Resources is now CollectionModel
- PagedResources is now PagedModel

# Java development environment

`sdk list java && sdk current java`

# Setup Maven wrapper

- https://github.com/takari/maven-wrapper

`mvn -N io.takari:maven:0.7.7:wrapper -Dmaven=3.5.4`

# Script step by step

```bash
./mvnw clean spring-boot:run
```

# One stop script

```bash
/bin/bash run.sh
/bin/bash run-test.sh
/bin/bash run-stop.sh
```
