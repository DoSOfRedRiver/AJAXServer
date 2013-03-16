package com.gmail.dosofredriver.ajax.serviceserver.util.view;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Date: 07.03.13
 * Time: 22:45
 *
 * @author DoSOfRR
 */
public class ConsoleView implements ViewInterface {
    private OutputStream os;

    /*
     * Creates an console view.
     *
     * @param os
     *      Specifies output stream that will be used to print messages.
     */
    public ConsoleView(OutputStream os) {
        this.os = os;
    }

    public ConsoleView() {
        //System.out as default output stream.
        this.os = System.out;
    }

    @Override
    public void showDefault() throws IOException {
        os.write("Welcome to server administration panel.".getBytes());
        os.write("\nType \"-help\" to print help".getBytes());
    }

    @Override
    public void showHelp() throws IOException {
        os.write("\nType \"-start\" to start server.".getBytes());
        os.write("\nType \"-stop\" to stop server.".getBytes());
        os.write("\nType \"-restart\" to restart server.".getBytes());
        os.write("\nType \"-help\" to get help.".getBytes());
    }

    @Override
    public void showMessage(String message) throws IOException {
        os.write(message.getBytes());
    }

    @Override
    public void showAuthorize() throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public Commands readMessage(byte[] message) throws IOException {
        String strmsg = new String(message);

        switch (strmsg) {
            case "-start"   : return Commands.Start_Server;
            case "-stop"    : return Commands.Stop_Server;
            case "-help"    : return Commands.Help;
            case "-restart" : return Commands.Restart_Server;

            default         : return Commands.Undefined;
        }
    }
}
