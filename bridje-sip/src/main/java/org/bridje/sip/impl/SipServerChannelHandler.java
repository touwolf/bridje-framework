
package org.bridje.sip.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bridje.ioc.Ioc;
import org.bridje.sip.SipRequest;
import org.bridje.sip.SipResponse;

public class SipServerChannelHandler extends SimpleChannelInboundHandler<SipRequest>
{

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SipRequest msg) throws Exception
    {
        RootSipBridlet rootBridlet = Ioc.context().find(RootSipBridlet.class);
        SipResponse resp = rootBridlet.handle(msg);
        resp.setRecipient(msg.getSender());
        msg.getHeaders()
                    .entrySet().stream()
                    .filter(e -> !resp.getHeaders().containsKey(e.getKey()))
                    .forEach(e -> resp.getHeaders().put(e.getKey(), e.getValue()));
        ctx.channel().writeAndFlush(resp);
    }
}
