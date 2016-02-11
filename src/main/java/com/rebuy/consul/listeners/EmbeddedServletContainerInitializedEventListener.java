package com.rebuy.consul.listeners;

import com.rebuy.consul.ConsulService;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;

public class EmbeddedServletContainerInitializedEventListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent>
{
    private final ConsulService consulService;

    public EmbeddedServletContainerInitializedEventListener(ConsulService consulService)
    {
        this.consulService = consulService;
    }

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event)
    {
        consulService.register();
    }
}
