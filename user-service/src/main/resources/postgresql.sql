CREATE TABLE t_user(
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(255) NULL,
    nickname VARCHAR(50) NOT NULL,
    avatar_url VARCHAR(255) NULL,
    email VARCHAR(100) NULL,
    phone VARCHAR(11) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL,
    pwd_version INT NOT NULL DEFAULT 0,
    is_active INT NOT NULL DEFAULT 1
);

CREATE UNIQUE INDEX uk_user_username ON t_user(username);
CREATE UNIQUE INDEX uk_user_email ON t_user(email);
CREATE UNIQUE INDEX uk_user_phone ON t_user(phone);
-- admin,admin
INSERT INTO t_user(id, username, password, nickname) VALUES (1, 'admin', '$2a$10$o5d1XEvFtVD7u58vfWHZsuYggWyH/6BdSdTrbqgtRCTugJU5Lqkqa', '管理员');

CREATE TABLE t_role(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX uk_role_name ON t_role(name);

INSERT INTO t_role(id,name) VALUES (1, 'ROLE_ADMIN');

CREATE TABLE t_user_role(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL
);
CREATE UNIQUE INDEX uk_user_role_id ON t_user_role(user_id, role_id);
INSERT INTO t_user_role(user_id,role_id) VALUES (1,1);