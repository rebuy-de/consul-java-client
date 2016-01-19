package com.rebuy.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.rebuy.consul.exceptions.NoServiceFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    public Service getRandomService(String serviceName)
    {
        List<CatalogService> services = fetchServices(serviceName);

        verifyServiceFound(serviceName, services);

        CatalogService catalogService = pickRandomService(services);
        return mapService(catalogService);
    }

    public Service getRandomService(String serviceName, String tag)
    {
        List<CatalogService> services = fetchServices(serviceName, tag);

        verifyServiceFound(serviceName, services);

        CatalogService catalogService = pickRandomService(services);
        return mapService(catalogService);
    }

    private Service mapService(CatalogService catalogService)
    {
        return new Service(catalogService.getAddress(), catalogService.getServicePort());
    }

    private void verifyServiceFound(String serviceName, List<CatalogService> services)
    {
        if (services.size() == 0) {
            throw new NoServiceFoundException(serviceName);
        }
    }

    private List<CatalogService> fetchServices(String serviceName)
    {
        return client.getCatalogService(serviceName, QueryParams.DEFAULT).getValue();
    }

    private List<CatalogService> fetchServices(String serviceName, String tag)
    {
        return client.getCatalogService(serviceName, tag, QueryParams.DEFAULT).getValue();
    }

    private CatalogService pickRandomService(List<CatalogService> services)
    {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return services.get(random.nextInt(services.size()));
    }
}
