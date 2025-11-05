-- ============================
-- 支付请求表 (payment_request)
-- ============================
DROP TABLE IF EXISTS payment_request;
CREATE TABLE payment_request
(
    id         BIGSERIAL PRIMARY KEY,                 -- 主键
    subject    VARCHAR(255)   NOT NULL,               -- 主题/描述
    origin     VARCHAR(80)    NULL,                   -- 订单来源（源订单号）
    status     VARCHAR(32)    NOT NULL,               -- pending, success, failed, canceled
    amount     NUMERIC(18, 2) NOT NULL,               -- 金额
    remark     varchar(255)   NULL,                   -- 备注
    payment_at TIMESTAMP,                             -- 支付完成时间
    created_at TIMESTAMP      NOT NULL DEFAULT now(), -- 创建时间
    updated_at TIMESTAMP      NOT NULL DEFAULT now()  -- 更新时间
);

-- 索引
CREATE INDEX idx_pr_status_created
    ON payment_request (status, created_at DESC);

CREATE INDEX idx_pr_payment_at
    ON payment_request (payment_at);


-- ============================
-- 支付商品表 (payment_goods)
-- ============================
DROP TABLE IF EXISTS payment_goods;
CREATE TABLE payment_goods
(
    id         BIGSERIAL PRIMARY KEY,                       -- 主键
    payment_id BIGINT         NOT NULL,                     -- 关联 payment_request.id
    goods_id   VARCHAR(64)    NOT NULL,                     -- 商品编号（外部业务ID）
    goods_name VARCHAR(255)   NOT NULL,                     -- 商品名称
    price      NUMERIC(18, 2) NOT NULL CHECK (price >= 0),  -- 单价
    quantity   INT            NOT NULL CHECK (quantity > 0) -- 数量
);

-- 索引
CREATE INDEX idx_pg_payment_id
    ON payment_goods (payment_id);



-- ============================
-- 支付流水表 (payment_transaction)
-- ============================
DROP TABLE IF EXISTS payment_transaction;
CREATE TABLE payment_transaction
(
    id              BIGSERIAL PRIMARY KEY,
    payment_id      BIGINT       NOT NULL, -- 关联 payment_request.id
    out_trade_no    VARCHAR(50)  NOT NULL, -- 系统生成的唯一单号
    transaction_id  VARCHAR(128) NULL,     -- 渠道返回的唯一交易号
    channel         VARCHAR(32),           -- alipay, wechat, ...
    amount          NUMERIC(12, 2),
    channel_status  VARCHAR(32)  NULL,     -- 支付渠道商支付状态
    channel_payload JSONB,
    payment_at      TIMESTAMP    NULL,     -- 实际支付成功时间
    created_at      TIMESTAMP DEFAULT now(),
    updated_at      TIMESTAMP DEFAULT now()
);

-- 唯一索引
CREATE UNIQUE INDEX uk_pt_out_trade_no
    ON payment_transaction (out_trade_no);

CREATE UNIQUE INDEX uk_pt_channel_txn_id
    ON payment_transaction (transaction_id);

-- 普通索引
CREATE INDEX idx_pt_payment_id
    ON payment_transaction (payment_id);

CREATE INDEX idx_pt_channel_status
    ON payment_transaction (channel_status);

CREATE INDEX idx_pt_payment_at
    ON payment_transaction (payment_at);

-- 常用组合索引（可选）
-- 1. 查某支付单下最新交易
CREATE INDEX idx_pt_payment_id_created
    ON payment_transaction (payment_id, created_at DESC);

CREATE INDEX idx_pt_channel_status_updated
    ON payment_transaction (channel_status, updated_at);
