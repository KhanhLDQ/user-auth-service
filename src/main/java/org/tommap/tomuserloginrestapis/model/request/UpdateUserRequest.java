package org.tommap.tomuserloginrestapis.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank(message = "first name should not be blank!")
    @Size(max = 10, message = "first name should not be more than 10 characters!")
    private String firstName;

    @NotBlank(message = "last name should not be blank!")
    @Size(max = 10, message = "last name should not be more than 10 characters!")
    private String lastName;
}
