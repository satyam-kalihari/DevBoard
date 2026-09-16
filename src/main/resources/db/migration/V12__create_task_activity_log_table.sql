CREATE TABLE task_activity_log (
                                   id              UUID        NOT NULL DEFAULT gen_random_uuid(),
                                   task_id         UUID        NOT NULL,
                                   actor_id        UUID        NOT NULL,
                                   action_type     VARCHAR(50) NOT NULL,
                                   before_state    JSONB,
                                   after_state     JSONB,
                                   occurred_at     TIMESTAMP   NOT NULL DEFAULT NOW(),

                                   CONSTRAINT pk_task_activity_log         PRIMARY KEY (id),
                                   CONSTRAINT fk_task_activity_log_task    FOREIGN KEY (task_id)
                                       REFERENCES tasks (id) ON DELETE CASCADE,
                                   CONSTRAINT fk_task_activity_log_actor   FOREIGN KEY (actor_id)
                                       REFERENCES users (id) ON DELETE CASCADE
);

CREATE OR REPLACE FUNCTION prevent_update_on_activity_log()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'Updates not allowed on task_activity_log — append only';
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER lock_task_activity_log
    BEFORE UPDATE ON task_activity_log
    FOR EACH ROW EXECUTE FUNCTION prevent_update_on_activity_log();

CREATE INDEX idx_task_activity_log_task_id  ON task_activity_log (task_id);
CREATE INDEX idx_task_activity_log_actor_id ON task_activity_log (actor_id);