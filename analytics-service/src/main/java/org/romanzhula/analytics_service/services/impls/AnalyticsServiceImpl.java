package org.romanzhula.analytics_service.services.impls;

import lombok.RequiredArgsConstructor;
import org.romanzhula.analytics_service.dto.AnalyticsResponseDto;
import org.romanzhula.analytics_service.repositories.AnalyticsRepository;
import org.romanzhula.analytics_service.services.AnalyticsService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;


    @Override
    public List<AnalyticsResponseDto> getAllAnalyticsRecords(int page, int size) {
        return analyticsRepository.findAll(page, size);
    }

}
