package org.tommap.tomuserloginrestapis.exception;

import java.io.Serial;

public class EmailSendingException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -1709971364354992682L;

    public EmailSendingException(String message) {
        super(message);
    }
}
