
package org.bridje.sip;

import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;

public class SipResponseMessage
{
    private String version;

    private int statusCode;
    
    private String message;

    private Map<String,String> headers;

    private InetSocketAddress recipient;

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Map<String, String> getHeaders()
    {
        if(headers == null)
        {
            headers = new LinkedHashMap<>();
        }
        return headers;
    }

    public InetSocketAddress getRecipient()
    {
        return recipient;
    }

    public void setRecipient(InetSocketAddress recipient)
    {
        this.recipient = recipient;
    }
}
