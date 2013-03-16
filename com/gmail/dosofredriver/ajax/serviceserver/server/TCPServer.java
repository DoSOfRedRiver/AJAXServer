package com.gmail.dosofredriver.ajax.serviceserver.server;

import com.gmail.dosofredriver.ajax.serviceserver.server.pipeline.ServerPipelineFactory;
import com.gmail.dosofredriver.ajax.serviceserver.util.logger.ServerLogger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: 25.02.13
 * Time: 23:47
 *
 * @author DoSOfRR
 */
public class TCPServer {
    public static final int     DEFAULT_POOL_SIZE   = 4;
    public static final int     DEFAULT_PORT        = 777;

    private Channel         mainChannel;
    private ServerBootstrap server;
    private ServerLogger    logger;

    private boolean isStarted = false;
    private boolean isLogged  = false;

    private int poolSize;
    private int port;

    public TCPServer() {
        this(DEFAULT_PORT, DEFAULT_POOL_SIZE);
    }

    public TCPServer(int port) {
        this(port, DEFAULT_POOL_SIZE);
    }

    public TCPServer(int port, int poolSize) {
        this.port       = port;
        this.poolSize   = poolSize;
    }

    public void start() {

        if (!isStarted) {
            try {
                ExecutorService bossExec = Executors.newFixedThreadPool(1);
                ExecutorService workExec = Executors.newFixedThreadPool(poolSize);

                server = new ServerBootstrap(new NioServerSocketChannelFactory(bossExec, workExec, poolSize));

                server.setOption("backlog", 500);
                server.setOption("connectTimeoutMillis", 10000);
                server.setPipelineFactory(new ServerPipelineFactory());

                mainChannel = server.bind(new InetSocketAddress(port));

                isStarted = true;


                if (isLogged) {
                    logger.log(Level.INFO, "Server started on " + port + "port.");
                }

                System.out.println("Server started on " + port + " port.");
            } catch (Exception e) {
                if (isLogged) {
                    System.out.println("An error occupied while starting server. See log for more info.");
                    logger.log(Level.SEVERE, "An error occupied while starting server.", e);
                } else {
                    System.out.println("An error occupied while starting server. \n" + e);
                }
            }
        } else {
            System.out.println("Server is already started!");
        }
    }


    public void stop() {

        if (isStarted) {
            ChannelFuture future = mainChannel.close();

            future.awaitUninterruptibly();
            server.shutdown();
            isStarted = false;

            if (isLogged) {
                logger.log(Level.INFO, "Server was manually stopped.");
            }
            System.out.println("Server stopped.");
        } else {
            System.out.println("Server is not started!");
        }
    }

    public ServerLogger getLogger() {
        return logger;
    }

    public void setLogger(ServerLogger logger) {
        if (logger == null) {
            throw new NullPointerException("Logger can not be null!");
        }

        this.logger = logger;
        isLogged = true;
    }
}
