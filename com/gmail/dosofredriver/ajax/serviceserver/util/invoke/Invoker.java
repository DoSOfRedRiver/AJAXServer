package com.gmail.dosofredriver.ajax.serviceserver.util.invoke;

import com.gmail.dosofredriver.ajax.serviceserver.service.annotations.ServiceMethod;
import com.gmail.dosofredriver.ajax.serviceserver.util.parser.MethodStruct;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Date: 28.03.13
 * Time: 2:11
 *
 * @author DoSOfRR
 */
public class Invoker {
    private static IllegalArgumentException cachedException = new IllegalArgumentException(new NullPointerException("Method can not be null!"));

    public static Object invoke(MethodStruct param) throws Exception {  //todo check security fuckups
        String methodName = param.methodName.substring(param.methodName.lastIndexOf(".")+1, param.methodName.length());
        String  className = param.methodName.substring(0, param.methodName.lastIndexOf("."));

        Class   clazz    = getClass(className);
        Method  method   = getMethod(methodName, clazz, param.argsTypes);

        if (method == null)
            throw cachedException;

        //security check
        if (!method.isAnnotationPresent(ServiceMethod.class)) {
            throw new SecurityException("Method is not annotated with ServiceMethod!");
        }

        //use another invoke type if method is static
        if (Modifier.isStatic(method.getModifiers())) {
            return method.invoke(clazz.getClass(), param.params.toArray());
        } else {
            return method.invoke(clazz.newInstance(), param.params.toArray());  //todo create method to get class instance?
        }
    }

    private static Method getMethod(String methodName, Class owner, Class [] params) throws NoSuchMethodException, ClassNotFoundException {
        InvokeCache cache = InvokeCache.getInstance();
        Method result;

        if ((result = cache.findMethod(methodName)) != null) {          //try to find method in cache
            return result;
        } else {
            result = owner.getMethod(methodName, params);

            return result;
        }
    }

    private static Class getClass(String className) throws ClassNotFoundException {
        InvokeCache cache = InvokeCache.getInstance();
        Class result;

        if ((result = cache.findClass(className)) != null) {
            return result;
        } else {
            result = Class.forName(className);
            cache.cacheClass(result);

            return result;
        }
    }
}
