package org.tommap.tomuserloginrestapis.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

@Slf4j
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, Object> {
    private String newPasswordField;
    private String confirmedPasswordField;
    private String message;

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        this.newPasswordField = constraintAnnotation.newPassword();
        this.confirmedPasswordField = constraintAnnotation.confirmedPassword();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (null == value) {
            return true;
        }

        try {
            BeanWrapper bw = new BeanWrapperImpl(value);
            String newPassword = String.valueOf(bw.getPropertyValue(newPasswordField));
            String confirmedPassword = String.valueOf(bw.getPropertyValue(confirmedPasswordField));

            boolean isValid = newPassword.equals(confirmedPassword);

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(confirmedPasswordField)
                        .addConstraintViolation();
            }

            return isValid;
        } catch (Exception ex) {
            log.error("Error validation password match for fields {} & {} - msg: {}", newPasswordField, confirmedPasswordField, ex.getMessage());

            return false;
        }
    }
}
