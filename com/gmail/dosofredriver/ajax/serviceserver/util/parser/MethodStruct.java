package com.gmail.dosofredriver.ajax.serviceserver.util.parser;

import java.util.Set;

/**
 * Date: 07.03.13
 * Time: 21:27
 *
 * @author DoSOfRR
 */
public class MethodStruct {
    public Class [] argsTypes;
    public String   methodName;
    public Set      params;

    public MethodStruct(String methodName, Set params, Class [] argsTypes) {
        this.methodName = methodName;
        this.argsTypes  = argsTypes;
        this.params     = params;
    }
}
