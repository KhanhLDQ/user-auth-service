package org.tommap.tomuserloginrestapis.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tommap.tomuserloginrestapis.validation.PasswordMatch;
import org.tommap.tomuserloginrestapis.validation.SecuredPassword;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatch(newPassword = "newPassword", confirmedPassword = "confirmedPassword")
public class ResetPassword {
    @NotNull(message = "token should be required!")
    private String token;

    @SecuredPassword(message = "password should be in correct format!")
    private String newPassword;

    @NotNull
    private String confirmedPassword;
}
