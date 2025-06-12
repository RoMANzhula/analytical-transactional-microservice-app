package org.romanzhula.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetPassphraseRequest {

    private String googleId;
    private String githubId;

    @NotBlank
    private String passphrase;

}
