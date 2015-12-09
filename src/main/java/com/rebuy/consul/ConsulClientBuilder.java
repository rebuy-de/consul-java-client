package com.rebuy.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;

import java.util.ArrayList;
import java.util.List;

public class ConsulClientBuilder
{
    private String checkInterval = "10s";

    private final long ident;

    private String name;

    private int port = 80;

    private String server = "localhost";

    private boolean shutdownHook = false;

    private List<String> tags = new ArrayList<>();

    public ConsulClientBuilder() {
        this(System.nanoTime());
    }

    ConsulClientBuilder(long ident)
    {
        this.ident = ident;
    }

    public ConsulClientBuilder checkInterval(String checkInterval)
    {
        this.checkInterval = checkInterval;
        return this;
    }

    public String checkInterval()
    {
        return checkInterval;
    }

    public String id()
    {
        return String.format("%s:%d", name(), ident);
    }

    public String name()
    {
        return name;
    }

    public ConsulClientBuilder name(String name)
    {
        this.name = name;
        return this;
    }

    public int port()
    {
        return port;
    }

    public ConsulClientBuilder port(int port)
    {
        this.port = port;
        return this;
    }

    public String server()
    {
        return server;
    }

    public ConsulClientBuilder server(String server)
    {
        this.server = server;
        return this;
    }

    public ConsulClient setup()
    {
        ConsulClient client = buildClient();
        register(client);
        shutdownHook(client);

        return client;
    }

    public boolean shutdownHook()
    {
        return shutdownHook;
    }

    public ConsulClientBuilder shutdownHook(boolean registerShutdownHook)
    {
        this.shutdownHook = registerShutdownHook;
        return this;
    }

    public List<String> tags()
    {
        return tags;
    }

    public ConsulClientBuilder tag(String tag)
    {
        tags.add(tag);
        return this;
    }

    NewService.Check buildCheck()
    {
        NewService.Check check = new NewService.Check();
        check.setInterval(checkInterval());
        check.setHttp(String.format(
            "http://localhost:%d/health",
            port
        ));
        return check;
    }

    ConsulClient buildClient()
    {
        return new ConsulClient(server());
    }

    NewService buildService()
    {
        NewService service = new NewService();
        service.setId(id());
        service.setName(name());
        service.setPort(port());
        service.setCheck(buildCheck());
        service.setTags(tags);
        return service;
    }

    void register(ConsulClient client)
    {
        client.agentServiceRegister(buildService());
    }

    void shutdownHook(ConsulClient client)
    {
        if (shutdownHook) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                client.agentServiceDeregister(id());
            }));
        }
    }
}
