
package org.bridje.sip.impl;

import org.bridje.sip.SipRequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class SipRequestDecoder extends MessageToMessageDecoder<DatagramPacket>
{
    private static final Logger LOG = Logger.getLogger(SipRequestDecoder.class.getName());

    private SipRequestMessage currentMessage = new SipRequestMessage();

    private String intro = "";

    private String currentKey = "";

    private String currentVal = "";
    
    private StringBuilder body = new StringBuilder();

    private boolean parseIntro = true;

    private boolean parseKey = false;
    
    private int contentLength;
    
    private int bodyCount = 0;
    
    @Override
    protected void decode(ChannelHandlerContext ctx, DatagramPacket dgp, List<Object> out) throws Exception
    {
        ByteBuf in = dgp.content();
        currentMessage.setSender(dgp.sender());
        while (in.isReadable())
        {
            char ch = (char) in.readByte();
            if(contentLength > 0)
            {
                if(contentLength <= bodyCount)
                {
                    currentMessage.setBody(body.toString());
                    out.add(currentMessage);
                    prepareNextMessage();
                }
                else
                {
                    body.append(ch);
                    bodyCount++;
                }
            }
            else if(ch == '\n')
            {
                if(parseIntro)
                {
                    currentMessage.setIntro(intro);
                    parseIntro = false;
                }
                else
                {
                    if(currentKey.trim().isEmpty())
                    {
                        contentLength = findContentLength();
                        if(contentLength == 0)
                        {
                            out.add(currentMessage);
                            prepareNextMessage();
                        }
                        else
                        {
                            bodyCount++;
                        }
                    }
                    else
                    {
                        currentMessage.getHeaders().put(doTrim(currentKey), doTrim(currentVal));
                        currentKey = "";
                        currentVal = "";
                    }
                }
                parseKey = true;
            }
            else
            {
                if(parseIntro)
                {
                    intro += ch;
                }
                else if(parseKey)
                {
                    if(ch == ':')
                    {
                        parseKey = false;
                    }
                    else
                    {
                        currentKey += ch;
                    }
                }
                else
                {
                    currentVal += ch;
                }
            }
        }
    }

    private String doTrim(String value)
    {
        if(value == null)
        {
            return value;
        }
        return value.replaceAll("\r", "").trim();
    }
    private void prepareNextMessage()
    {
        currentMessage = new SipRequestMessage();
        intro = "";
        currentKey = "";
        currentVal = "";
        body = new StringBuilder();
        bodyCount = 0;
        contentLength = 0;
        parseIntro = true;
        parseKey = false;
    }

    private int findContentLength()
    {
        String clStr = currentMessage.getHeaders().get("Content-Length");
        try
        {
            if(clStr != null && !clStr.isEmpty())
            {
                if(!clStr.matches("[0-9]+"))
                {
                    LOG.log(Level.WARNING, "Invalid content length {0}", clStr);
                }
                return Integer.valueOf(clStr);
            }
        }
        catch (NumberFormatException e)
        {
            LOG.log(Level.WARNING, "Invalid content length {0}", clStr);
        }
        return 0;
    }
}
