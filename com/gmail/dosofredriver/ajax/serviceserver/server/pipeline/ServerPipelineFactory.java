package com.gmail.dosofredriver.ajax.serviceserver.server.pipeline;

import com.gmail.dosofredriver.ajax.serviceserver.server.handlers.ConnectionFilter;
import com.gmail.dosofredriver.ajax.serviceserver.server.handlers.DataReader;
import com.gmail.dosofredriver.ajax.serviceserver.server.handlers.Decoder;
import com.gmail.dosofredriver.ajax.serviceserver.server.handlers.Encoder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 * Date: 26.02.13
 * Time: 23:26
 *
 * @author DoSOfRR
 */
public class ServerPipelineFactory implements ChannelPipelineFactory {
    @Override
    public ChannelPipeline getPipeline() throws Exception {
        ConnectionFilter filter = new ConnectionFilter();
         //todo config
        ChannelPipeline result = Channels.pipeline(new Decoder(), new Encoder(), new DataReader());

        return result;
    }
}
