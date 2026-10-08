-- V{next}__allow_editor_role.sql
ALTER TABLE org_members DROP CONSTRAINT IF EXISTS chk_org_members_role;
ALTER TABLE org_members ADD CONSTRAINT chk_org_members_role
    CHECK (role IN ('OWNER', 'EDITOR', 'MEMBER', 'VIEWER'));