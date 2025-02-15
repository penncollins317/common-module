CREATE TABLE out_app (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    user_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    icon_url VARCHAR(255) NOT NULL,
    secret_key VARCHAR(64) NULL,
    aes_key VARCHAR(32) NULL,
    dev_server_url VARCHAR(100) NULL,
    status INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uk_out_app_name ON out_app(name);