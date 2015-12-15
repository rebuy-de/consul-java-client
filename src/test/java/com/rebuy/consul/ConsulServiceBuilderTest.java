package com.rebuy.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsulServiceBuilderTest
{
    private ConsulServiceBuilder builder;

    @Before
    public void before()
    {
        builder = new ConsulServiceBuilder()
            .agent("my-host")
            .name("my-service")
            .checkInterval("20s")
            .port(1337)
            .tag("bish")
            .tag("bash")
            .tag("bosh");
    }

    @Test
    public void properties_should_match()
    {
        assertEquals("my-host", builder.agent());
        assertEquals("my-service", builder.name());
        assertEquals("my-service:1337", builder.id());
        assertEquals("20s", builder.checkInterval());
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

        assertEquals("my-service:1337", service.getId());
        assertEquals("my-service", service.getName());
        assertEquals(Integer.valueOf(1337), service.getPort());
    }

    @Test
    public void generated_client_should_be_correct()
    {
        ConsulClient client = builder.buildClient();
        assertNotNull(client);
    }

    @Test
    public void build_should_generate_propper_object() {
        ConsulService service = builder.build();
        assertNotNull(service);
        assertNotNull(service.client);
        assertNotNull(service.service);

        assertEquals("my-service", service.service.getName());
        assertEquals("my-service:1337", service.service.getId());
        assertEquals(1337, (int)service.service.getPort());
        assertEquals(3, service.service.getTags().size());
        assertTrue(service.service.getTags().contains("bish"));
        assertTrue(service.service.getTags().contains("bash"));
        assertTrue(service.service.getTags().contains("bosh"));
    }
}
