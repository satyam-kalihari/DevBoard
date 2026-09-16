CREATE TABLE standup_runs (
                              id              UUID        NOT NULL DEFAULT gen_random_uuid(),
                              standup_id      UUID        NOT NULL,
                              run_date        DATE        NOT NULL,
                              ai_summary      TEXT,
                              is_finalized    BOOLEAN     NOT NULL DEFAULT FALSE,
                              finalized_at    TIMESTAMP,

                              CONSTRAINT pk_standup_runs                  PRIMARY KEY (id),
                              CONSTRAINT fk_standup_runs_standup          FOREIGN KEY (standup_id)
                                  REFERENCES standups (id) ON DELETE CASCADE,
                              CONSTRAINT uq_standup_runs_standup_date     UNIQUE (standup_id, run_date),
                              CONSTRAINT chk_standup_runs_finalized       CHECK (
                                  is_finalized = FALSE
                                      OR finalized_at IS NOT NULL
                                  )
);

CREATE INDEX idx_standup_runs_standup_id ON standup_runs (standup_id);
CREATE INDEX idx_standup_runs_run_date   ON standup_runs (run_date);