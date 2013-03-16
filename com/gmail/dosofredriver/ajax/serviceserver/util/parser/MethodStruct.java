package com.gmail.dosofredriver.ajax.serviceserver.util.parser;

import java.util.Map;

/**
 * Date: 07.03.13
 * Time: 21:27
 *
 * @author DoSOfRR
 */
public class MethodStruct<T> {
    public Map<T, ? extends T>  params;
    public String               name;
}
