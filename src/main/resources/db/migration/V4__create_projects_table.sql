-- Table: public.projects

-- DROP TABLE IF EXISTS public.projects;

CREATE TABLE IF NOT EXISTS public.projects
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    org_id uuid NOT NULL,
    lead_user_id uuid,
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    description character varying(255) COLLATE pg_catalog."default",
    status character varying(255) COLLATE pg_catalog."default" NOT NULL DEFAULT 'PENDING'::character varying,
    start_date date,
    target_date date,
    end_date date,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    updated_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_projects PRIMARY KEY (id),
    CONSTRAINT uq_projects_name_per_org UNIQUE (org_id, name),
    CONSTRAINT fk_projects_lead FOREIGN KEY (lead_user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE SET NULL,
    CONSTRAINT fk_projects_org FOREIGN KEY (org_id)
        REFERENCES public.organizations (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT chk_projects_status CHECK (status::text = ANY (ARRAY['PENDING'::character varying::text, 'ACTIVE'::character varying::text, 'ON_HOLD'::character varying::text, 'COMPLETED'::character varying::text, 'CANCELLED'::character varying::text])),
    CONSTRAINT chk_projects_end_date CHECK (end_date IS NULL OR start_date IS NULL OR end_date >= start_date),
    CONSTRAINT chk_projects_dates CHECK (start_date IS NULL OR target_date IS NULL OR start_date <= target_date)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.projects
    OWNER to postgres;
-- Index: idx_projects_lead_user_id

-- DROP INDEX IF EXISTS public.idx_projects_lead_user_id;

CREATE INDEX IF NOT EXISTS idx_projects_lead_user_id
    ON public.projects USING btree
        (lead_user_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_projects_org_id

-- DROP INDEX IF EXISTS public.idx_projects_org_id;

CREATE INDEX IF NOT EXISTS idx_projects_org_id
    ON public.projects USING btree
        (org_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;
-- Index: idx_projects_status

-- DROP INDEX IF EXISTS public.idx_projects_status;

CREATE INDEX IF NOT EXISTS idx_projects_status
    ON public.projects USING btree
        (status COLLATE pg_catalog."default" ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;

-- Trigger: set_updated_at

-- DROP TRIGGER IF EXISTS set_updated_at ON public.projects;

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE UPDATE
    ON public.projects
    FOR EACH ROW
EXECUTE FUNCTION public.trigger_set_updated_at();