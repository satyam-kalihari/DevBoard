-- Table: public.task_comments

-- DROP TABLE IF EXISTS public.task_comments;

CREATE TABLE IF NOT EXISTS public.task_comments
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    task_id uuid NOT NULL,
    user_id uuid NOT NULL,
    body text COLLATE pg_catalog."default" NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    updated_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_task_comments PRIMARY KEY (id),
    CONSTRAINT fk_task_comments_task FOREIGN KEY (task_id)
        REFERENCES public.tasks (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_task_comments_user FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.task_comments
    OWNER to postgres;

-- Trigger: set_updated_at

-- DROP TRIGGER IF EXISTS set_updated_at ON public.task_comments;

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE UPDATE
    ON public.task_comments
    FOR EACH ROW
EXECUTE FUNCTION public.trigger_set_updated_at();