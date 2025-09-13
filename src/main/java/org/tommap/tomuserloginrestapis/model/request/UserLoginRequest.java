package org.tommap.tomuserloginrestapis.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class UserLoginRequest {
    @Email(message = "user name should be in email format!")
    private String username;

    @NotNull(message = "password should not be null!")
    private String password;
}
