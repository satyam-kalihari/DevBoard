-- Table: public.task_labels

-- DROP TABLE IF EXISTS public.task_labels;

CREATE TABLE IF NOT EXISTS public.task_labels
(
    task_id uuid NOT NULL,
    label_id uuid NOT NULL,
    CONSTRAINT pk_task_labels PRIMARY KEY (task_id, label_id),
    CONSTRAINT fk_task_labels_label FOREIGN KEY (label_id)
    REFERENCES public.labels (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE,
    CONSTRAINT fk_task_labels_task FOREIGN KEY (task_id)
    REFERENCES public.tasks (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.task_labels
    OWNER to postgres;