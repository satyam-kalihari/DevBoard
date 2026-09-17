-- Table: public.task_activity_log

-- DROP TABLE IF EXISTS public.task_activity_log;

CREATE TABLE IF NOT EXISTS public.task_activity_log
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    task_id uuid NOT NULL,
    actor_id uuid NOT NULL,
    action_type character varying(50) COLLATE pg_catalog."default" NOT NULL,
    before_state jsonb,
    after_state jsonb,
    occurred_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_task_activity_log PRIMARY KEY (id),
    CONSTRAINT fk_task_activity_log_actor FOREIGN KEY (actor_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_task_activity_log_task FOREIGN KEY (task_id)
        REFERENCES public.tasks (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.task_activity_log
    OWNER to postgres;
-- Index: idx_task_activity_log_actor_id

-- DROP INDEX IF EXISTS public.idx_task_activity_log_actor_id;

CREATE INDEX IF NOT EXISTS idx_task_activity_log_actor_id
    ON public.task_activity_log USING btree
        (actor_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_task_activity_log_task_id

-- DROP INDEX IF EXISTS public.idx_task_activity_log_task_id;

CREATE INDEX IF NOT EXISTS idx_task_activity_log_task_id
    ON public.task_activity_log USING btree
        (task_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;

-- Trigger: lock_task_activity_log

-- DROP TRIGGER IF EXISTS lock_task_activity_log ON public.task_activity_log;

CREATE OR REPLACE TRIGGER lock_task_activity_log
    BEFORE UPDATE
    ON public.task_activity_log
    FOR EACH ROW
EXECUTE FUNCTION public.prevent_update_on_activity_log();