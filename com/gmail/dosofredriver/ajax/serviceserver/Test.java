package com.gmail.dosofredriver.ajax.serviceserver;

import com.gmail.dosofredriver.ajax.serviceserver.server.TCPServer;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 03.11.12
 * Time: 18:24
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public static void main (String ... args) throws InterruptedException {
        final TCPServer server = new TCPServer();

        server.start();
    }
}
