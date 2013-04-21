package com.gmail.dosofredriver.ajax.serviceserver.util.invoke;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Date: 29.03.13
 * Time: 1:48
 *
 * @author DoSOfRR
 */
public class InvokeCache {
    private static InvokeCache ourInstance = new InvokeCache();


    private ConcurrentHashMap<String, Class>    cachedClasses = new ConcurrentHashMap();
    private ConcurrentHashMap<String, Method>   cachedMethods = new ConcurrentHashMap();

    public static InvokeCache getInstance() {
        return ourInstance;
    }

    private InvokeCache() {
    }

    public Method findMethod(String methodName) {
        return cachedMethods.get(methodName);
    }

    public Class findClass(String className) {
        return cachedClasses.get(className);
    }

    public boolean cacheMethod(Method method) {
        if (findMethod(method.toString()) == null) {
            cachedMethods.put(method.toString(), method);
            return true;
        } else {
            return false;
        }
    }

    public boolean cacheClass(Class clazz) {
        if (findClass(clazz.getName()) == null) {
            cachedClasses.put(clazz.getName(), clazz);
            return true;
        } else {
            return false;
        }
    }

    private void clearCache() {
        cachedClasses.clear();
        cachedMethods.clear();
    }
}
