package com.gmail.dosofredriver.ajax.serviceserver.service.annotations;

/**
 * This annotation is used as marker for all classes that should be used as
 * controller for <code>Service</code> class.
 *
 * @see com.gmail.dosofredriver.ajax.serviceserver.service.Service;
 */

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceClass {
}
