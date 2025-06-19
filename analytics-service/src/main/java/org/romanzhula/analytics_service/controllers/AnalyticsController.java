package org.romanzhula.analytics_service.controllers;

import lombok.RequiredArgsConstructor;
import org.romanzhula.analytics_service.dto.AnalyticsResponseDto;
import org.romanzhula.analytics_service.services.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
