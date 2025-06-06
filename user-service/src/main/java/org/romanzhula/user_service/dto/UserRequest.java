package org.romanzhula.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank(message = "Name must not be blank!")
    private String name;

    @NotBlank(message = "Email must not be blank!")
    @Email(message = "Invalid email format")
    private String email;

}
