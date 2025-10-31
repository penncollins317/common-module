DROP TABLE IF EXISTS file_shared;
CREATE TABLE file_shared
(

    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    file_id    BIGINT       NOT NULL,
    token      VARCHAR(50)  NOT NULL,
    url        VARCHAR(255) NOT NULL,
    expire_at  TIMESTAMP    NULL, -- 过期时间，为空时，永不过期
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE INDEX file_shared_token_idx ON file_shared (token);
CREATE INDEX file_shared_user_id_idx ON file_shared (user_id);
CREATE INDEX file_shared_file_id_idx ON file_shared (file_id);
