package com.gmail.dosofredriver.ajax.serviceserver.server.session;

import org.jboss.netty.channel.Channel;

import java.nio.ByteBuffer;

/**
 * Date: 02.03.13
 * Time: 19:13
 *
 * @author DoSOfRR
 */
public class Session {
    private ByteBuffer  data;
    private Channel     channel;

    public Session(ByteBuffer data, Channel channel) {
        this.setChannel(channel);
        this.setData(data);
    }

    public ByteBuffer getData() {
        return data;
    }

    public void setData(ByteBuffer data) {
        this.data = data;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
