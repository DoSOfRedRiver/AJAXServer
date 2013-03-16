package com.gmail.dosofredriver.ajax.serviceserver.util.view;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Date: 07.03.13
 * Time: 21:47
 *
 * @author DoSOfRR
 */

/*
 * This interface realizes view for admin panel. With that interface you can specify
 * your view without changing <code>Commander</code> controller.
 */
public interface ViewInterface {
    BlockingQueue<Commands> messages = new LinkedBlockingQueue<>();

    /*
     * This method is used to show initial interface and/or message.
     * Here you can write some tips for users, or send HTML form, for example.
     */
    public void showDefault() throws IOException;

    /*
     * Show help, that can describe commands, params etc.
     */
    public void showHelp() throws IOException;

    /*
     * Show some message that server send to user.
     */
    public void showMessage(String message) throws IOException;

    /*
     * Show authorize form/message.
     */
    public void showAuthorize() throws IOException;

    /*
     * This method will invoked when an message will arrived.
     * Method should parse incoming <code>message</code> and
     * return result as <code>Commands</code> enum.
     */
    public Commands readMessage(byte [] message) throws IOException;


}
