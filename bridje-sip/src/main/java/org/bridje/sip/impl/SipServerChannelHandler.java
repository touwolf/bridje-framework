
package org.bridje.sip.impl;

import org.bridje.sip.SipRequestMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bridje.ioc.Ioc;
import org.bridje.sip.SipResponseMessage;

public class SipServerChannelHandler extends SimpleChannelInboundHandler<SipRequestMessage>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SipRequestMessage msg) throws Exception
    {
        RootSipBridlet rootBridlet = Ioc.context().find(RootSipBridlet.class);
        SipResponseMessage resp = rootBridlet.handle(msg);
        resp.setRecipient(msg.getSender());
        msg.getHeaders()
                    .entrySet().stream()
                    .filter(e -> !resp.getHeaders().containsKey(e.getKey()))
                    .forEach(e -> resp.getHeaders().put(e.getKey(), e.getValue()));
        ctx.channel().writeAndFlush(resp);
    }
}
