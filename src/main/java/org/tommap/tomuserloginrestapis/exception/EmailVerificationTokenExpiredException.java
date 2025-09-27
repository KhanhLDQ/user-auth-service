package org.tommap.tomuserloginrestapis.exception;

import java.io.Serial;

public class EmailVerificationTokenExpiredException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1729281727299592808L;

    public EmailVerificationTokenExpiredException(String message) {
        super(message);
    }
}
