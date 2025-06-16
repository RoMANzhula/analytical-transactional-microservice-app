package org.romanzhula.notification_service.services;

import org.romanzhula.notification_service.dto.NotificationResponse;
import org.springframework.data.domain.Page;

public interface NotificationService {

    Page<NotificationResponse> getAllNotifications(int page, int size);

    Page<NotificationResponse> getNotificationsByUserId(String userId, int page, int size);

}
