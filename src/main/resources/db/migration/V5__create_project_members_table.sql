CREATE TABLE project_members (
                                 id          UUID        NOT NULL DEFAULT gen_random_uuid(),
                                 project_id  UUID        NOT NULL,
                                 user_id     UUID        NOT NULL,
                                 joined_at   TIMESTAMP   NOT NULL DEFAULT NOW(),

                                 CONSTRAINT pk_project_members               PRIMARY KEY (id),
                                 CONSTRAINT uq_project_members_project_user  UNIQUE (project_id, user_id),
                                 CONSTRAINT fk_project_members_project       FOREIGN KEY (project_id)
                                     REFERENCES projects (id) ON DELETE CASCADE,
                                 CONSTRAINT fk_project_members_user          FOREIGN KEY (user_id)
                                     REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_project_members_project_id ON project_members (project_id);
CREATE INDEX idx_project_members_user_id    ON project_members (user_id);