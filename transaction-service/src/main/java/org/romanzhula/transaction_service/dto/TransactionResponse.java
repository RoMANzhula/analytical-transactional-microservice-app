package org.romanzhula.transaction_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private Long id;
    private String userId; // from user-service
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;

    // optional (for testing)
    private String googleId;
    private String githubId;

}
