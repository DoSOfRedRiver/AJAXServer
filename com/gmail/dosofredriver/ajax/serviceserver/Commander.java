package com.gmail.dosofredriver.ajax.serviceserver;

import com.gmail.dosofredriver.ajax.serviceserver.server.TCPServer;
import com.gmail.dosofredriver.ajax.serviceserver.service.worker.Worker;
import com.gmail.dosofredriver.ajax.serviceserver.util.logger.ServerLogger;
import com.gmail.dosofredriver.ajax.serviceserver.util.view.ConsoleView;
import com.gmail.dosofredriver.ajax.serviceserver.util.view.ViewInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 * Date: 06.03.13
 * Time: 22:54
 *
 * @author DoSOfRR
 */
public class Commander {
    private ViewInterface   view;
    private ServerLogger    logger;
    private TCPServer       server;
    private Thread          serverThread;
    private Worker          worker;

    private boolean isLogged = false;

    public Commander(ViewInterface view) {
        this.view = view;
        init();
    }

    private void init() {
        try {
            logger = new ServerLogger(this.getClass().getName(), "CommanderLog.html");
            server = new TCPServer(777, 4);

            if (logger != null) {
                isLogged = true;
                server.setLogger(logger);
            }
        } catch (Exception e) {
            System.err.println("An error occupied while initializing server: \n" + e);
        }
    }

    public void startServer() {
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                server.start();
            }
        });

        serverThread.setDaemon(true);
        serverThread.start();
    }

    public void stopServer() {
        server.stop();
        serverThread.interrupt();
    }

    public boolean deploy() {
        return false;
    }

    /*
     * This is a default program entry point.
     */
    public static void main(String ... args) {
        Commander commander = new Commander(new ConsoleView());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        boolean run = true;

        try {
            commander.view.showDefault();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (run) {
            byte [] message;

            try {
                message = br.readLine().getBytes();

                switch (commander.view.readMessage(message)) {
                    case Help           :   commander.view.showHelp(); break;
                    case Start_Server   :   commander.startServer(); break;
                    case Stop_Server    :   commander.stopServer(); break;
                    case Undefined      :   commander.view.showMessage("Undefined command."); break;
                    case Exit           :   run = false; break;

                    default             :   commander.view.showMessage("Not supported yet.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
