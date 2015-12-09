package com.rebuy.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;

public class ConsulService
{
    public static ConsulServiceBuilder builder()
    {
        return new ConsulServiceBuilder();
    }

    final ConsulClient client;

    final NewService service;

    public ConsulService(ConsulClient client, NewService service)
    {
        this.client = client;
        this.service = service;
    }

    public void register()
    {
        client.agentServiceRegister(service);
    }

    public void shutdownHook()
    {
        Runtime.getRuntime().addShutdownHook(
            new Thread(this::unregister));
    }

    public void unregister()
    {
        client.agentServiceDeregister(service.getId());
    }
}
