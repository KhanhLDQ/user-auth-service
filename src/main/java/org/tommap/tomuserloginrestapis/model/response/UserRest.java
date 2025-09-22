package org.tommap.tomuserloginrestapis.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class UserRest {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<AddressRest> addresses;

    @Getter @Setter
    @NoArgsConstructor
    public static class AddressRest {
        private Long id;
        private String city;
        private String country;
        private String street;
        private String postalCode;
        private String type;
    }
}
