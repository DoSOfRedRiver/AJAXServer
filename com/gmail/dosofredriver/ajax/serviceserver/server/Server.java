package com.gmail.dosofredriver.ajax.serviceserver.server;

import com.gmail.dosofredriver.ajax.serviceserver.util.logger.ServerLogger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 02.11.12
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class Server implements Runnable {
    public final static int DEFAULT_POOL_SIZE = 8;

    private volatile boolean stopFlag = false;
    private volatile boolean debugmode;

    private ServerLogger logger;

    private ExecutorService     executor;
    private Selector            selector;
    private ByteBuffer          buffer;
    private ServerSocketChannel ssc;
    private ServerSocket        ss;

    private int                 port;


    public Server() {
        this.port = 777; //lucky number as default port
        startServer(DEFAULT_POOL_SIZE);

        try {
            logger = new ServerLogger(this.getClass().getName());
        } catch (IOException e) {
            System.err.println("WARNING: FAILED TO CREATE LOGGER!");
        }

        if (logger == null) {
            debugmode = false;
        }
    }

    public Server(int port) {
        this.port   = port;
        startServer(DEFAULT_POOL_SIZE);

        try {
            logger = new ServerLogger(this.getClass().getName());
        } catch (IOException e) {
            System.err.println("WARNING: FAILED TO CREATE LOGGER!");
        }

        if (logger == null) {
            debugmode = false;
        }
    }

    public Server(int port, int pool_size) {
        this.port = port;
        startServer(pool_size);

        try {
            logger = new ServerLogger(this.getClass().getName());
        } catch (IOException e) {
            System.err.println("WARNING: FAILED TO CREATE LOGGER!");
        }

        if (logger == null) {
            debugmode = false;
        }
    }

    /*
     * This method is used to open server socket,
     * create channel and start server for listening
     * incoming connections.
     */
    private void startServer(int pool_size) {
        executor    = Executors.newFixedThreadPool(pool_size);
        buffer      = ByteBuffer.allocate(16384);

        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);

            ss = ssc.socket();
            ss.bind(new InetSocketAddress(port));

            selector = Selector.open();

            ssc.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Listening on port: " + port);

            if (debugmode) {
                logger.log(Level.INFO, "Listening on port: " + port);
            }

            while (!stopFlag) {
                if (selector.select() == 0) {
                    continue;
                }

                executor.execute(this);
            }
        } catch (IOException e) {
            if (debugmode) {
                logger.log(Level.SEVERE, "Error in main program cycle: " + e);
            }
        }

    }

    @Override
    public void run() {
        for (final SelectionKey key : selector.keys()) {
            if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                onAccept(selector);
            } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                onRead(key);
            }
        }
    }

    /*
     * This method is used for reading incoming data from socket.
     *
     * @param key
     *              Selection key that used to identify connection.
     */
    private void onRead(SelectionKey key) {
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
            try {
                sc.close();
            } catch (IOException e1) {
                if (debugmode) {
                    logger.log(Level.WARNING, "Error while closing socket");
                }
            }

            if (debugmode) {
                logger.log(Level.WARNING, "Closed: " + sc);
            }
        }
    }

    /*
     * This method is used to get client socket, register new
     * channel and configure it.
     *
     * @param selector
     *          Selector for channel registration.
     */
    private void onAccept(Selector selector) {
        try {
            Socket s = ss.accept();
            System.out.println("Incoming connection from: " + s);

            if(debugmode) {
                logger.log(Level.INFO, "Incoming connection from: " + s);
            }

            SocketChannel sc = s.getChannel();
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            logger.log(Level.WARNING, "onAccept: " + e);
        }
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
            logger.log(Level.WARNING, "Error closing socket: " + s + "\nby\n " + e);
        }
    }

    public void setDebugmode(boolean debugmode) {
        this.debugmode = debugmode;
    }
}
