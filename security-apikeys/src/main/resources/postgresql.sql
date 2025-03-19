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

CREATE TABLE api_key(
    id BIGSERIAL PRIMARY KEY,
    app_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(64) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status INT NOT NULL
);

Create UNIQUE INDEX api_key_app_id_uk ON api_key(code);