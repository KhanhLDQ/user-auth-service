package org.tommap.tomuserloginrestapis.shared;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailUtils {
    public String generateEmailVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
