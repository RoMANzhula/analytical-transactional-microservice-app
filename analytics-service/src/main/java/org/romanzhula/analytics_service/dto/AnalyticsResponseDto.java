package org.romanzhula.analytics_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponseDto {

    private String id;
    private String userId;
    private String eventType;
    private LocalDateTime eventTime;

}
