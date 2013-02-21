package com.gmail.dosofredriver.ajax.serviceserver.service;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * This interface is used by <code>Server</code> to handle incoming and outcoming requests.
 *
 * The {@link #incomingRequest(java.nio.channels.AsynchronousSocketChannel)  incomingRequest}
 * always invoked when necessary to process the incoming request.
 *
 * If you need to send some static content before or after you handle request use
 * {@link #initialOutcomingRequest()} or/and  {@link #finalOutcomingRequest()}.
 *
 * @author DoSOfRR
 */
public interface RequestHandler {


    /*
     * This method is used to handle incoming request.
     *
     * @param asc
     *          Stores the connection wrapped with AsynchronousSocketChannel.
     */
    public void incomingRequest(AsynchronousSocketChannel asc);



    /*
     *  Handles outcoming request. This method automatically invoked
     *  before {@link #incomingRequest(java.nio.ByteBuffer, java.nio.channels.AsynchronousSocketChannel)  incomingRequest}
     *
     *  @return
     *          <code>ByteBuffer</code> that will be sent to client.
     */
    public ByteBuffer initialOutcomingRequest();



    /*
     *  Handles outcoming request. This method automatically invoked
     *  after {@link #incomingRequest(java.nio.ByteBuffer, java.nio.channels.AsynchronousSocketChannel)  incomingRequest}
     *
     *  @return
     *          <code>ByteBuffer</code> that will be sent to client.
     */
    public ByteBuffer finalOutcomingRequest();
}
