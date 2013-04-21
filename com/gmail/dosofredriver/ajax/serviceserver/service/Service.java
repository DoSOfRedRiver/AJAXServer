package com.gmail.dosofredriver.ajax.serviceserver.service;

import com.gmail.dosofredriver.ajax.serviceserver.server.session.Session;
import com.gmail.dosofredriver.ajax.serviceserver.util.invoke.Invoker;
import com.gmail.dosofredriver.ajax.serviceserver.util.parser.Parser;

import java.nio.ByteBuffer;

/**
 * Date: 07.03.13
 * Time: 21:19
 *
 * @author DoSOfRR
 */
public class Service {
    public static Session service(Session session) throws Exception {
        String request = new String(session.getData().array());                                     //get AJAX request

        Object result = Invoker.invoke(Parser.parseRequest(request));                               //parse it, and call application

        return new Session(ByteBuffer.wrap(result.toString().getBytes()), session.getChannel());    //return new Session
    }
}
