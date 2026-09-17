-- Table: public.project_members

-- DROP TABLE IF EXISTS public.project_members;

CREATE TABLE IF NOT EXISTS public.project_members
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    project_id uuid NOT NULL,
    user_id uuid NOT NULL,
    joined_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_project_members PRIMARY KEY (id),
    CONSTRAINT uq_project_members_project_user UNIQUE (project_id, user_id),
    CONSTRAINT fk_project_members_project FOREIGN KEY (project_id)
    REFERENCES public.projects (id) MATCH SIMPLE
                        ON UPDATE NO ACTION
                        ON DELETE CASCADE,
    CONSTRAINT fk_project_members_user FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
                        ON UPDATE NO ACTION
                        ON DELETE CASCADE
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.project_members
    OWNER to postgres;
-- Index: idx_project_members_project_id

-- DROP INDEX IF EXISTS public.idx_project_members_project_id;

CREATE INDEX IF NOT EXISTS idx_project_members_project_id
    ON public.project_members USING btree
    (project_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_project_members_user_id

-- DROP INDEX IF EXISTS public.idx_project_members_user_id;

CREATE INDEX IF NOT EXISTS idx_project_members_user_id
    ON public.project_members USING btree
    (user_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;