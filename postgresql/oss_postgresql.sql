DROP TABLE IF EXISTS file_meta;

CREATE TABLE file_meta
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    store_path   VARCHAR(60)  NOT NULL,
    access_url   VARCHAR(255),
    content_type VARCHAR(78),
    size         BIGINT                   DEFAULT 0,
    status       VARCHAR(10),
    md5          VARCHAR(32),
    sha256       VARCHAR(64),
    user_id      BIGINT,
    is_public    BOOLEAN      NOT NULL    DEFAULT FALSE,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);