package com.gmail.dosofredriver.ajax.serviceserver.server.session;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.Channels;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Date: 27.02.13
 * Time: 14:42
 *
 * @author DoSOfRR
 */
public class SessionController {
    private BlockingQueue<Session>  in;

    private static SessionController ourInstance = new SessionController();

    public static SessionController getInstance() {
        return ourInstance;
    }

    private SessionController() {
        in  = new LinkedBlockingQueue<>();
    }

    public void putSession(Session session) {
        in.add(session);   //put\add ?
    }

    public void response(Session session) {
        ChannelFuture future = Channels.write(session.getChannel(), session);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    public Session getSession() throws InterruptedException {
        return in.take();  //poll\take ?
    }
}
