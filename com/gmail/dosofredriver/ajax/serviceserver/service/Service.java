package com.gmail.dosofredriver.ajax.serviceserver.service;

import com.gmail.dosofredriver.ajax.serviceserver.util.parser.Parser;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * This class is uses to invoke methods, implemented by registered class.
 *
 * @author DoSOfRR
 *
 * @see com.gmail.dosofredriver.ajax.serviceserver.service.annotations.ServiceClass;
 * @see com.gmail.dosofredriver.ajax.serviceserver.service.annotations.ServiceMethod;
 * @see com.gmail.dosofredriver.ajax.serviceserver.util.parser.Parser;
 */
public class Service {
    public final static String DEFAULT_CONTROLLER_NAME = "InvokeController";

    /*
    *  Initializes a new <code>Service</code> object, that uses for dynamic
    *  method calling, and loading classes.
    *  This version of constructor use <code>DEFAULT_CONTROLLER_NAME</code>
    *  variable as default name of controller that should be used.
    */
    public Service() {
        register(DEFAULT_CONTROLLER_NAME);
    }

    /*
    *  Initializes a new <code>Service</code> object, that uses for dynamic
    *  method calling, and loading classes.
    *
    *  @param args
    *          Uses as argument for <code>register</code> class, and set it as
    *          service controller.
    */
    public Service(String controllerName) {
        register(controllerName);
    }

    public Object callMethod(Parser parser) {
        Object result;
        Method method;

        try {

            method = this.getClass().getMethod(parser.getMethodName());
        } catch (NoSuchMethodException e) {
            System.err.println("Can not find method by name: " + parser.getMethodName());
        }

        return null;
    }

    /*
     * Converts collection to <code>Class</code> objects array.
     * @param src
     *          The source collection.
     */
    private Class [] typesToArray(Collection<? extends Class> src) {
        Class [] result = new Class[src.size()];
        int i = 0;

        for (Class arg : src) {
            result[i] = arg;
            i++;
        }
        return result;
    }

    /*
     * This method is uses for loading classed.
     *
     * @param arg
     *          Contents name of class presented for loading.
     */
    private void register(String arg) {}
}
