package com.rebuy.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsulService
{
    private static final Logger logger = LoggerFactory.getLogger(ConsulService.class);

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
        logger.info("registered service in consul: {}", service.toString());
    }

    public void shutdownHook()
    {
        Runtime.getRuntime().addShutdownHook(
            new Thread(this::unregister));
        logger.info("activated shutdown hook for service: {}", service.toString());
    }

    public void unregister()
    {
        client.agentServiceDeregister(service.getId());
        logger.info("unregistered service in consul: {}", service.toString());
    }
}
