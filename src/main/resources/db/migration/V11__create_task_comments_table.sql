CREATE TABLE task_comments (
                               id          UUID        NOT NULL DEFAULT gen_random_uuid(),
                               task_id     UUID        NOT NULL,
                               user_id     UUID        NOT NULL,
                               body        TEXT        NOT NULL,
                               created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
                               updated_at  TIMESTAMP   NOT NULL DEFAULT NOW(),

                               CONSTRAINT pk_task_comments         PRIMARY KEY (id),
                               CONSTRAINT fk_task_comments_task    FOREIGN KEY (task_id)
                                   REFERENCES tasks (id) ON DELETE CASCADE,
                               CONSTRAINT fk_task_comments_user    FOREIGN KEY (user_id)
                                   REFERENCES users (id) ON DELETE CASCADE
);

CREATE TRIGGER set_updated_at
    BEFORE UPDATE ON task_comments
    FOR EACH ROW EXECUTE FUNCTION trigger_set_updated_at();