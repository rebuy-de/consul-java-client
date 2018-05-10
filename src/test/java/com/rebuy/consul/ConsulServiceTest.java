package com.rebuy.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.rebuy.consul.exceptions.NoServiceFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConsulServiceTest
{
    private ConsulClient clientMock;

    private ConsulService service;

    private NewService serviceMock;

    @Before
    public void before()
    {
        clientMock = mock(ConsulClient.class);
        serviceMock = mock(NewService.class);
        when(serviceMock.getId()).thenReturn("42");
        service = new ConsulService(clientMock, serviceMock);
    }

    @Test
    public void register_should_invoke_client()
    {
        when(clientMock.agentServiceRegister(Mockito.any())).thenReturn(null);

        service.register();
        Mockito.verify(clientMock).agentServiceRegister(serviceMock);
    }

    @Test
    public void unregister_should_invoke_client()
    {
        service.unregister();
        Mockito.verify(clientMock).agentServiceSetMaintenance("42", true);
    }

    @Test(expected = NoServiceFoundException.class)
    public void findService_should_throw_exception_if_no_services_are_found()
    {
        Response<List<CatalogService>> response = new Response<>(new ArrayList<>(), 1L, true, 1L);
        when(clientMock.getCatalogService(Mockito.anyString(), Mockito.any(QueryParams.class))).thenReturn(response);

        service.getRandomService("service");
        Mockito.verify(clientMock).getCatalogService("service", Mockito.any(QueryParams.class));
    }

    @Test
    public void findService_should_invoke_client()
    {
        List<CatalogService> services = new ArrayList<>();
        services.add(mock(CatalogService.class));
        Response<List<CatalogService>> response = new Response<>(services, 1L, true, 1L);
        when(clientMock.getCatalogService(Mockito.anyString(), Mockito.any(QueryParams.class))).thenReturn(response);

        service.getRandomService("service");
        Mockito.verify(clientMock).getCatalogService(Mockito.eq("service"), Mockito.any(QueryParams.class));
    }

    @Test
    public void findService_should_return_one_service()
    {
        List<CatalogService> services = new ArrayList<>();
        CatalogService service1 = mock(CatalogService.class);
        when(service1.getAddress()).thenReturn("192.168.0.1");
        services.add(service1);

        CatalogService service2 = mock(CatalogService.class);
        when(service2.getAddress()).thenReturn("192.168.0.2");
        services.add(service2);

        CatalogService service3 = mock(CatalogService.class);
        when(service3.getAddress()).thenReturn("192.168.0.3");
        services.add(service3);

        Response<List<CatalogService>> response = new Response<>(services, 1L, true, 1L);
        when(clientMock.getCatalogService(Mockito.anyString(), Mockito.any(QueryParams.class))).thenReturn(response);

        Service catalogService = service.getRandomService("service");
        boolean foundMatch = false;
        for (CatalogService service : services) {
            if (service.getAddress().equals(catalogService.getHostname()) && service.getServicePort() == catalogService.getPort()) {
                foundMatch = true;
            }
        }
        assertTrue(foundMatch);
        Mockito.verify(clientMock).getCatalogService(Mockito.eq("service"), Mockito.any(QueryParams.class));
    }

    @Test
    public void findService_should_return_one_service_with_tag()
    {
        List<CatalogService> services = new ArrayList<>();
        CatalogService service1 = mock(CatalogService.class);
        when(service1.getAddress()).thenReturn("192.168.0.1");
        services.add(service1);

        CatalogService service2 = mock(CatalogService.class);
        when(service2.getAddress()).thenReturn("192.168.0.2");
        services.add(service2);

        CatalogService service3 = mock(CatalogService.class);
        when(service3.getAddress()).thenReturn("192.168.0.3");
        services.add(service3);

        Response<List<CatalogService>> response = new Response<>(services, 1L, true, 1L);
        when(clientMock.getCatalogService(Mockito.anyString(), Mockito.anyString(), Mockito.any(QueryParams.class))).thenReturn(response);

        Service catalogService = service.getRandomService("service", "my-tag");
        boolean foundMatch = false;
        for (CatalogService service : services) {
            if (service.getAddress().equals(catalogService.getHostname()) && service.getServicePort() == catalogService.getPort()) {
                foundMatch = true;
            }
        }
        assertTrue(foundMatch);
        Mockito.verify(clientMock).getCatalogService(Mockito.eq("service"), Mockito.eq("my-tag"), Mockito.any(QueryParams.class));
    }
}
