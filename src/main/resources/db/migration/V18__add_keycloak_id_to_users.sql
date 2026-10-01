-- V18__add_keycloak_id_to_users.sql
ALTER TABLE users ADD COLUMN keycloak_id UUID UNIQUE;