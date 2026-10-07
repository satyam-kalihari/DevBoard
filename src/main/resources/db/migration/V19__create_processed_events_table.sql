CREATE TABLE IF NOT EXISTS processed_events (
    consumer      VARCHAR(100) NOT NULL,
    event_id      UUID         NOT NULL,
    processed_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_processed_events PRIMARY KEY (consumer, event_id)
);