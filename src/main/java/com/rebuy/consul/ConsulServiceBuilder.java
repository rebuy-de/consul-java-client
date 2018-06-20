package com.rebuy.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;

import java.util.ArrayList;
import java.util.List;

public class ConsulServiceBuilder
{
    private String checkInterval = "10s";

    private String timeout = "10s";

    private String name;

    private int port = 80;

    private String agent = "localhost";

    private List<String> tags = new ArrayList<>();

    public ConsulServiceBuilder checkInterval(String checkInterval)
    {
        this.checkInterval = checkInterval;
        return this;
    }

    public String checkInterval()
    {
        return checkInterval;
    }

    public ConsulServiceBuilder timeout(String timeout)
    {
        this.timeout = timeout;
        return this;
    }

    public String timeout()
    {
        return timeout;
    }

    public String id()
    {
        return String.format("%s:%d", name(), port);
    }

    public String name()
    {
        return name;
    }

    public ConsulServiceBuilder name(String name)
    {
        this.name = name;
        return this;
    }

    public int port()
    {
        return port;
    }

    public ConsulServiceBuilder port(int port)
    {
        this.port = port;
        return this;
    }

    public String agent()
    {
        return agent;
    }

    public ConsulServiceBuilder agent(String agent)
    {
        this.agent = agent;
        return this;
    }

    public ConsulService build() {
        return new ConsulService(buildClient(), buildService());
    }

    public List<String> tags()
    {
        return tags;
    }

    public ConsulServiceBuilder tag(String tag)
    {
        tags.add(tag);
        return this;
    }

    NewService.Check buildCheck()
    {
        NewService.Check check = new NewService.Check();
        check.setTimeout(timeout());
        check.setInterval(checkInterval());
        check.setHttp(String.format(
            "http://localhost:%d/health",
            port
        ));
        return check;
    }

    ConsulClient buildClient()
    {
        return new ConsulClient(agent());
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
}
