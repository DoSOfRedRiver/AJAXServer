package com.gmail.dosofredriver.ajax.serviceserver.service.exceptions;

/**
 * This exception указывает for controller implementation error.
 * Controller class must be annotated with <code>ServiceClass</code> annotation.
 */
public class IsNotAnnotatedException extends Throwable {
    private String message;

    public IsNotAnnotatedException() {
        message = "Given class or method is not annotated!";
    }

    public IsNotAnnotatedException(Throwable ex) {
        super(ex);
        message = "Given class or method is not annotated!";
    }

    public IsNotAnnotatedException(String message) {
        this.message = message;
    }

    public IsNotAnnotatedException(String message, Throwable ex) {
        super(ex);
        this.message = message;
    }
}
