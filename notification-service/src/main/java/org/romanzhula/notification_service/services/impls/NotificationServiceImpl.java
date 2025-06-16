package org.romanzhula.notification_service.services.impls;

import lombok.RequiredArgsConstructor;
import org.romanzhula.notification_service.dto.NotificationResponse;
import org.romanzhula.notification_service.models.Notification;
import org.romanzhula.notification_service.repositories.NotificationRepository;
import org.romanzhula.notification_service.services.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getAllNotifications(int page, int size) {
        Page<Notification> notificationsPage = notificationRepository.findAll(PageRequest.of(page, size));

        List<NotificationResponse> content = notificationsPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList())
        ;

        return new PageImpl<>(content, notificationsPage.getPageable(), notificationsPage.getTotalElements());

    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsByUserId(String userId, int page, int size) {
        Page<Notification> notificationsPage = notificationRepository.findByUserId(userId, PageRequest.of(page, size));

        List<NotificationResponse> content = notificationsPage.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList())
        ;

        return new PageImpl<>(content, notificationsPage.getPageable(), notificationsPage.getTotalElements());
    }
    

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .build()
        ;
    }

}
