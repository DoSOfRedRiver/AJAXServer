package com.gmail.dosofredriver.ajax.serviceserver.util.parser;

import com.sun.istack.internal.logging.Logger;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 03.11.12
 * Time: 18:24
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    public Vector<Class>  hm = new Vector<Class>();//p<Class, String>();
    public Vector<String> hn = new Vector<String>();

    private Class [] typesToArray(Collection<? extends Class> src) {
        Class [] result = new Class[src.size()];
        int i = 0;

        for (Class arg : src) {
            result[i] = arg;
            i++;
        }
        System.out.print('c');
        return result;
    }

    public String toString(String str, Integer i, Character c) {
        return str + i + c;
    }

    public static void main (String [] args) throws Exception{
        ExecutorService es = Executors.newFixedThreadPool(8);

    }
}
