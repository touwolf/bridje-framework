
package org.bridje.sip;

import java.net.InetSocketAddress;

public class SipResponse extends SipMessage
{
    private SipVersion version;

    private int statusCode;

    private String message;

    private InetSocketAddress recipient;

    public SipVersion getVersion()
    {
        return version;
    }

    public void setVersion(SipVersion version)
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

    public InetSocketAddress getRecipient()
    {
        return recipient;
    }

    public void setRecipient(InetSocketAddress recipient)
    {
        this.recipient = recipient;
    }
}
