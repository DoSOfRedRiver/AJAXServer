package com.gmail.dosofredriver.ajax.serviceserver.service;

import com.gmail.dosofredriver.ajax.serviceserver.util.parser.MethodStruct;

/**
 * Date: 07.03.13
 * Time: 21:19
 *
 * @author DoSOfRR
 */
public abstract class Service {

    abstract protected MethodStruct parse(byte [] request);

    abstract protected byte [] invoke(MethodStruct struct);

    public final byte [] service(byte [] request) {
        MethodStruct struct;

        struct = parse(request);

        return invoke(struct);
    }
}
