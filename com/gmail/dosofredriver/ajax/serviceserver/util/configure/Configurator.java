package com.gmail.dosofredriver.ajax.serviceserver.util.configure;

import com.gmail.dosofredriver.ajax.serviceserver.server.TCPServer;
import com.gmail.dosofredriver.ajax.serviceserver.server.handlers.ConnectionFilter;
import com.gmail.dosofredriver.ajax.serviceserver.service.worker.Worker;
import com.gmail.dosofredriver.ajax.serviceserver.util.logger.ServerLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Date: 26.04.13
 * Time: 2:14
 *
 * @author DoSOfRR
 */
public class Configurator {
    private ConnectionFilter    configuredFilter;
    private ServerLogger        configuredLogger;
    private TCPServer           configuredServer;
    private Worker              configuredWorker;

    public Configurator(String configPath) throws IOException {
        init(configPath);
    }

    private void init(String configPath) throws IOException {
        String data = readFile(configPath);

        if (data == null) {
            setDefaults();
            return;
        }

        parseConfig(data);
    }

    private void parseConfig(String data) throws IOException {
        String whiteList_path   = ConnectionFilter.DEFAULT_WHITELIST_PATH;
        String blackList_path   = ConnectionFilter.DEFAULT_BLACKLIST_PATH;
        String server_ip        = TCPServer.DEFAULT_IP_ADDRESS;
        String log_name         = ServerLogger.DEFAULT_LOG_NAME;

        int server_pool_size= TCPServer.DEFAULT_POOL_SIZE;
        int worker_pool_size= Worker.DEFAULT_POOL_SIZE;
        int server_port     = TCPServer.DEFAULT_PORT;

        boolean blackListOn = false;
        boolean whiteListOn = false;

        StringTokenizer tokenizer = new StringTokenizer(data);

        while (tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken();
            String type = line.substring(0, line.indexOf('='));
            String value= line.substring(line.indexOf('=')+1, line.length());

            switch (type.toLowerCase()) {
                case "server-pool-size" : server_pool_size  = Integer.parseInt(value);      break;
                case "worker-pool-size" : worker_pool_size  = Integer.parseInt(value);      break;
                case "server-port"      : server_port       = Integer.parseInt(value);      break;
                case "server-ip"        : server_ip         = value;                        break;
                case "log-name"         : log_name          = value;                        break;
                case "blacklist"        : blackListOn       = Boolean.parseBoolean(value);  break;
                case "whitelist"        : whiteListOn       = Boolean.parseBoolean(value);  break;
                case "whitelist-path"   : whiteList_path    = value;                        break;
                case "blacklist-path"   : blackList_path    = value;                        break;

                default: System.err.println("Unknown config option - " + value);
            }

            this.configuredWorker = new Worker(worker_pool_size);
            this.configuredServer = new TCPServer(server_port, server_pool_size, server_ip);
            this.configuredLogger = new ServerLogger(ServerLogger.DEFAULT_NAME, log_name);
            this.configuredFilter = new ConnectionFilter();

            //configure filter
            if (blackListOn) {
                this.configuredFilter.setOption(ConnectionFilter.BLACKLIST_ON, blackList_path);
            }
            if (whiteListOn) {
                this.configuredFilter.setOption(ConnectionFilter.WHITELIST_ON, whiteList_path);
            }
        }
    }

    private String readFile(String configPath) throws IOException {
        File file = new File(configPath);
        FileInputStream fis;
        byte [] buffer;

        if (!file.exists()) {
            return null;
        }

        fis = new FileInputStream(file);

        buffer = new byte[fis.available()];

        for (int b, count = 0; (b = fis.read()) != -1; count++) {
            buffer[count] = (byte) b;
        }

        return new String(buffer);
    }

    private void setDefaults() throws IOException {
        this.configuredFilter = new ConnectionFilter();
        this.configuredLogger = new ServerLogger();
        this.configuredServer = new TCPServer();
        this.configuredWorker = new Worker();
    }


    public ConnectionFilter getConfiguredFilter() {
        return configuredFilter;
    }

    public ServerLogger getConfiguredLogger() {
        return configuredLogger;
    }

    public TCPServer getConfiguredServer() {
        return configuredServer;
    }

    public Worker getConfiguredWorker() {
        return configuredWorker;
    }
}
