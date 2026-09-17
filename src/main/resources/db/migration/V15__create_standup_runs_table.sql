-- Table: public.standup_runs

-- DROP TABLE IF EXISTS public.standup_runs;

CREATE TABLE IF NOT EXISTS public.standup_runs
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    standup_id uuid NOT NULL,
    run_date date NOT NULL,
    ai_summary text COLLATE pg_catalog."default",
    is_finalized boolean NOT NULL DEFAULT false,
    finalized_at timestamp without time zone,
    CONSTRAINT pk_standup_runs PRIMARY KEY (id),
    CONSTRAINT uk8inqnh6gwcxspwhg6ud8hro9n UNIQUE (standup_id, run_date),
    CONSTRAINT uq_standup_runs_standup_date UNIQUE (standup_id, run_date),
    CONSTRAINT fk_standup_runs_standup FOREIGN KEY (standup_id)
    REFERENCES public.standups (id) MATCH SIMPLE
                           ON UPDATE NO ACTION
                           ON DELETE CASCADE,
    CONSTRAINT chk_standup_runs_finalized CHECK (is_finalized = false OR finalized_at IS NOT NULL)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.standup_runs
    OWNER to postgres;
-- Index: idx_standup_runs_run_date

-- DROP INDEX IF EXISTS public.idx_standup_runs_run_date;

CREATE INDEX IF NOT EXISTS idx_standup_runs_run_date
    ON public.standup_runs USING btree
    (run_date ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_standup_runs_standup_id

-- DROP INDEX IF EXISTS public.idx_standup_runs_standup_id;

CREATE INDEX IF NOT EXISTS idx_standup_runs_standup_id
    ON public.standup_runs USING btree
    (standup_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;