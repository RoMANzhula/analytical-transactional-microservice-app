package org.romanzhula.notification_service.components;

import lombok.RequiredArgsConstructor;
import org.romanzhula.notification_service.dto.events.NotificationRequestDTO;
import org.romanzhula.notification_service.models.Notification;
import org.romanzhula.notification_service.repositories.NotificationRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationRepository notificationRepository;


    @RabbitListener(
            queues = "${spring.rabbitmq.queue.notification}",
            errorHandler = "rabbitErrorHandler"
    )
    public void handleNotification(NotificationRequestDTO notificationRequestDTO) {
        Notification notification = Notification.builder()
                .userId(notificationRequestDTO.getUserId())
                .message(notificationRequestDTO.getMessage())
                .build()
        ;

        notificationRepository.save(notification);
    }

}
