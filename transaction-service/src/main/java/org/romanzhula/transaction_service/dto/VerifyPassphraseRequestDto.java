package org.romanzhula.transaction_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyPassphraseRequestDto {

    @NotNull
    private Long userId;

    @NotBlank
    private String passphrase;

}
