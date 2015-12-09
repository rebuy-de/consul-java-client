package com.rebuy.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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
}
