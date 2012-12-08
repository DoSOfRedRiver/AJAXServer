package com.gmail.dosofredriver.ajax.serviceserver.util.parser;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * The <code>String</code> class is used to parse input string,
 * and get code realised methods.
 *
 * @author DoSOfRR
 *
 * @see    com.gmail.dosofredriver.ajax.serviceserver.service.Service;
 */
public class Parser {
    /*
     * Default pattern, uses to find method prototype.
     */
    private final Pattern           pattern = Pattern.compile("[a-zA-Z]\\w+\\(([\\w\\s]+)?\\)");
    private Vector<Class>           methodArgsTypes  = new Vector<Class>();
    private Vector<String>          methodArgsValues = new Vector<String>();
    private String                  methodName;
    private String                  source;
    private Matcher                 matcher;

    /*
     * Allocates a new copy of parser with specified source string.
     *
     *  @param source
     *         String represented to parse.
     */
    public Parser(String source) {
        this.source = source;
        matcher = pattern.matcher(source);
        parse();
    }

    public void parse() {
        try {
            matcher.find();
            getParameters(matcher.group());
        } catch (IllegalStateException e) {
            System.err.println("Illegal state exception. Unknown command format!");
        } catch (ClassNotFoundException e) {
            System.err.println("Cant cast given type" + e);
        }
    }

    private void getParameters(String prototype) throws ClassNotFoundException {
        String src = prototype.substring(prototype.indexOf('(') + 1,prototype.length() - 1);
        StringTokenizer st = new StringTokenizer(src);

        while (st.hasMoreElements()) {
            String string = st.nextToken();
            String value;
            String  type;

            type  = string.substring(0, string.indexOf('_'));
            value = string.substring(string.indexOf('_')+1, string.length());

            getMethodArgsTypes().add(Class.forName(type));
            getMethodArgsValues().add(value);
        }
    }

    public String getMethodName() {
        return methodName;
    }

    public Vector<Class> getMethodArgsTypes() {
        return methodArgsTypes;
    }

    public Vector<String> getMethodArgsValues() {
        return methodArgsValues;
    }
}
