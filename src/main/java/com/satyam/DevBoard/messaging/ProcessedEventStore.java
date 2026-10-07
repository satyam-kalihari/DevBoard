package com.satyam.DevBoard.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProcessedEventStore {

    private final JdbcClient jdbcClient;

    public boolean markedProcessed(String consumer, UUID eventId){
        int rows = jdbcClient.sql("""
                    INSERT INTO processed_events (consumer, event_id)
                    VALUES (:consumer, :eventId)
                    ON CONFLICT DO NOTHING
                """)
                .param("consumer", consumer)
                .param("eventId", eventId)
                .update();

        return rows == 1;
    }
}
