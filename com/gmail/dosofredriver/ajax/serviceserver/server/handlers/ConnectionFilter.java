package com.gmail.dosofredriver.ajax.serviceserver.server.handlers;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 26.02.13
 * Time: 14:49
 *
 * @author DoSOfRR
 */
public class ConnectionFilter extends SimpleChannelUpstreamHandler {
    public static final String DEFAULT_WHITELIST_PATH = "config/whitelist.txt";
    public static final String DEFAULT_BLACKLIST_PATH = "config/blacklist.txt";

    public static final int WHITELIST_ON = 0;
    public static final int BLACKLIST_ON = 1;
    public static final int WHITELIST_OFF = 3;
    public static final int BLACKLIST_OFF = 4;

    private final Set<String> whiteList = new HashSet<>();
    private final Set<String> blackList = new HashSet<>();

    private String whiteListPath;
    private String blackListPath;

    private boolean isWhiteListOn;
    private boolean isBlackListOn;

    public ConnectionFilter() {
        isWhiteListOn = false;
        isBlackListOn = false;
        whiteListPath = DEFAULT_WHITELIST_PATH;
        blackListPath = DEFAULT_BLACKLIST_PATH;
    }

    public void setOption(int list, String listPath) throws IllegalArgumentException, IOException {
        switch (list) {
            case BLACKLIST_ON: {
                blackListPath = listPath;
                isBlackListOn = true;
            } break;

            case WHITELIST_ON: {
                whiteListPath = listPath;
                isWhiteListOn = true;
            } break;

            default: throw new IllegalArgumentException();
        }

        loadList();
    }

    public void setOption(int list) throws IllegalArgumentException, IOException {
        switch (list) {
            case BLACKLIST_ON:  isBlackListOn = true; break;
            case WHITELIST_ON:  isWhiteListOn = true; break;
            case BLACKLIST_OFF: isBlackListOn = false; break;
            case WHITELIST_OFF: isWhiteListOn = false; break;
            default: throw new IllegalArgumentException();
        }

        loadList();
    }

    public boolean reloadLists() throws IOException {
        loadList();

        return true;
    }

    @Override
    public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        String  addr;

        addr = ctx.getChannel().getRemoteAddress().toString();
        addr = addr.substring(1, addr.indexOf(':'));

        if (isBlackListOn) {
            if (blackList.contains(addr)) {
                ctx.getChannel().close();  //todo reject connection
            }
        }

        if (isWhiteListOn) {
            if (!whiteList.contains(addr)) {
                ctx.getChannel().close();  //todo reject connection
            }
        }
    }

    private void loadList() throws IOException {
        BufferedReader br;
        String line;

        if (isBlackListOn) {
            br = new BufferedReader(new FileReader(blackListPath));

            while ((line = br.readLine()) != null) {
                blackList.add(line);
            }
        }

        if (isWhiteListOn) {
            br = new BufferedReader(new FileReader(whiteListPath));

            while ((line = br.readLine()) != null) {
                whiteList.add(line);
            }
        }
    }
}
