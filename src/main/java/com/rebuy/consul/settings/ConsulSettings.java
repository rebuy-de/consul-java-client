package com.rebuy.consul.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Configuration
@ConfigurationProperties("consul")
@Validated
public class ConsulSettings
{
    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Size(min = 1)
    private String agent;

    @NotNull
    private int siloPort;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAgent()
    {
        return agent;
    }

    public void setAgent(String agent)
    {
        this.agent = agent;
    }

    public int getSiloPort()
    {
        return siloPort;
    }

    public void setSiloPort(int siloPort)
    {
        this.siloPort = siloPort;
    }
}
