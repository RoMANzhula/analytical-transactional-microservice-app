package org.romanzhula.analytics_service.components;

import lombok.RequiredArgsConstructor;
import org.romanzhula.analytics_service.dto.events.TransactionEventDto;
import org.romanzhula.analytics_service.models.AnalyticsRecord;
import org.romanzhula.analytics_service.repositories.AnalyticsRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AnalyticsConsumer {

    private final AnalyticsRepository analyticsRepository;


    @RabbitListener(
            queues = "${spring.rabbitmq.queue.analytics",
            errorHandler = "rabbitErrorHandler"
    )
    public void handleAnalyticss(TransactionEventDto transactionEventDto) {
        AnalyticsRecord analyticsRecord = AnalyticsRecord.create(
                transactionEventDto.getUserId(),
                transactionEventDto.getEventType(),
                transactionEventDto.getEventTime()
        );

        analyticsRepository.save(analyticsRecord);
    }

}
