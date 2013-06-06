package com.gmail.dosofredriver.ajax.serviceserver;

import com.gmail.dosofredriver.ajax.serviceserver.server.TCPServer;
import com.gmail.dosofredriver.ajax.serviceserver.service.worker.Worker;
import com.gmail.dosofredriver.ajax.serviceserver.util.configure.Configurator;
import com.gmail.dosofredriver.ajax.serviceserver.util.logger.ServerLogger;
import com.gmail.dosofredriver.ajax.serviceserver.util.view.ConsoleView;
import com.gmail.dosofredriver.ajax.serviceserver.util.view.ViewInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * Date: 06.03.13
 * Time: 22:54
 *
 * @author DoSOfRR
 */
public class Commander {
    public static final String  DEFAULT_CONFIG_PATH = "config/config.txt";

    private String          configPath  = DEFAULT_CONFIG_PATH;

    private ViewInterface   view;
    private ServerLogger    logger;
    private TCPServer       server;
    private Thread          serverThread;
    private Thread          workerThread;
    private Worker          worker;

    private boolean autoStart   = false;
    private boolean isLogged    = false;

    public Commander(ViewInterface view, String ... args) {
        this.view = view;
        init(args);
    }



    private void init(String ... args) {
        try {
            parseArgs(args);

            Configurator config = new Configurator(configPath);

            logger = config.getConfiguredLogger();
            server = config.getConfiguredServer();
            worker = config.getConfiguredWorker();
            worker.setLogger(logger);
            server.setLogger(logger);
            server.setFilter(config.getConfiguredFilter());

            if (logger != null) {
                isLogged = true;
                server.setLogger(logger);
            }

            if (autoStart) {
                this.startServer();
            }
        } catch (Exception e) {
            System.err.println("An error occupied while initializing server: \n" + e);
        }
    }

    private void parseArgs(String ... args) {
        for (String arg : args) {
            String value = null;
            String type;

            if (arg.contains(":")) {
                type  = arg.substring(0, arg.indexOf(':'));
                value = arg.substring(arg.indexOf(':')+1, arg.length());
            } else {
                type = arg;
            }

            switch (type) {
                case "configpath"   : configPath = "value"; break;
                case "autostart"    : autoStart  = true;    break;

                default: {
                    System.out.println("Option " + value + " is not supported!");
                    System.exit(0);
                }
            }
        }
    }

    public void startServer() {
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                server.start();
            }
        });
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    worker.start();        //todo ex
                } catch (InterruptedException e) {
                    logger.log(Level.SEVERE, "Can not start worker!", e);
                }
            }
        });

        serverThread.setDaemon(true);
        serverThread.start();
        workerThread.setDaemon(true);
        workerThread.start();
    }

    public void stopServer() {
        try {
            server.stop();
            serverThread.interrupt();
            worker.stop();
            workerThread.interrupt();
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "An error occupied while stopping server", e);
        }
    }

    public boolean deploy() {
        return false;
    }

    /*
     * This is a default program entry point.
     */
    public static void main(String ... args) {
        Commander commander = new Commander(new ConsoleView(), args);
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
