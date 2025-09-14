package org.tommap.tomuserloginrestapis.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginResponse {
    private String jwt;
    private String username;
    private String userId;
}
