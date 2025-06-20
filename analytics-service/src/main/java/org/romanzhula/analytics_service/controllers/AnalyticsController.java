package org.romanzhula.analytics_service.controllers;

import lombok.RequiredArgsConstructor;
import org.romanzhula.analytics_service.dto.AnalyticsResponseDto;
import org.romanzhula.analytics_service.services.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;


    @GetMapping("/all")
    public ResponseEntity<List<AnalyticsResponseDto>> getAllAnalyticsRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(analyticsService.getAllAnalyticsRecords(page, size));
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<List<AnalyticsResponseDto>> getAllAnalyticsRecordsByUserId(
            @PathVariable(name = "user-id") String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(analyticsService.getAllAnalyticsRecordsByUserId(userId, page, size));
    }

}
