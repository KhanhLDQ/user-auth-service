package org.tommap.tomuserloginrestapis.exception;

import java.io.Serial;

public class EmailVerificationRequiredException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -4260202316917875357L;

    public EmailVerificationRequiredException(String message) {
        super(message);
    }
}
