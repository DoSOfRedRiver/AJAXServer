package com.gmail.dosofredriver.ajax.serviceserver.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 02.11.12
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class Server {
    private volatile boolean stopFlag = false;
    private ByteBuffer          buffer = ByteBuffer.allocate(16384);
    private ServerSocketChannel ssc;
    private ServerSocket        ss;
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public Server() {
        this.port = 777; //lucky number as default port
    }

    /*
     * This method is used to open server socket,
     * create channel and start server for listening
     * incoming connections.
     */
    private void startServer() {
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);

            ss = ssc.socket();
            ss.bind(new InetSocketAddress(port));

            Selector selector = Selector.open();

            ssc.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Listening on port: ");

            while (!stopFlag) {
                if (selector.select() == 0) {
                    continue;
                }

                for (SelectionKey key : selector.keys()) {
                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                        onAccept(selector);
                    } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                        onRead(key);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }

    /*
     * This method is used for reading incoming data from socket.
     *
     * @param key
     *              Selection key that used to identify connection.
     */
    private void onRead(SelectionKey key) throws IOException {
        SocketChannel sc = null;

        try {
            sc = (SocketChannel) key.channel();
            buffer.clear();
            sc.read(buffer);
            buffer.flip();

            if (buffer.limit() == 0) {
                removeConnection(key, sc);
            } else {
                //process buffer
            }
        } catch (IOException e) {
            key.cancel();
            sc.close();

            System.out.println("Closed: " + sc);
        }
    }

    /*
     * This method is used to get client socket, register new
     * channel and configure it.
     *
     * @param selector
     *          Selector for channel registration.
     */
    private void onAccept(Selector selector) throws IOException {
        Socket s = ss.accept();
        System.out.println("Incoming connection from: " + s);

        SocketChannel sc = s.getChannel();
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
    }


    /*
     * This method is used to remove "dead" connections.
     *
     * @param key
     *              Selection key that used to identify connection
     *
     * @param sc
     *              Socket channel that used to get socket from it.
     */
    private void removeConnection(SelectionKey key, SocketChannel sc) {
        Socket s = null;

        key.cancel();

        try {
            s = sc.socket();
            s.close();
        } catch( IOException e ) {
            System.err.println( "Error closing socket: " + s + "\nby\n " + e);    //log dat
        }
    }


}
