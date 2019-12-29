package org.mifos.ussd.common.exception;

public class SessionNotFoundException extends RuntimeException {
    public SessionNotFoundException(String s) {
        super(s);
    }
}
