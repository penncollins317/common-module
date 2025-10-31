DROP TABLE IF EXISTS file_meta;

CREATE TABLE file_meta
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    store_path   VARCHAR(60)  NOT NULL,
    access_url   VARCHAR(255),
    content_type VARCHAR(78),
    size         BIGINT                DEFAULT 0,
    status       VARCHAR(10),
    md5          VARCHAR(32),
    sha256       VARCHAR(64),
    user_id      BIGINT,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE file_meta
    ADD COLUMN acl VARCHAR(8) DEFAULT 'PRIVATE',
    ADD CONSTRAINT chk_acl_valid CHECK (acl IN ('PRIVATE', 'PUBLIC', 'INTERNAL'));