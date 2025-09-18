package org.tommap.tomuserloginrestapis.exception;

import java.io.Serial;

public class ResourceAlreadyExistedException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -1801501679945040733L;

    public ResourceAlreadyExistedException(String message) {
        super(message);
    }
}
