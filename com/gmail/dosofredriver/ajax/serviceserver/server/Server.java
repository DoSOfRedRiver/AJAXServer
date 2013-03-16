package com.gmail.dosofredriver.ajax.serviceserver.server;

import com.gmail.dosofredriver.ajax.serviceserver.service.RequestHandler;
import com.gmail.dosofredriver.ajax.serviceserver.util.logger.ServerLogger;
import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 02.11.12
 * Time: 15:15
 * To change this template use File | Settings | File Templates.
 */
public class Server {
    public final static int DEFAULT_POOL_SIZE   = 8;
    public final static int DEFAULT_PORT        = 777;

    private volatile boolean debugmode;
    private volatile long    controlUnit;

    private RequestHandler  requestHandler;
    private ServerLogger    logger;

    private AsynchronousServerSocketChannel     assc;
    private AsynchronousChannelGroup            group;

    private int     port;


    public Server() {
        init(DEFAULT_PORT, DEFAULT_POOL_SIZE, null);
    }

    public Server(RequestHandler requestHandler) {
        init(DEFAULT_PORT, DEFAULT_POOL_SIZE, requestHandler);
    }

    public Server(int port) {
        init(port, DEFAULT_POOL_SIZE, null);
    }

    public Server(int port, RequestHandler requestHandler) {
        init(port, DEFAULT_POOL_SIZE, requestHandler);
    }

    public Server(int port, int pool_size) {
        init(port, pool_size, null);
    }

    public Server(int port, int pool_size, RequestHandler requestHandler) {
        init(port, pool_size, requestHandler);
    }

    /*
     * Initializes server with given parameters
     *
     * @param port
     *          Server port
     *
     * @param pool_size
     *          Size of the thread pool
     *
     */
    private void init(int port, final int pool_size, RequestHandler requestHandler) {
        setRequestHandler(requestHandler);
        this.port = port;
        debugmode = true;
        createLogger();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start(pool_size);
                } catch (IOException | InterruptedException e) {
                    System.err.println("WARNING: An error occupied, while starting server. For more info see log.");

                    if (debugmode) {
                        logger.log(Level.SEVERE, "Error while stating server", e);
                    } else {
                        e.printStackTrace(); //if logger is unavailable
                    }
                }
            }
        }).start();
    }



    /*
     * This method is used to open server socket,
     * create channel and start server for listening
     * incoming connections.
     *
     * @param pool_size
     *              Determines the size of thread pool.
     */
    public void start(int pool_size) throws IOException, InterruptedException {
        controlUnit = Long.MAX_VALUE;

        group = AsynchronousChannelGroup.withFixedThreadPool(pool_size, new ThreadFactory() {
            @Override
            @NotNull
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        });

        try {
            assc = AsynchronousServerSocketChannel.open(group).bind(new InetSocketAddress(port));
        } catch (BindException e) {
            System.err.println("An bind exception occupied. Possible port already in use. \n" + e);
            if (debugmode) {
                logger.log(Level.SEVERE,"An bind exception occupied. Possible port already in use.", e);
            }

            System.exit(0);
        }


        System.out.println("Server started on port " + port);
        if (debugmode) {
            logger.log(Level.INFO, "Server started on port " + port);
        }

        assc.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
            @Override
            public void completed(AsynchronousSocketChannel result, Object attachment) {
                assc.accept(null, this);
                handleComplete(result, attachment);
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                //todo failed
            }
        });

        group.awaitTermination(controlUnit, TimeUnit.SECONDS);
    }

    /*
     * Stops server
     */
    public void stop() {
        try {
            controlUnit = 0;
            assc.close();
        } catch (IOException e) {
            System.out.println("An error occupied while stoping server. For more info see log.");
            if (debugmode) {
                logger.log(Level.SEVERE, "An error occupied while stoping server. \n" + e );
            } else {
                e.printStackTrace();
            }
        }
    }

    /*
     * Creates a logger, for logging information.
     */
    private void createLogger() {
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
     *  Handles incoming connection. This method is invoked directly from
     *  CompletionHandler's complete method.
     */
    private void handleComplete(AsynchronousSocketChannel result, Object attachment) {
        if (requestHandler != null) {
            //result.write(requestHandler.initialOutcomingRequest());
            requestHandler.incomingRequest(result);
            //result.write(requestHandler.finalOutcomingRequest());
        } else {
            System.out.println("WARNING! The requestHandler is not set. All requests will be dropped!");
            if (debugmode) {
                logger.log(Level.SEVERE, "WARNING! The requestHandler is not set. All requests will be dropped!");
            }
        }

        try {
            System.out.println("Connection from: " + result.getRemoteAddress());
            if (debugmode) {
                logger.log(Level.INFO, "Connection from: " + result.getRemoteAddress());
            }
        } catch (Exception e) {
            if (debugmode) {
                logger.log(Level.WARNING, "An error on complete(): \n" + e);
            }
        }
    }

    public void setDebugmode(boolean debugmode) {
        this.debugmode = debugmode;
    }

    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }


    /*private void start(int pool_size) {
        executor    = Executors.newFixedThreadPool(pool_size);
        buffer      = ByteBuffer.allocate(16384123);           //16384

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

                Set<SelectionKey> keys = selector.keys();

                for (Iterator<SelectionKey> iterator = keys.iterator(); iterator.hasNext();) {
                    SelectionKey key = iterator.next();

                    if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                        executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                onAccept(selector);
                            }
                        });
                    } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
                        onRead(key);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);

            if (debugmode) {
                logger.log(Level.SEVERE, "Error in main program cycle: " + e);
            }
        }

    }*/
}
