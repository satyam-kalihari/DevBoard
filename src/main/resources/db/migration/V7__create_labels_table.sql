CREATE TABLE labels (
                        id        UUID            NOT NULL DEFAULT gen_random_uuid(),
                        org_id    UUID            NOT NULL,
                        name      VARCHAR(50)     NOT NULL,
                        color_hex VARCHAR(7)      NOT NULL DEFAULT '#6366F1',

                        CONSTRAINT pk_labels                PRIMARY KEY (id),
                        CONSTRAINT fk_labels_org            FOREIGN KEY (org_id)
                            REFERENCES organizations (id) ON DELETE CASCADE,
                        CONSTRAINT uq_labels_name_per_org   UNIQUE (org_id, name),
                        CONSTRAINT chk_labels_color_hex     CHECK (color_hex ~ '^#[A-Fa-f0-9]{6}$')
    );

CREATE INDEX idx_labels_org_id ON labels (org_id);