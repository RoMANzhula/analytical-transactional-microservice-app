package org.romanzhula.analytics_service.repositories;

import lombok.RequiredArgsConstructor;
import org.romanzhula.analytics_service.dto.AnalyticsResponseDto;
import org.romanzhula.analytics_service.models.AnalyticsRecord;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class AnalyticsRepository {

    private final DataSource dataSource; // for INSERT
    private final JdbcTemplate jdbcTemplate; // for SELECT


    public void save(AnalyticsRecord record) {
        String sql = "INSERT INTO analytics_records (user_id, event_type, event_time) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, record.getUserId());
            statement.setString(2, record.getEventType());
            statement.setObject(3, record.getEventTime());

            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException("Failed to insert record into ClickHouse", exception);
        }
    }

    public List<AnalyticsResponseDto> findAll(int page, int size) {
        int offset = page * size;
        String sql = """
                SELECT user_id, event_type, event_time
                FROM analytics_records
                ORDER BY event_time DESC
                LIMIT ? OFFSET ?
                """
        ;

        return jdbcTemplate.query(
                sql,
                new Object[]{size, offset},
                (rs, rowNum) -> AnalyticsResponseDto.builder()
                        .id(rs.getString("id"))
                        .userId(rs.getString("user_id"))
                        .eventType(rs.getString("event_type"))
                        .eventTime(rs.getTimestamp("event_time").toLocalDateTime())
                        .build()
                )
        ;
    }

    public List<AnalyticsResponseDto> findAllByUserId(String userId, int page, int size) {
        int offset = page * size;
        String sql = """
                SELECT id, user_id, event_type, event_time
                FROM analytics_records
                WHERE user_id = ?
                ORDER BY event_time DESC
                LIMIT ? OFFSET ?
                """
        ;

        return jdbcTemplate.query(
                sql,
                new Object[]{userId, size, offset},
                (rs, rowNum) -> AnalyticsResponseDto.builder()
                        .id(rs.getString("id"))
                        .userId(rs.getString("user_id"))
                        .eventType(rs.getString("event_type"))
                        .eventTime(rs.getTimestamp("event_time").toLocalDateTime())
                        .build()
                )
        ;
    }

}
