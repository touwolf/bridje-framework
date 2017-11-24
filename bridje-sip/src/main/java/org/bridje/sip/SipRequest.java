
package org.bridje.sip;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a SIP Request Message.
 */
public class SipRequest extends SipMessage
{
    private SipMethod method;

    private String uri;

    private SipVersion version;

    private List<SipViaInfo> via;

    private String body;

    private InetSocketAddress sender;

    /**
     * The INET address for the sender of this message.
     * 
     * @return The INET address for the sender of this message.
     */
    public InetSocketAddress getSender()
    {
        return sender;
    }

    /**
     * The INET address for the sender of this message.
     * 
     * @param sender The INET address for the sender of this message.
     */
    public void setSender(InetSocketAddress sender)
    {
        this.sender = sender;
    }

    /**
     * The SIP protocol method for this message.
     * 
     * @return The SIP protocol method for this message.
     */
    public SipMethod getMethod()
    {
        return method;
    }

    /**
     * The SIP protocol method for this message.
     * 
     * @param method The SIP protocol method for this message.
     */
    public void setMethod(SipMethod method)
    {
        this.method = method;
    }

    /**
     * The requested URI.
     * 
     * @return The requested URI.
     */
    public String getUri()
    {
        return uri;
    }

    /**
     * The requested URI.
     * 
     * @param uri The requested URI.
     */
    public void setUri(String uri)
    {
        this.uri = uri;
    }

    /**
     * The SIP protocol version for this message.
     * 
     * @return The SIP protocol version for this message.
     */
    public SipVersion getVersion()
    {
        return version;
    }

    /**
     * The SIP protocol version for this message.
     * 
     * @param version The SIP protocol version for this message.
     */
    public void setVersion(SipVersion version)
    {
        this.version = version;
    }

    /**
     * Gets the list of VIA headers for this message.
     * 
     * @return The list of VIA headers for this message.
     */
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

    /**
     * Gets the body of the message.
     * 
     * @return The body of the message.
     */
    public String getBody()
    {
        return body;
    }

    /**
     * Sets the body of the message.
     * 
     * @param body The body of the message.
     */
    public void setBody(String body)
    {
        this.body = body;
    }

    /**
     * Sets the intro of the message so it can be parsed.
     * 
     * @param intro Sets the intro of the message so it can be parsed.
     */
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
