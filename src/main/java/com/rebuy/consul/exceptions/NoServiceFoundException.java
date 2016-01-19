package com.rebuy.consul.exceptions;

public class NoServiceFoundException extends RuntimeException
{
    public NoServiceFoundException(String serviceName)
    {
        super(String.format("%s not found", serviceName));
    }
}
