CREATE TABLE tasks (
                       id              UUID            NOT NULL DEFAULT gen_random_uuid(),
                       project_id      UUID            NOT NULL,
                       sprint_id       UUID,
                       parent_task_id  UUID,
                       title           VARCHAR(500)    NOT NULL,
                       description     TEXT,
                       status          VARCHAR(20)     NOT NULL DEFAULT 'BACKLOG',
                       priority        VARCHAR(20)     NOT NULL DEFAULT 'NONE',
                       story_points    SMALLINT,
                       rank            INTEGER         NOT NULL DEFAULT 0,
                       due_date        DATE,
                       created_at      TIMESTAMP       NOT NULL DEFAULT NOW(),
                       updated_at      TIMESTAMP       NOT NULL DEFAULT NOW(),

                       CONSTRAINT pk_tasks                 PRIMARY KEY (id),
                       CONSTRAINT fk_tasks_project         FOREIGN KEY (project_id)
                           REFERENCES projects (id) ON DELETE CASCADE,
                       CONSTRAINT fk_tasks_sprint          FOREIGN KEY (sprint_id)
                           REFERENCES sprints (id) ON DELETE SET NULL,
                       CONSTRAINT fk_tasks_parent          FOREIGN KEY (parent_task_id)
                           REFERENCES tasks (id) ON DELETE SET NULL,
                       CONSTRAINT chk_tasks_status         CHECK (status IN (
                                                                             'BACKLOG','TODO','IN_PROGRESS','IN_REVIEW','DONE','CANCELLED'
                           )),
                       CONSTRAINT chk_tasks_priority       CHECK (priority IN (
                                                                               'URGENT','HIGH','MEDIUM','LOW','NONE'
                           )),
                       CONSTRAINT chk_tasks_story_points   CHECK (story_points >= 0),
                       CONSTRAINT chk_tasks_rank           CHECK (rank >= 0),
                       CONSTRAINT chk_tasks_no_self_ref    CHECK (id != parent_task_id)
    );

CREATE TRIGGER set_updated_at
    BEFORE UPDATE ON tasks
    FOR EACH ROW EXECUTE FUNCTION trigger_set_updated_at();

CREATE INDEX idx_tasks_project_id   ON tasks (project_id);
CREATE INDEX idx_tasks_sprint_id    ON tasks (sprint_id);
CREATE INDEX idx_tasks_status       ON tasks (status);
CREATE INDEX idx_tasks_priority     ON tasks (priority);
CREATE INDEX idx_tasks_rank         ON tasks (project_id, sprint_id, rank);