CREATE TABLE sprints (
                         id          UUID            NOT NULL DEFAULT gen_random_uuid(),
                         project_id  UUID            NOT NULL,
                         name        VARCHAR(255)    NOT NULL,
                         goal        TEXT,
                         start_date  DATE            NOT NULL,
                         end_date    DATE            NOT NULL,
                         status      VARCHAR(20)     NOT NULL DEFAULT 'PLANNED',
                         created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),

                         CONSTRAINT pk_sprints                   PRIMARY KEY (id),
                         CONSTRAINT fk_sprints_project           FOREIGN KEY (project_id)
                             REFERENCES projects (id) ON DELETE CASCADE,
                         CONSTRAINT uq_sprints_name_per_project  UNIQUE (project_id, name),
                         CONSTRAINT chk_sprints_status           CHECK (status IN (
                                                                                   'PLANNED','ACTIVE','COMPLETED','CANCELLED'
                             )),
                         CONSTRAINT chk_sprints_dates            CHECK (start_date <= end_date)
);

CREATE INDEX idx_sprints_project_id ON sprints (project_id);
CREATE INDEX idx_sprints_status     ON sprints (status);