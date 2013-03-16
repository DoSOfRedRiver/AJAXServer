package com.gmail.dosofredriver.ajax.serviceserver.server.handlers;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.SimpleChannelDownstreamHandler;

/**
 * Date: 26.02.13
 * Time: 23:51
 *
 * @author DoSOfRR
 */
public class DataWriter extends SimpleChannelDownstreamHandler {

    @Override
    public void handleDownstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        //todo
        System.out.println("Writer");

        super.handleDownstream(ctx, e);
    }
}
