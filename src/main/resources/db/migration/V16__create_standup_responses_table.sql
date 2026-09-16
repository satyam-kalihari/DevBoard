CREATE TABLE standup_responses (
                                   id                  UUID        NOT NULL DEFAULT gen_random_uuid(),
                                   standup_run_id      UUID        NOT NULL,
                                   user_id             UUID        NOT NULL,
                                   answer_yesterday    TEXT,
                                   answer_today        TEXT,
                                   answer_blockers     TEXT,
                                   has_blockers        BOOLEAN     NOT NULL DEFAULT FALSE,
                                   submitted_at        TIMESTAMP   NOT NULL DEFAULT NOW(),

                                   CONSTRAINT pk_standup_responses             PRIMARY KEY (id),
                                   CONSTRAINT fk_standup_responses_run         FOREIGN KEY (standup_run_id)
                                       REFERENCES standup_runs (id) ON DELETE CASCADE,
                                   CONSTRAINT fk_standup_responses_user        FOREIGN KEY (user_id)
                                       REFERENCES users (id) ON DELETE CASCADE,
                                   CONSTRAINT uq_standup_responses_run_user    UNIQUE (standup_run_id, user_id)
);

CREATE INDEX idx_standup_responses_run_id   ON standup_responses (standup_run_id);
CREATE INDEX idx_standup_responses_user_id  ON standup_responses (user_id);
CREATE INDEX idx_standup_responses_blockers ON standup_responses (has_blockers)
    WHERE has_blockers = TRUE;