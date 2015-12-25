
package org.bridje.core.impl.http.jetty;

import org.bridje.core.http.HttpServer;
import org.bridje.core.http.HttpServerException;
import org.bridje.core.ioc.annotations.Component;

@Component
class HttpServerImpl implements HttpServer
{
    private JettyServer server; 
    
    @Override
    public void start() throws HttpServerException
    {
        try
        {
            server.start();
        }
        catch(Exception ex)
        {
            throw new HttpServerException(ex.getMessage(), ex);
        }
    }

    @Override
    public void stop() throws HttpServerException
    {
        try
        {
            server.stop();
        }
        catch(Exception ex)
        {
            throw new HttpServerException(ex.getMessage(), ex);
        }
    }
    
}
