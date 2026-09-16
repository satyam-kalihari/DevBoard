CREATE TABLE notifications (
                               id          UUID        NOT NULL DEFAULT gen_random_uuid(),
                               user_id     UUID        NOT NULL,
                               org_id      UUID        NOT NULL,
                               type        VARCHAR(50) NOT NULL,
                               title       VARCHAR(255) NOT NULL,
                               payload     JSONB,
                               is_read     BOOLEAN     NOT NULL DEFAULT FALSE,
                               created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),

                               CONSTRAINT pk_notifications         PRIMARY KEY (id),
                               CONSTRAINT fk_notifications_user    FOREIGN KEY (user_id)
                                   REFERENCES users (id) ON DELETE CASCADE,
                               CONSTRAINT fk_notifications_org     FOREIGN KEY (org_id)
                                   REFERENCES organizations (id) ON DELETE CASCADE,
                               CONSTRAINT chk_notifications_type   CHECK (type IN (
                                                                                   'TASK_ASSIGNED','TASK_COMMENT_MENTION',
                                                                                   'STANDUP_REMINDER','SPRINT_STARTED',
                                                                                   'SPRINT_ENDED','BLOCKER_ADDED','MEMBER_INVITED'
                                   ))
);

CREATE INDEX idx_notifications_user_id  ON notifications (user_id);
CREATE INDEX idx_notifications_is_read  ON notifications (user_id, is_read)
    WHERE is_read = FALSE;