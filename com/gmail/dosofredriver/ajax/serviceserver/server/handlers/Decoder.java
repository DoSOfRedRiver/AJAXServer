package com.gmail.dosofredriver.ajax.serviceserver.server.handlers;

import com.gmail.dosofredriver.ajax.serviceserver.server.session.Session;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import java.nio.ByteBuffer;

/**
 * Date: 27.02.13
 * Time: 2:07
 *
 * @author DoSOfRR
 */
public class Decoder extends OneToOneDecoder {
    private int length;

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {

        if (msg instanceof ChannelBuffer) {
            ByteBuffer data = ByteBuffer.wrap(((ChannelBuffer) msg).array());
            return new Session(data, channel);
        }

        return null;
    }
}
