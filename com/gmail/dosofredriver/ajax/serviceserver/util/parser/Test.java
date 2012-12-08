package com.gmail.dosofredriver.ajax.serviceserver.util.parser;

import com.sun.istack.internal.logging.Logger;

import java.lang.reflect.Method;
import java.util.*;
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
        Test t = new Test();



        Logger logger = Logger.getLogger(Test.class);
        logger.log(Level.ALL, "test log");

        System.out.println(logger);

        t.hm.add(Class.forName("java.lang.String"));
        t.hm.add(Class.forName("java.lang.Integer"));
        t.hm.add(Class.forName("java.lang.Character"));

        t.hn.add("lol");
        t.hn.add("14");
        t.hn.add("p");

        Class c = Class.forName("java.lang.String");

        t.hn.get(1);


        Method m = Test.class.getMethod("toString", t.typesToArray(t.hm));
        Method m1 = Class.forName("java.lang.Integer").getMethod("valueOf", Class.forName("java.lang.String"));
        Method m2 = Class.forName("java.lang.Character").getMethod("valueOf", Class.forName("java.lang.String"));
        System.out.println(m);
        System.out.println(m.invoke(Test.class, t.hn.get(0), m1.invoke("java.lang.Integer",t.hn.get(1)), m2.invoke("java.lang.Character",t.hn.get(2))));

    }
}
