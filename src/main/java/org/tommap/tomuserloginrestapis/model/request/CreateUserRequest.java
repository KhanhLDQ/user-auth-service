package org.tommap.tomuserloginrestapis.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tommap.tomuserloginrestapis.validation.SecuredPassword;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
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

    private List<@Valid CreateAddressRequest> addresses;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAddressRequest {
        @NotBlank(message = "city should not be blank!")
        private String city;

        @NotBlank(message = "country should not be blank!")
        private String country;

        @NotBlank(message = "street should not be blank!")
        private String street;

        @NotBlank(message = "postal code should not be blank!")
        private String postalCode;

        @NotBlank(message = "type should not be blank!")
        private String type;
    }
}
