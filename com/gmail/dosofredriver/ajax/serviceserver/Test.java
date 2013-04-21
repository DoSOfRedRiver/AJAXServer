package com.gmail.dosofredriver.ajax.serviceserver;

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
    public void azaza(int a) {
        System.out.println("Azazaa:" + a);
    }

    public String summarize(int a, int b) {
        return "" + (a+b);
    }

    public static void main (String ... args) throws Exception {
        String validJSON = "{\n" +
                "    \"methodName\": \"com.gmail.dosofredriver.ajax.serviceserver.Test.summarize\",\n" +
                "    \"params\": [\n" +
                "        {\n" +
                "            \"type\": \"int\",\n" +
                "            \"value\": \"1\"\n" +
                "        },\n" +
                "\t\t{\n" +
                "            \"type\": \"int\",\n" +
                "            \"value\": \"3\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        System.out.println(Invoker.invoke(Parser.parseRequest(validJSON)));

    }
}
