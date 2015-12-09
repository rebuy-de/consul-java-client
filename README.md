consul-java-client
==================

Consul client for our Java services.

Primary responsible person (PRP)
--------------------------------

* Björn Häuser, [@bjoernhaeuser](https://github.com/bjoernhaeuser)

Dependency
----------

```
<dependency>
    <groupId>com.rebuy</groupId>
    <artifactId>consul-java-client</artifactId>
    <version>1.0.0</version>
</dependency>
```

Example
-------

```java
ConsulService service = ConsulService.builder()
    .server("my-host")
    .name("my-service")
    .checkInterval("20s")
    .port(1337)
    .build();
service.register();
service.unregister();
```