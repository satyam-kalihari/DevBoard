CREATE TABLE projects (
                          id              UUID            NOT NULL DEFAULT gen_random_uuid(),
                          org_id          UUID            NOT NULL,
                          lead_user_id    UUID,
                          name            VARCHAR(255)    NOT NULL,
                          description     TEXT,
                          status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
                          start_date      DATE,
                          target_date     DATE,
                          end_date        DATE,
                          created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
                          updated_at      TIMESTAMP       NOT NULL DEFAULT NOW(),

                          CONSTRAINT pk_projects                  PRIMARY KEY (id),
                          CONSTRAINT fk_projects_org              FOREIGN KEY (org_id)
                              REFERENCES organizations (id) ON DELETE CASCADE,
                          CONSTRAINT fk_projects_lead             FOREIGN KEY (lead_user_id)
                              REFERENCES users (id) ON DELETE SET NULL,
                          CONSTRAINT uq_projects_name_per_org     UNIQUE (org_id, name),
                          CONSTRAINT chk_projects_status          CHECK (status IN (
                                                                                    'PENDING','ACTIVE','ON_HOLD','COMPLETED','CANCELLED'
                              )),
                          CONSTRAINT chk_projects_dates           CHECK (
                              start_date IS NULL OR target_date IS NULL
                                  OR start_date <= target_date
                              ),
                          CONSTRAINT chk_projects_end_date        CHECK (
                              end_date IS NULL OR start_date IS NULL
                                  OR end_date >= start_date
                              )
);

CREATE TRIGGER set_updated_at
    BEFORE UPDATE ON projects
    FOR EACH ROW EXECUTE FUNCTION trigger_set_updated_at();