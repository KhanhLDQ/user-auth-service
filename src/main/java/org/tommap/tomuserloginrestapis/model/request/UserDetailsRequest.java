package org.tommap.tomuserloginrestapis.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tommap.tomuserloginrestapis.validation.SecuredPassword;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsRequest {
    @NotBlank(message = "first name should not be blank!")
    @Size(max = 10, message = "first name should not be more than 10 characters!")
    private String firstName;

    @NotBlank(message = "last name should not be blank!")
    @Size(max = 10, message = "last name should not be more than 10 characters!")
    private String lastName;

    @Email(message = "email should be in correct format!")
    private String email;

    @SecuredPassword(message = "password should be in correct format!")
    private String password;
}
