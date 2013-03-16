package com.gmail.dosofredriver.ajax.serviceserver.service.worker;

import com.gmail.dosofredriver.ajax.serviceserver.server.session.Session;
import com.gmail.dosofredriver.ajax.serviceserver.server.session.SessionController;
import com.gmail.dosofredriver.ajax.serviceserver.util.logger.ServerLogger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public Worker(ServerLogger logger) {
        this.logger = logger;
        init(DEFAULT_POOL_SIZE);
    }

    public Worker(int pool_size) {
        init(pool_size);
    }

    public Worker(int pool_size, ServerLogger logger) {
        this.logger = logger;
        init(pool_size);
    }

    private void init(int pool_size) {
        try {
            executor = Executors.newFixedThreadPool(pool_size);
            start();
        } catch (InterruptedException e) {
            //todo log this
        }
    }

    public void start() throws InterruptedException {
        while (!stopFlag) {
            Session session = SessionController.getInstance().getSession();

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    //todo start service methods
                    //send session object as resource
                }
            });
        }
    }
}
