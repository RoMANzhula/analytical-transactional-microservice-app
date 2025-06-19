package org.romanzhula.analytics_service.services;

import org.romanzhula.analytics_service.dto.AnalyticsResponseDto;

import java.util.List;


public interface AnalyticsService {

    List<AnalyticsResponseDto> getAllAnalyticsRecords(int page, int size);

}
