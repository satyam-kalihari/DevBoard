-- Table: public.org_members

-- DROP TABLE IF EXISTS public.org_members;

CREATE TABLE IF NOT EXISTS public.org_members
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    org_id uuid NOT NULL,
    user_id uuid NOT NULL,
    role character varying(255) COLLATE pg_catalog."default" NOT NULL DEFAULT 'MEMBER'::character varying,
    joined_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_org_members PRIMARY KEY (id),
    CONSTRAINT ukfidl85svc3ri5pi3f55g8mxxl UNIQUE (org_id, user_id),
    CONSTRAINT uq_org_members_org_user UNIQUE (org_id, user_id),
    CONSTRAINT fk_org_members_org FOREIGN KEY (org_id)
    REFERENCES public.organizations (id) MATCH SIMPLE
                        ON UPDATE NO ACTION
                        ON DELETE CASCADE,
    CONSTRAINT fk_org_members_user FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
                        ON UPDATE NO ACTION
                        ON DELETE CASCADE,
    CONSTRAINT chk_org_members_role CHECK (role::text = ANY (ARRAY['OWNER'::character varying::text, 'MEMBER'::character varying::text, 'VIEWER'::character varying::text]))
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.org_members
    OWNER to postgres;