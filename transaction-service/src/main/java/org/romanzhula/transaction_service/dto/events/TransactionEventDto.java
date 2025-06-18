package org.romanzhula.transaction_service.dto.events;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEventDto {

    @NotNull
    private String userId;

    @NotNull
    private String eventType;

    @NotNull
    private LocalDateTime eventTime;

}
