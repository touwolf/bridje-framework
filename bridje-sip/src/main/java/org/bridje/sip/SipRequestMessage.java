
package org.bridje.sip;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class SipRequestMessage
{
    private String method;

    private String uri;
    
    private String version;

    private Map<String,String> headers;
    
    private String body;
    
    private InetSocketAddress sender;

    public InetSocketAddress getSender()
    {
        return sender;
    }

    public void setSender(InetSocketAddress sender)
    {
        this.sender = sender;
    }
    
    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getUri()
    {
        return uri;
    }

    public void setUri(String uri)
    {
        this.uri = uri;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public Map<String, String> getHeaders()
    {
        if(headers == null)
        {
            headers = new HashMap<>();
        }
        return headers;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public void setIntro(String intro)
    {
        String[] introArr = intro.split(" ");
        if(introArr.length != 3)
        {
            throw new IllegalArgumentException("Invalid sip message intro");
        }
        setMethod(introArr[0]);
        setUri(introArr[1]);
        setVersion(introArr[2]);
    }
}
