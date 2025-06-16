package org.romanzhula.notification_service.controllers;


import lombok.RequiredArgsConstructor;
import org.romanzhula.notification_service.dto.NotificationResponse;
import org.romanzhula.notification_service.services.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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

}
