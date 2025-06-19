package org.romanzhula.analytics_service.repositories;

import lombok.RequiredArgsConstructor;
import org.romanzhula.analytics_service.models.AnalyticsRecord;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


@Repository
@RequiredArgsConstructor
public class AnalyticsRepository {

    private final DataSource dataSource; // for INSERT


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


}
