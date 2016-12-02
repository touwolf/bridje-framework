
package org.bridje.sip;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class SipRequest extends SipMessage
{
    private SipMethod method;

    private String uri;

    private SipVersion version;

    private List<SipViaInfo> via;

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

    public SipMethod getMethod()
    {
        return method;
    }

    public void setMethod(SipMethod method)
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

    public SipVersion getVersion()
    {
        return version;
    }

    public void setVersion(SipVersion version)
    {
        this.version = version;
    }

    public List<SipViaInfo> getVia()
    {
        if(via == null)
        {
            List<String> viaHeaders = getHeaders().get("Via");
            if(viaHeaders != null)
            {
                this.via = new ArrayList<>(viaHeaders.size());
                for (String viaHeader : viaHeaders)
                {
                    this.via.add(SipViaInfo.fromString(viaHeader));
                }
            }
        }
        return via;
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
        setMethod(SipMethod.valueOf(introArr[0]));
        setUri(introArr[1]);
        setVersion(SipVersion.fromString(introArr[2]));
    }
}
