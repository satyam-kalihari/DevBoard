-- Table: public.notifications

-- DROP TABLE IF EXISTS public.notifications;

CREATE TABLE IF NOT EXISTS public.notifications
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    org_id uuid NOT NULL,
    type character varying(255) COLLATE pg_catalog."default" NOT NULL,
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    payload jsonb,
    is_read boolean NOT NULL DEFAULT false,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_notifications PRIMARY KEY (id),
    CONSTRAINT fk_notifications_org FOREIGN KEY (org_id)
        REFERENCES public.organizations (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT chk_notifications_type CHECK (type::text = ANY (ARRAY['TASK_ASSIGNED'::character varying::text, 'TASK_COMMENT_MENTION'::character varying::text, 'STANDUP_REMINDER'::character varying::text, 'SPRINT_STARTED'::character varying::text, 'SPRINT_ENDED'::character varying::text, 'BLOCKER_ADDED'::character varying::text, 'MEMBER_INVITED'::character varying::text]))
)

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.notifications
    OWNER to postgres;
-- Index: idx_notifications_is_read

-- DROP INDEX IF EXISTS public.idx_notifications_is_read;

CREATE INDEX IF NOT EXISTS idx_notifications_is_read
    ON public.notifications USING btree
        (user_id ASC NULLS LAST, is_read ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default
    WHERE is_read = false;
-- Index: idx_notifications_user_id

-- DROP INDEX IF EXISTS public.idx_notifications_user_id;

CREATE INDEX IF NOT EXISTS idx_notifications_user_id
    ON public.notifications USING btree
        (user_id ASC NULLS LAST)
    WITH (fillfactor=100, deduplicate_items=True)
    TABLESPACE pg_default;