package com.rebuy.consul.listeners;

import com.rebuy.consul.ConsulService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;

public class ContextStoppedEventListener implements ApplicationListener<ContextStoppedEvent>
{
    private final ConsulService consulService;

    public ContextStoppedEventListener(ConsulService consulService)
    {
        this.consulService = consulService;
    }

    @Override
    public void onApplicationEvent(ContextStoppedEvent event)
    {
        consulService.unregister();
    }
}
