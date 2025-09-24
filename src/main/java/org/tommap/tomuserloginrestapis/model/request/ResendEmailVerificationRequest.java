package org.tommap.tomuserloginrestapis.model.request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ResendEmailVerificationRequest {
    @Email(message = "email should be in valid format!")
    private String email;
}
