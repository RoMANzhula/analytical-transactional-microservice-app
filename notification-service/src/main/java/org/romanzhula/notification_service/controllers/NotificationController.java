package org.romanzhula.notification_service.controllers;


import lombok.RequiredArgsConstructor;
import org.romanzhula.notification_service.dto.NotificationResponse;
import org.romanzhula.notification_service.services.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping("/all")
    public ResponseEntity<Page<NotificationResponse>> getAllNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<NotificationResponse> notificationsPage = notificationService.getAllNotifications(page, size);

        return ResponseEntity.ok(notificationsPage);
    }

    @GetMapping("/user/{user-id}")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsByUserId(
            @PathVariable(name = "user-id") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<NotificationResponse> notificationsPage = notificationService.getNotificationsByUserId(userId, page, size);

        return ResponseEntity.ok(notificationsPage);
    }

}
