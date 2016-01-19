package com.rebuy.consul;

public class Service
{
    private final String hostname;

    private final int port;

    public Service(final String hostname, final int port)
    {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname()
    {
        return hostname;
    }

    public int getPort()
    {
        return port;
    }
}
