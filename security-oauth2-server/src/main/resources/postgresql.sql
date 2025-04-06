CREATE TABLE oauth2_user_related(
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    out_uid VARCHAR(50) NOT NULL,
    provider VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX oauth2_user_related_userid ON oauth2_user_related(provider, out_uid);