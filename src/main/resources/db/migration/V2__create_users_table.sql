CREATE TABLE users (
                       id          UUID            NOT NULL DEFAULT gen_random_uuid(),
                       name        VARCHAR(255)    NOT NULL,
                       email       VARCHAR(255)    NOT NULL,
                       phone       VARCHAR(20),
                       avatar_url  TEXT,
                       is_active   BOOLEAN         NOT NULL DEFAULT TRUE,
                       created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
                       updated_at  TIMESTAMP       NOT NULL DEFAULT NOW(),

                       CONSTRAINT pk_users         PRIMARY KEY (id),
                       CONSTRAINT uq_users_email   UNIQUE (email),
                       CONSTRAINT uq_users_phone   UNIQUE (phone),
                       CONSTRAINT chk_users_email  CHECK (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    CONSTRAINT chk_users_phone  CHECK (phone ~ '^\+?[0-9]{7,15}$')
);

CREATE TRIGGER set_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION trigger_set_updated_at();