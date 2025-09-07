package org.tommap.tomuserloginrestapis.shared;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserUtils {
    public String generateUserId() {
        return UUID.randomUUID().toString();
    }
}
