package org.romanzhula.analytics_service.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
public class AnalyticsRecord {

    private String id;
    private String userId;
    private String eventType;
    private LocalDateTime eventTime;


    public static AnalyticsRecord create(String userId, String eventType, LocalDateTime eventTime) {
        return AnalyticsRecord.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .eventType(eventType)
                .eventTime(eventTime)
                .build()
        ;
    }

}
