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
    public final static String DEFAUL_LOG_NAME = "log.html";

    private Logger logger;

    public ServerLogger(String name) throws IOException {
        init(name, DEFAUL_LOG_NAME);
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
}
