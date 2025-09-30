CREATE TABLE payment_request
(
    id           BIGSERIAL PRIMARY KEY,                 -- 主键，自增
    subject      VARCHAR(255)   NOT NULL,               -- 主题/描述
    out_trade_no VARCHAR(64)    NOT NULL,               -- 外部交易号（业务唯一标识）
    status       VARCHAR(32)    NOT NULL,               -- 支付状态（枚举存储为字符串）
    amount       NUMERIC(18, 2) NOT NULL,               -- 金额，保留两位小数
    payment_at   TIMESTAMP,                             -- 支付完成时间
    created_at   TIMESTAMP      NOT NULL DEFAULT now(), -- 创建时间
    updated_at   TIMESTAMP      NOT NULL DEFAULT now()  -- 更新时间
);

-- 确保外部交易号唯一
CREATE UNIQUE INDEX idx_payment_request_out_trade_no
    ON payment_request (out_trade_no);

-- 状态 + 创建时间索引，便于查询某状态的最新请求
CREATE INDEX idx_payment_request_status_created
    ON payment_request (status, created_at DESC);

-- 支付时间索引，便于统计/查询已支付订单
CREATE INDEX idx_payment_request_payment_at
    ON payment_request (payment_at);

-- 金额区间查询索引（如统计大额订单）
CREATE INDEX idx_payment_request_amount
    ON payment_request (amount);



CREATE TABLE payment_goods
(
    id         BIGSERIAL PRIMARY KEY,                        -- 主键
    payment_id BIGINT         NOT NULL,                      -- 关联 PaymentRequest.id
    goods_id   VARCHAR(64)    NOT NULL,                      -- 商品编号（外部业务ID）
    goods_name VARCHAR(255)   NOT NULL,                      -- 商品名称
    price      NUMERIC(18, 2) NOT NULL CHECK (price >= 0),   -- 单价，不能为负
    quantity   INT            NOT NULL CHECK (quantity > 0) -- 数量，必须大于 0
);

-- 按支付请求查所有商品
CREATE INDEX idx_payment_goods_payment_id
    ON payment_goods (payment_id);

-- 如果经常按商品ID查询（例如统计某商品的销售情况）
CREATE INDEX idx_payment_goods_goods_id
    ON payment_goods (goods_id);

-- 如果需要查询单价范围
CREATE INDEX idx_payment_goods_price
    ON payment_goods (price);
