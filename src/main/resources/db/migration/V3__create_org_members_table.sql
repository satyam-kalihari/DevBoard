CREATE TABLE org_members (
                             id          UUID            NOT NULL DEFAULT gen_random_uuid(),
                             org_id      UUID            NOT NULL,
                             user_id     UUID            NOT NULL,
                             role        VARCHAR(20)     NOT NULL DEFAULT 'MEMBER',
                             joined_at   TIMESTAMP       NOT NULL DEFAULT NOW(),

                             CONSTRAINT pk_org_members           PRIMARY KEY (id),
                             CONSTRAINT uq_org_members_org_user  UNIQUE (org_id, user_id),
                             CONSTRAINT fk_org_members_org       FOREIGN KEY (org_id)
                                 REFERENCES organizations (id) ON DELETE CASCADE,
                             CONSTRAINT fk_org_members_user      FOREIGN KEY (user_id)
                                 REFERENCES users (id) ON DELETE CASCADE,
                             CONSTRAINT chk_org_members_role     CHECK (role IN ('OWNER', 'MEMBER', 'VIEWER'))
);