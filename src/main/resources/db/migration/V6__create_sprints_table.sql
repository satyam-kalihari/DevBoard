-- Table: public.sprints

-- DROP TABLE IF EXISTS public.sprints;

CREATE TABLE IF NOT EXISTS public.sprints
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    project_id uuid NOT NULL,
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    goal text COLLATE pg_catalog."default",
    start_date date NOT NULL,
    end_date date NOT NULL,
    status character varying(255) COLLATE pg_catalog."default" NOT NULL DEFAULT 'PLANNED'::character varying,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_sprints PRIMARY KEY (id),
    CONSTRAINT ukb6isiavxhrayjppin9pkypu5x UNIQUE (project_id, name),
    CONSTRAINT uq_sprints_name_per_project UNIQUE (project_id, name),
    CONSTRAINT fk_sprints_project FOREIGN KEY (project_id)
    REFERENCES public.projects (id) MATCH SIMPLE
                         ON UPDATE NO ACTION
                         ON DELETE CASCADE,
    CONSTRAINT chk_sprints_status CHECK (status::text = ANY (ARRAY['PLANNED'::character varying::text, 'ACTIVE'::character varying::text, 'COMPLETED'::character varying::text, 'CANCELLED'::character varying::text])),
    CONSTRAINT chk_sprints_dates CHECK (start_date <= end_date)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.sprints
    OWNER to postgres;
-- Index: idx_sprints_project_id

-- DROP INDEX IF EXISTS public.idx_sprints_project_id;

CREATE INDEX IF NOT EXISTS idx_sprints_project_id
    ON public.sprints USING btree
    (project_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_sprints_status

-- DROP INDEX IF EXISTS public.idx_sprints_status;

CREATE INDEX IF NOT EXISTS idx_sprints_status
    ON public.sprints USING btree
    (status COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;