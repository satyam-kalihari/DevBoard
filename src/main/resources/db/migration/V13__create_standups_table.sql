CREATE TABLE standups (
                          id              UUID        NOT NULL DEFAULT gen_random_uuid(),
                          project_id      UUID        NOT NULL,
                          schedule_days   VARCHAR(50) NOT NULL DEFAULT 'MON,TUE,WED,THU,FRI',
                          scheduled_time  TIME        NOT NULL DEFAULT '09:00:00',
                          is_active       BOOLEAN     NOT NULL DEFAULT TRUE,
                          created_at      TIMESTAMP   NOT NULL DEFAULT NOW(),

                          CONSTRAINT pk_standups          PRIMARY KEY (id),
                          CONSTRAINT fk_standups_project  FOREIGN KEY (project_id)
                              REFERENCES projects (id) ON DELETE CASCADE,
                          CONSTRAINT uq_standups_project  UNIQUE (project_id)
);