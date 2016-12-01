
package org.bridje.sip.impl;

import org.bridje.sip.SipResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;
import java.util.List;

class SipResponseEncoder extends MessageToMessageEncoder<SipResponseMessage>
{
    @Override
    protected void encode(ChannelHandlerContext ctx, SipResponseMessage msg, List<Object> out) throws Exception
    {
        ByteBuf buff = ctx.alloc().buffer();
        buff.writeBytes(msg.getVersion().getBytes(CharsetUtil.UTF_8));
        buff.writeBytes(" ".getBytes(CharsetUtil.UTF_8));
        buff.writeBytes(String.valueOf(msg.getStatusCode()).getBytes(CharsetUtil.UTF_8));
        buff.writeBytes(" ".getBytes(CharsetUtil.UTF_8));
        buff.writeBytes(msg.getMessage().getBytes(CharsetUtil.UTF_8));
        buff.writeBytes(" ".getBytes(CharsetUtil.UTF_8));
        buff.writeBytes("\n".getBytes(CharsetUtil.UTF_8));
        msg.getHeaders().forEach((k, v) -> 
        {
            buff.writeBytes(k.getBytes(CharsetUtil.UTF_8));
            buff.writeBytes(": ".getBytes(CharsetUtil.UTF_8));
            buff.writeBytes(v.getBytes(CharsetUtil.UTF_8));
            buff.writeBytes("\n".getBytes(CharsetUtil.UTF_8));
        });
        buff.writeBytes("\n".getBytes(CharsetUtil.UTF_8));
        DatagramPacket packet = new DatagramPacket(buff, msg.getRecipient());
        out.add(packet);
    }
}
