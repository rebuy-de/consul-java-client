consul-java-client
==================

Consul client for our Java services.

* Minimized bootstrapping amount.
* Easy Spring Boot and Spring support.
* Because of the underlying client, only a single health check is supported at the moment. The health check is a HTTP check of `http://localhost:<port>/health`.

Primary responsible person (PRP)
--------------------------------

* [@prp-consul-java-client](https://github.com/orgs/rebuy-de/teams/prp-consul-java-client)


Maven dependency
----------------

```
<dependency>
    <groupId>com.rebuy</groupId>
    <artifactId>consul-java-client</artifactId>
    <version>1.4.0</version>
</dependency>
```


Examples
--------

### Minimal example

```java
ConsulService service = ConsulService.builder()
    .name("my-service")
    .build();
service.register();
```


### Full example

```java
ConsulService service = ConsulService.builder()
    .agent("localhost")
    .name("my-service")
    .checkInterval("20s")
    .port(1337)
    .tag("vhost")
    .tag("silo")
    .build();
service.register();
```


### Spring integration

```java
@Configuration
public class ConsulConfiguration
{
    @Autowired
    private ConsulService consulService;

    @Bean
    public ContextClosedEventListener contextClosedEventListener()
    {
        return new ContextClosedEventListener(consulService);
    }

    @Bean
    public ContextStoppedEventListener contextStoppedEventListener()
    {
        return new ContextStoppedEventListener(consulService);
    }

    @Bean
    public ContextRefreshedEventListener contextRefreshedEventListener()
    {
        return new ContextRefreshedEventListener(consulService);
    }
}
```
### Spring Boot integration


```java
@Configuration
public class ConsulConfiguration
{
    @Autowired
    private ConsulService consulService;

    @Bean
    public ContextClosedEventListener contextClosedEventListener()
    {
        return new ContextClosedEventListener(consulService);
    }

    @Bean
    public ContextStoppedEventListener contextStoppedEventListener()
    {
        return new ContextStoppedEventListener(consulService);
    }

    @Bean
    public EmbeddedServletContainerInitializedEventListener embeddedServletContainerInitializedEventListener()
    {
        return new EmbeddedServletContainerInitializedEventListener(consulService);
    }
}
```


### Play framework integration

```scala
@Singleton
class ConsulConfiguration @Inject()(configuration: Configuration, lifecycle: ApplicationLifecycle) {

  val service = ConsulService.builder()
    .agent(configuration.getString("consul.agent").getOrElse("localhost"))
    .name("pim-export")
    .checkInterval("20s")
    .port(configuration.getInt("service.port").get)
    .tag("vhost")
    .build()

  service.register()

  lifecycle.addStopHook {() =>
    Future.successful(service.unregister())
  }
```
