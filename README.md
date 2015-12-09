consul-java-client
==================

Consul client for our Java services.

Primary responsible person (PRP)
--------------------------------

* Björn Häuser, [@bjoernhaeuser](https://github.com/bjoernhaeuser)

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