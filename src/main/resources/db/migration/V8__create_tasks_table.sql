-- Table: public.tasks

-- DROP TABLE IF EXISTS public.tasks;

CREATE TABLE IF NOT EXISTS public.tasks
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    project_id uuid NOT NULL,
    sprint_id uuid,
    parent_task_id uuid,
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default" NOT NULL DEFAULT 'BACKLOG'::character varying,
    priority character varying(255) COLLATE pg_catalog."default" NOT NULL DEFAULT 'NONE'::character varying,
    story_points integer,
    rank integer NOT NULL DEFAULT 0,
    due_date date,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    updated_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_tasks PRIMARY KEY (id),
    CONSTRAINT fk_tasks_parent FOREIGN KEY (parent_task_id)
        REFERENCES public.tasks (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE SET NULL,
    CONSTRAINT fk_tasks_project FOREIGN KEY (project_id)
        REFERENCES public.projects (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_tasks_sprint FOREIGN KEY (sprint_id)
        REFERENCES public.sprints (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE SET NULL,
    CONSTRAINT chk_tasks_rank CHECK (rank >= 0),
    CONSTRAINT chk_tasks_no_self_ref CHECK (id <> parent_task_id),
    CONSTRAINT chk_tasks_priority CHECK (priority::text = ANY (ARRAY['URGENT'::character varying::text, 'HIGH'::character varying::text, 'MEDIUM'::character varying::text, 'LOW'::character varying::text, 'NONE'::character varying::text])),
    CONSTRAINT chk_tasks_status CHECK (status::text = ANY (ARRAY['BACKLOG'::character varying::text, 'TODO'::character varying::text, 'IN_PROGRESS'::character varying::text, 'IN_REVIEW'::character varying::text, 'DONE'::character varying::text, 'CANCELLED'::character varying::text])),
    CONSTRAINT chk_tasks_story_points CHECK (story_points >= 0)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.tasks
    OWNER to postgres;
-- Index: idx_tasks_parent

-- DROP INDEX IF EXISTS public.idx_tasks_parent;

CREATE INDEX IF NOT EXISTS idx_tasks_parent
    ON public.tasks USING btree
        (parent_task_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_tasks_priority

-- DROP INDEX IF EXISTS public.idx_tasks_priority;

CREATE INDEX IF NOT EXISTS idx_tasks_priority
    ON public.tasks USING btree
        (priority COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_tasks_project_id

-- DROP INDEX IF EXISTS public.idx_tasks_project_id;

CREATE INDEX IF NOT EXISTS idx_tasks_project_id
    ON public.tasks USING btree
        (project_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_tasks_rank

-- DROP INDEX IF EXISTS public.idx_tasks_rank;

CREATE INDEX IF NOT EXISTS idx_tasks_rank
    ON public.tasks USING btree
        (project_id ASC NULLS LAST, sprint_id ASC NULLS LAST, rank ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_tasks_sprint_id

-- DROP INDEX IF EXISTS public.idx_tasks_sprint_id;

CREATE INDEX IF NOT EXISTS idx_tasks_sprint_id
    ON public.tasks USING btree
        (sprint_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_tasks_status

-- DROP INDEX IF EXISTS public.idx_tasks_status;

CREATE INDEX IF NOT EXISTS idx_tasks_status
    ON public.tasks USING btree
        (status COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;

-- Trigger: set_updated_at

-- DROP TRIGGER IF EXISTS set_updated_at ON public.tasks;

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE UPDATE
    ON public.tasks
    FOR EACH ROW
EXECUTE FUNCTION public.trigger_set_updated_at();