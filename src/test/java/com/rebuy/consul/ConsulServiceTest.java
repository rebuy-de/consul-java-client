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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConsulService.class, ConsulClient.class, NewService.class})
public class ConsulServiceTest
{
    private ConsulClient clientMock;

    private ConsulService service;

    private NewService serviceMock;

    @Before
    public void before()
    {
        clientMock = PowerMockito.mock(ConsulClient.class);
        serviceMock = PowerMockito.mock(NewService.class);
        Mockito.when(serviceMock.getId()).thenReturn("42");
        service = new ConsulService(clientMock, serviceMock);
    }

    @Test
    public void register_should_invoke_client()
    {
        Mockito.when(clientMock.agentServiceRegister(Mockito.any())).thenReturn(null);

        service.register();
        Mockito.verify(clientMock).agentServiceRegister(serviceMock);
    }

    @Test
    public void unregister_should_invoke_client()
    {
        Mockito.when(clientMock.agentServiceRegister(Mockito.any())).thenReturn(null);

        service.unregister();
        Mockito.verify(clientMock).agentServiceDeregister("42");
    }

    @Test(expected = NoServiceFoundException.class)
    public void findService_should_throw_exception_if_no_services_are_found()
    {
        Response<List<CatalogService>> response = new Response<>(new ArrayList<>(), 1L, true, 1L);
        Mockito.when(clientMock.getCatalogService(Mockito.anyString(), Mockito.any(QueryParams.class))).thenReturn(response);

        service.getRandomService("service");
        Mockito.verify(clientMock).getCatalogService("service", Mockito.any(QueryParams.class));
    }

    @Test
    public void findService_should_invoke_client()
    {
        List<CatalogService> services = new ArrayList<>();
        services.add(Mockito.mock(CatalogService.class));
        Response<List<CatalogService>> response = new Response<>(services, 1L, true, 1L);
        Mockito.when(clientMock.getCatalogService(Mockito.anyString(), Mockito.any(QueryParams.class))).thenReturn(response);

        service.getRandomService("service");
        Mockito.verify(clientMock).getCatalogService(Mockito.eq("service"), Mockito.any(QueryParams.class));
    }

    @Test
    public void findService_should_return_one_service()
    {
        List<CatalogService> services = new ArrayList<>();
        services.add(Mockito.mock(CatalogService.class));
        services.add(Mockito.mock(CatalogService.class));
        services.add(Mockito.mock(CatalogService.class));

        Response<List<CatalogService>> response = new Response<>(services, 1L, true, 1L);
        Mockito.when(clientMock.getCatalogService(Mockito.anyString(), Mockito.any(QueryParams.class))).thenReturn(response);

        CatalogService catalogService = service.getRandomService("service");
        assert(services.contains(catalogService));
    }
}
