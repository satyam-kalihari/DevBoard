-- Table: public.standup_responses

-- DROP TABLE IF EXISTS public.standup_responses;

CREATE TABLE IF NOT EXISTS public.standup_responses
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    standup_run_id uuid NOT NULL,
    user_id uuid NOT NULL,
    answer_yesterday text COLLATE pg_catalog."default",
    answer_today text COLLATE pg_catalog."default",
    answer_blockers text COLLATE pg_catalog."default",
    has_blockers boolean NOT NULL DEFAULT false,
    submitted_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_standup_responses PRIMARY KEY (id),
    CONSTRAINT ukics1my2sfq5ds1t69jeywiyd7 UNIQUE (standup_run_id, user_id),
    CONSTRAINT uq_standup_responses_run_user UNIQUE (standup_run_id, user_id),
    CONSTRAINT fk_standup_responses_run FOREIGN KEY (standup_run_id)
    REFERENCES public.standup_runs (id) MATCH SIMPLE
                           ON UPDATE NO ACTION
                           ON DELETE CASCADE,
    CONSTRAINT fk_standup_responses_user FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
                           ON UPDATE NO ACTION
                           ON DELETE CASCADE
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.standup_responses
    OWNER to postgres;
-- Index: idx_standup_responses_blockers

-- DROP INDEX IF EXISTS public.idx_standup_responses_blockers;

CREATE INDEX IF NOT EXISTS idx_standup_responses_blockers
    ON public.standup_responses USING btree
    (has_blockers ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default
    WHERE has_blockers = true;
-- Index: idx_standup_responses_run_id

-- DROP INDEX IF EXISTS public.idx_standup_responses_run_id;

CREATE INDEX IF NOT EXISTS idx_standup_responses_run_id
    ON public.standup_responses USING btree
    (standup_run_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_standup_responses_user_id

-- DROP INDEX IF EXISTS public.idx_standup_responses_user_id;

CREATE INDEX IF NOT EXISTS idx_standup_responses_user_id
    ON public.standup_responses USING btree
    (user_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;