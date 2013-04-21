package com.gmail.dosofredriver.ajax.serviceserver.server.handlers;

import com.gmail.dosofredriver.ajax.serviceserver.server.session.Session;
import com.gmail.dosofredriver.ajax.serviceserver.server.session.SessionController;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * Date: 26.02.13
 * Time: 23:51
 *
 * @author DoSOfRR
 */
public class DataReader extends SimpleChannelUpstreamHandler {

    @Override
    public void messageReceived(final ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        Object message = e.getMessage();

        if (message instanceof Session) {
            SessionController.getInstance().putSession((Session) message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        //todo log exception
        System.err.println("Err from: " + e.getChannel().getRemoteAddress());
        e.getChannel().close();
    }


}
