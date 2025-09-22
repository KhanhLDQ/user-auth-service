package org.tommap.tomuserloginrestapis.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AddressType {
    BILLING("BILLING"),
    SHIPPING("SHIPPING");

    private final String value;

    public static AddressType fromValue(String value) {
        if (null == value || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Address type cannot be null or empty");
        }

        return Arrays.stream(values())
                .filter(type -> type.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid address type: %s", value)));
    }
}
