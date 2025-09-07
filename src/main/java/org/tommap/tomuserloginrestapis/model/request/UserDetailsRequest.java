package org.tommap.tomuserloginrestapis.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
