-- Table: public.labels

-- DROP TABLE IF EXISTS public.labels;

CREATE TABLE IF NOT EXISTS public.labels
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    org_id uuid NOT NULL,
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    color_hex character varying(7) COLLATE pg_catalog."default" NOT NULL DEFAULT '#6366F1'::character varying,
    CONSTRAINT pk_labels PRIMARY KEY (id),
    CONSTRAINT ukiyo312nf67erc4yfxerct1bvj UNIQUE (org_id, name),
    CONSTRAINT uq_labels_name_per_org UNIQUE (org_id, name),
    CONSTRAINT fk_labels_org FOREIGN KEY (org_id)
        REFERENCES public.organizations (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT chk_labels_color_hex CHECK (color_hex::text ~ '^#[A-Fa-f0-9]{6}$'::text)
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.labels
    OWNER to postgres;
-- Index: idx_labels_org_id

-- DROP INDEX IF EXISTS public.idx_labels_org_id;

CREATE INDEX IF NOT EXISTS idx_labels_org_id
    ON public.labels USING btree
        (org_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;