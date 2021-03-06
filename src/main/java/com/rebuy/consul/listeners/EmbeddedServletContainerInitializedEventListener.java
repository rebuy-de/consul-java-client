package com.rebuy.consul.listeners;

import com.rebuy.consul.ConsulService;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

public class EmbeddedServletContainerInitializedEventListener implements ApplicationListener<ServletWebServerInitializedEvent>
{
    private final ConsulService consulService;

    public EmbeddedServletContainerInitializedEventListener(ConsulService consulService)
    {
        this.consulService = consulService;
    }

    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent event)
    {
        consulService.register();
    }
}
