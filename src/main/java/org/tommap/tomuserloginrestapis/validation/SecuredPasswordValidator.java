package org.tommap.tomuserloginrestapis.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SecuredPasswordValidator implements ConstraintValidator<SecuredPassword, String> {
    private final String UPPER_CASE_PATTERN = ".*[A-Z].*";
    private final String LOWER_CASE_PATTERN = ".*[a-z].*";
    private final String DIGITS_PATTERN = ".*[0-9].*";
    private final String SPECIAL_CHARS_PATTERN = ".*[@%&].*";

    private int minLength;
    private boolean requireUppercase;
    private boolean requireLowercase;
    private boolean requireDigits;
    private boolean requireSpecialChars;

    @Override
    public void initialize(SecuredPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.requireUppercase = constraintAnnotation.requireUpperCase();
        this.requireLowercase = constraintAnnotation.requireLowerCase();
        this.requireDigits = constraintAnnotation.requireDigits();
        this.requireSpecialChars = constraintAnnotation.requireSpecialChars();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value || value.trim().isEmpty()) {
            return false;
        }

        if (value.length() < minLength) {
            return false;
        }

        if (requireUppercase && !value.matches(UPPER_CASE_PATTERN)) {
            return false;
        }

        if (requireLowercase && !value.matches(LOWER_CASE_PATTERN)) {
            return false;
        }

        if (requireDigits && !value.matches(DIGITS_PATTERN)) {
            return false;
        }

        return !requireSpecialChars || value.matches(SPECIAL_CHARS_PATTERN);
    }
}
