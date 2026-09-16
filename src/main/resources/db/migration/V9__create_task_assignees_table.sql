CREATE TABLE task_assignees (
                                task_id     UUID    NOT NULL,
                                user_id     UUID    NOT NULL,
                                assigned_at TIMESTAMP NOT NULL DEFAULT NOW(),

                                CONSTRAINT pk_task_assignees        PRIMARY KEY (task_id, user_id),
                                CONSTRAINT fk_task_assignees_task   FOREIGN KEY (task_id)
                                    REFERENCES tasks (id) ON DELETE CASCADE,
                                CONSTRAINT fk_task_assignees_user   FOREIGN KEY (user_id)
                                    REFERENCES users (id) ON DELETE CASCADE
);