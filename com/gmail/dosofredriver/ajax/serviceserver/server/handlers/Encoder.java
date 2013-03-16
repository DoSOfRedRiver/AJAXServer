package com.gmail.dosofredriver.ajax.serviceserver.server.handlers;

import com.gmail.dosofredriver.ajax.serviceserver.server.session.Session;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * Date: 02.03.13
 * Time: 19:06
 *
 * @author DoSOfRR
 */
public class Encoder extends OneToOneEncoder {
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        System.out.println("Encode");
        if (msg instanceof Session) {
            return ChannelBuffers.wrappedBuffer(((Session) msg).getData());
        } else {   System.out.println("EMPTY BUFFER!");
            return ChannelBuffers.EMPTY_BUFFER;
        } //note the writer should manually close channel after the message walk through pipeline
    }
}
