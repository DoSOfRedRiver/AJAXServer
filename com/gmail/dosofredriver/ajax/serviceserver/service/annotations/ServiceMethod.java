package com.gmail.dosofredriver.ajax.serviceserver.service.annotations;

/**
 * This annotation is used as marker for all methods that should be invoked by
 * <code>Service</code> class.
 *
 * @see com.gmail.dosofredriver.ajax.serviceserver.service.Service;
 */

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceMethod {
}
