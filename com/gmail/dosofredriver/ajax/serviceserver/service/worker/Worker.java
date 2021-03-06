package com.gmail.dosofredriver.ajax.serviceserver.service.worker;

import com.gmail.dosofredriver.ajax.serviceserver.server.session.Session;
import com.gmail.dosofredriver.ajax.serviceserver.server.session.SessionController;
import com.gmail.dosofredriver.ajax.serviceserver.service.Service;
import com.gmail.dosofredriver.ajax.serviceserver.util.logger.ServerLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Date: 06.03.13
 * Time: 21:04
 *
 * @author DoSOfRR
 */
public class Worker {
    public static final int DEFAULT_POOL_SIZE = 8;

    private volatile boolean stopFlag = false;

    private ExecutorService executor;
    private ServerLogger    logger;

    private boolean isLogged = false;

    public Worker() {
        this(DEFAULT_POOL_SIZE);
    }

    public Worker(int pool_size) {
        init(pool_size);
    }

    private void init(int pool_size) {
        executor = Executors.newFixedThreadPool(pool_size);
    }

    public void start() throws InterruptedException {
        if (isLogged) {
            logger.log(Level.INFO, "Worker started.");
        }

        stopFlag = false;

        while (!stopFlag) {
            final Session session = SessionController.getInstance().getSession();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        SessionController.getInstance().response(Service.service(session));
                    } catch (Exception e) {
                        if (isLogged) {
                            logger.log(Level.SEVERE, "An error occupied while parsing request!", e);
                            session.getChannel().close();    //note try to use AutoCloseable
                        }
                    }
                }
            });
        }
    }

    public void stop() throws InterruptedException {
        stopFlag = true;
        executor.awaitTermination(10, TimeUnit.SECONDS);  //note хуй знает почему таски работающие висят :\

        if (isLogged) {
            logger.log(Level.INFO, "Worker was manually stopped.");
        }
    }

    public void setLogger(ServerLogger logger) {
        if (logger == null) {
            throw new NullPointerException("Logger can not be null!");
        }

        this.logger = logger;
        isLogged = true;
    }
}
