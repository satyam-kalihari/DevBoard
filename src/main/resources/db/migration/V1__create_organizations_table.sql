CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE organizations (
                               id          UUID            NOT NULL DEFAULT gen_random_uuid(),
                               name        VARCHAR(255)    NOT NULL,
                               slug        VARCHAR(100)    NOT NULL,
                               code        VARCHAR(10)     NOT NULL,
                               avatar_url  TEXT,
                               location    VARCHAR(255)    NOT NULL,
                               timezone    VARCHAR(100)    NOT NULL,
                               is_active   BOOLEAN         NOT NULL DEFAULT TRUE,
                               created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
                               updated_at  TIMESTAMP       NOT NULL DEFAULT NOW(),

                               CONSTRAINT pk_organizations        PRIMARY KEY (id),
                               CONSTRAINT uq_organizations_slug   UNIQUE (slug),
                               CONSTRAINT uq_organizations_code   UNIQUE (code),
                               CONSTRAINT chk_organizations_code  CHECK (code ~ '^[A-Z0-9]{2,10}$'),
    CONSTRAINT chk_organizations_slug  CHECK (slug ~ '^[a-z0-9-]{2,100}$')
);

CREATE OR REPLACE FUNCTION trigger_set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at
    BEFORE UPDATE ON organizations
    FOR EACH ROW EXECUTE FUNCTION trigger_set_updated_at();