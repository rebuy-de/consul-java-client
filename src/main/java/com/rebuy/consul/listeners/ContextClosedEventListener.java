package com.rebuy.consul.listeners;

import com.rebuy.consul.ConsulService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent>
{
    private final ConsulService consulService;

    public ContextClosedEventListener(ConsulService consulService)
    {
        this.consulService = consulService;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event)
    {
        consulService.unregister();
    }
}
