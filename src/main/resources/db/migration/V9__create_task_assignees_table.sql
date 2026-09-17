-- Table: public.task_assignees

-- DROP TABLE IF EXISTS public.task_assignees;

CREATE TABLE IF NOT EXISTS public.task_assignees
(
    task_id uuid NOT NULL,
    user_id uuid NOT NULL,
    assigned_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_task_assignees PRIMARY KEY (task_id, user_id),
    CONSTRAINT fk_task_assignees_task FOREIGN KEY (task_id)
    REFERENCES public.tasks (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE,
    CONSTRAINT fk_task_assignees_user FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.task_assignees
    OWNER to postgres;