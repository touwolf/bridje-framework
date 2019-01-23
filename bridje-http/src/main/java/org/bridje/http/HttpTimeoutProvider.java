package org.bridje.http;

public interface HttpTimeoutProvider
{
    Integer timeoutForPath(String path);
}
