-- Table: public.standups

-- DROP TABLE IF EXISTS public.standups;

CREATE TABLE IF NOT EXISTS public.standups
(
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    project_id uuid NOT NULL,
    schedule_days character varying(255) COLLATE pg_catalog."default" NOT NULL DEFAULT 'MON,TUE,WED,THU,FRI'::character varying,
    scheduled_time time without time zone NOT NULL DEFAULT '09:00:00'::time without time zone,
    is_active boolean NOT NULL DEFAULT true,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT pk_standups PRIMARY KEY (id),
    CONSTRAINT uq_standups_project UNIQUE (project_id),
    CONSTRAINT fk_standups_project FOREIGN KEY (project_id)
    REFERENCES public.projects (id) MATCH SIMPLE
                        ON UPDATE NO ACTION
                        ON DELETE CASCADE,
    CONSTRAINT chk_standups_days CHECK (schedule_days::text ~ '^(MON|TUE|WED|THU|FRI|SAT|SUN)(,(MON|TUE|WED|THU|FRI|SAT|SUN))*$'::text)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.standups
    OWNER to postgres;