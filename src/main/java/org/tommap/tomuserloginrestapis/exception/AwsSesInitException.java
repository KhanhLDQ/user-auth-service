package org.tommap.tomuserloginrestapis.exception;

import java.io.Serial;

public class AwsSesInitException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 7501904007414799763L;

    public AwsSesInitException(String message) {
        super(message);
    }
}
