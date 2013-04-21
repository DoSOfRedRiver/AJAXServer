package com.gmail.dosofredriver.ajax.serviceserver.util.parser;

import com.gmail.dosofredriver.ajax.serviceserver.util.collections.DefaultOrderComparator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

/**
 * Date: 29.03.13
 * Time: 3:22
 *
 * @author DoSOfRR
 */
public class Parser {
    public static final String KEY_METHODNAME   = "methodName";
    public static final String KEY_PARAMS       = "params";

    private static JSONParser parser = new JSONParser();



    public static MethodStruct parseRequest(String query) throws Exception {
        Map<String, String> strParams = new TreeMap(new DefaultOrderComparator());
        String methodName;
        Set params;



        JSONObject jsonObject = (JSONObject)parser.parse(query);

        //get method name from request
        methodName = (String) jsonObject.get(KEY_METHODNAME);

        //get params from request
        JSONArray  ja = (JSONArray)  jsonObject.get(KEY_PARAMS);

        for (Object param : ja) {
            strParams.put(
                    (String) ((JSONObject) param).get("type"),
                    (String) ((JSONObject) param).get("value")
            );
        }

        //get classes from strings
        Class [] classes = getClasses(strParams.keySet());

        //get parameters
        params = getInstances(getMap(classes, strParams.values()));

        return new MethodStruct(methodName, params, classes);
    }



    public static Class [] getClasses(Set<String> classNames) throws ClassNotFoundException {
        Class [] result = new Class[classNames.size()];

        int i=0;
        for(String name : classNames) {
            switch (name) {
                case "int"      : result[i] = int.class;    break;
                case "long"     : result[i] = long.class;   break;
                case "boolean"  : result[i] = boolean.class;break;
                default         : result[i] = Class.forName(name);
            }
            i++;
        }

        return result;
    }



    private static Set getInstances(Map<Class, String> args) throws Exception {
        Set result = new TreeSet(new DefaultOrderComparator());

        for (Map.Entry<Class, String> entry : args.entrySet()) {
            switch (entry.getKey().toString()) {
                case "boolean"  : result.add(Boolean.parseBoolean(entry.getValue()));   break;
                case "long"     : result.add(Long.parseLong(entry.getValue()));         break;
                case "int"      : result.add(Integer.parseInt(entry.getValue()));       break;

                default         : result.add(getInstance(entry.getKey(), entry.getValue()));
            }

        }

        return result;
    }



    private static<T> T getInstance(Class<T> tClass, String value) throws Exception {
        return (T)tClass.getConstructor(String.class).newInstance(value);
    }



    private static Map<Class, String> getMap(Class [] classes, Collection<String> values) {
        Map<Class, String> result = new TreeMap(new DefaultOrderComparator());

        int i = 0;
        for (String value : values) {
            result.put(classes[i], value);
            i++;
        }

        return result;
    }
}
