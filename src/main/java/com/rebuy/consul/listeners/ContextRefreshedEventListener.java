package com.rebuy.consul.listeners;

import com.rebuy.consul.ConsulService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent>
{
    private final ConsulService consulService;

    public ContextRefreshedEventListener(ConsulService consulService)
    {
        this.consulService = consulService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        consulService.register();
    }
}
