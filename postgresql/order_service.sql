CREATE TABLE order_header (
  id BIGINT PRIMARY KEY,           -- 订单ID，自增主键
  code VARCHAR(32) NOT NULL UNIQUE, -- 订单编号，业务唯一标识
  user_id BIGINT NOT NULL,                  -- 用户ID
  order_status SMALLINT NOT NULL DEFAULT 0, -- 订单状态（0: 待支付; 1: 已支付; 2: 已发货; 3: 已完成; 4: 已取消）
  total_amount NUMERIC(12, 2) NOT NULL,     -- 订单总金额
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 创建时间
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);


CREATE TABLE order_line (
  id BIGINT PRIMARY KEY, -- 订单明细ID，自增主键
  order_id BIGINT NOT NULL , -- 关联的订单ID
  product_name VARCHAR(128) NOT NULL,  -- 商品名称
  product_image VARCHAR(256),          -- 商品图片URL
  unit VARCHAR(32),                    -- 商品单位（如：件、公斤）
  unit_price NUMERIC(12, 2) NOT NULL,  -- 商品单价
  quantity INTEGER NOT NULL,           -- 购买数量
  subtotal_amount NUMERIC(12, 2) NOT NULL -- 小计金额（单价 × 数量）
);