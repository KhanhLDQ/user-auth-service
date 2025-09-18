package org.tommap.tomuserloginrestapis.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 3594698844115474813L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
