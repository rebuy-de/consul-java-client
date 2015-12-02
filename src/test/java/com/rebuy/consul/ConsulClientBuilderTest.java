package com.rebuy.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsulClientBuilderTest
{
    private ConsulClientBuilder builder;

    @Before
    public void before()
    {
        builder = new ConsulClientBuilder()
            .server("my-host")
            .name("my-service")
            .ident("42")
            .checkInterval("20s")
            .shutdownHook(true)
            .port(1337)
            .tag("bish")
            .tag("bash")
            .tag("bosh");
    }

    @Test
    public void properties_should_match()
    {
        assertEquals("my-host", builder.server());
        assertEquals("my-service", builder.name());
        assertEquals("42", builder.ident());
        assertEquals("my-service:42", builder.id());
        assertEquals("20s", builder.checkInterval());
        assertEquals(true, builder.shutdownHook());
        assertEquals(1337, builder.port());
        assertEquals(3, builder.tags().size());
        assertTrue(builder.tags().contains("bish"));
        assertTrue(builder.tags().contains("bash"));
        assertTrue(builder.tags().contains("bosh"));
    }

    @Test
    public void generated_check_should_be_correct()
    {
        NewService.Check check = builder.buildCheck();

        assertNotNull(check);
        assertEquals("20s", check.getInterval());
        assertEquals("http://localhost:1337/health", check.getHttp());
    }

    @Test
    public void generated_service_should_be_correct()
    {
        NewService service = builder.buildService();

        assertEquals("my-service:42", service.getId());
        assertEquals("my-service", service.getName());
        assertEquals(Integer.valueOf(1337), service.getPort());
    }

    @Test
    public void generated_client_should_be_correct()
    {
        ConsulClient client = builder.buildClient();
        assertNotNull(client);
    }
}
