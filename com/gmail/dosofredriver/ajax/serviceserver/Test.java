package com.gmail.dosofredriver.ajax.serviceserver;

import com.gmail.dosofredriver.ajax.serviceserver.service.annotations.ServiceMethod;
import com.gmail.dosofredriver.ajax.serviceserver.util.invoke.Invoker;
import com.gmail.dosofredriver.ajax.serviceserver.util.parser.Parser;

/**
 * Created with IntelliJ IDEA.
 * User: Александр
 * Date: 03.11.12
 * Time: 18:24
 * To change this template use File | Settings | File Templates.
 */
public class Test {
    //@ServiceMethod
    public static void test() {
        System.out.println("Test");
    }

    @ServiceMethod
    public String summarize(int a, int b) {
        return "" + (a+b);
    }

    public static void main (String ... args) throws Exception {
        String validJSON = "{\n" +
                "    \"methodName\": \"com.gmail.dosofredriver.ajax.serviceserver.Test.test\",\n" +
                "    \"params\": [\n" +
                "    ]\n" +
                "}";

        System.out.println(Invoker.invoke(Parser.parseRequest(validJSON)));
    }
}
