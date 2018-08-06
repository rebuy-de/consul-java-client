package com.rebuy.consul;

import com.rebuy.consul.listeners.ContextClosedEventListener;
import com.rebuy.consul.listeners.ContextStoppedEventListener;
import com.rebuy.consul.listeners.EmbeddedServletContainerInitializedEventListener;
import com.rebuy.consul.settings.ConsulSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"vagrant", "sandbox"})
public class ConsulConfiguration
{
    @Bean
    public ContextClosedEventListener contextClosedEventListener(ConsulService consulService)
    {
        return new ContextClosedEventListener(consulService);
    }

    @Bean
    public ContextStoppedEventListener contextStoppedEventListener(ConsulService consulService)
    {
        return new ContextStoppedEventListener(consulService);
    }

    @Bean
    public EmbeddedServletContainerInitializedEventListener embeddedServletContainerInitializedEventListener(ConsulService consulService)
    {
        return new EmbeddedServletContainerInitializedEventListener(consulService);
    }

    @Bean
    public ConsulService consulService(ConsulSettings consulSettings)
    {
        ConsulServiceBuilder consulServiceBuilder = new ConsulServiceBuilder();

        return consulServiceBuilder
            .agent(consulSettings.getAgent())
            .port(consulSettings.getSiloPort())
            .name(consulSettings.getName())
            .endpoint("actuator/health")
            .tag("silo")
            .tag("vhost")
            .build();
    }
}
