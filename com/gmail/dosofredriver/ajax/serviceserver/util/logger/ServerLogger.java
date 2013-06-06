package com.gmail.dosofredriver.ajax.serviceserver.util.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: 09.12.12
 * Time: 22:15
 *
 * @author DoSOfRR
 */
public class ServerLogger {
    public final static String DEFAULT_LOG_NAME = "log.html";
    public final static String DEFAULT_NAME     = "Server logger";

    private Logger logger;

    public ServerLogger() throws IOException {
        init(DEFAULT_NAME, DEFAULT_LOG_NAME);
    }

    public ServerLogger(String name) throws IOException {
        init(name, DEFAULT_LOG_NAME);
    }

    public ServerLogger(String name, String filename) throws IOException {
        init(name, filename);
    }

    private void init(String name, String filename) throws IOException {
        FileHandler fh = new FileHandler(filename);

        fh.setFormatter(new HTMLFormatter());
        logger = Logger.getLogger(name);
        logger.addHandler(fh);
    }


    public void log(Level level, String msg) {
        logger.log(level, msg);
    }

    public void log(Level level, String msg, Throwable ex) {
        logger.log(level, msg, ex);
    }
}
