package org.tommap.tomuserloginrestapis.model.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    @Email(message = "email should be in valid format!")
    private String email;
}
