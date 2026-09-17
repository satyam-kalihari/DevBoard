-- Table: public.organizations

-- DROP TABLE IF EXISTS public.organizations;

CREATE TABLE IF NOT EXISTS public.organizations
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    slug character varying(255) COLLATE pg_catalog."default" NOT NULL,
    code character varying(255) COLLATE pg_catalog."default" NOT NULL,
    avatar_url character varying(255) COLLATE pg_catalog."default",
    location character varying(255) COLLATE pg_catalog."default" NOT NULL,
    time_zone character varying(255) COLLATE pg_catalog."default" NOT NULL,
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    updated_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_organizations PRIMARY KEY (id),
    CONSTRAINT uq_organizations_code UNIQUE (code),
    CONSTRAINT uq_organizations_slug UNIQUE (slug),
    CONSTRAINT chk_organizations_code CHECK (code::text ~ '^[A-Z0-9]{2,10}$'::text),
    CONSTRAINT chk_organizations_slug CHECK (slug::text ~ '^[a-z0-9-]{2,100}$'::text)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.organizations
    OWNER to postgres;

-- Trigger: set_updated_at

-- DROP TRIGGER IF EXISTS set_updated_at ON public.organizations;

CREATE OR REPLACE TRIGGER set_updated_at
    BEFORE UPDATE
    ON public.organizations
    FOR EACH ROW
EXECUTE FUNCTION public.trigger_set_updated_at();