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


CREATE TABLE user_account(
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    account_type VARCHAR(5) NOT NULL,
    account_value VARCHAR(100) NOT NULL,
    validated BOOL NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uk_user_account ON user_account(account_type, account_value);
CREATE UNIQUE INDEX uk_user_account_user_id ON user_account(user_id);


CREATE TABLE user_address (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,               -- 用户ID，外键可选
    name VARCHAR(10) NOT NULL,             -- 收件人姓名
    phone VARCHAR(11) NOT NULL,            -- 收件人电话
    province VARCHAR(50),                  -- 省
    city VARCHAR(50),                      -- 市
    district VARCHAR(50),                  -- 区
    detail VARCHAR(255) NOT NULL,          -- 详细地址
    street VARCHAR(100),                   -- 门牌号（如 XX街道XX号）
    latitude DECIMAL(10, 6),               -- 纬度（例如：23.129163）
    longitude DECIMAL(10, 6),              -- 经度（例如：113.264435）
    is_default BOOLEAN DEFAULT FALSE,      -- 是否为默认地址
    created_at TIMESTAMP DEFAULT now(),    -- 创建时间
    updated_at TIMESTAMP DEFAULT now()     -- 更新时间
);

CREATE INDEX user_address_user_idx ON user_address(user_id, is_default);