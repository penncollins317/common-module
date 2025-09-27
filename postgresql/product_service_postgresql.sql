CREATE TABLE product_category(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    parent_id BIGINT NULL,
    path VARCHAR(60) NULL,
    image_url VARCHAR(255) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX uq_product_category_name_parent ON product_category(parent_id, name);
CREATE INDEX idx_product_category_path ON product_category(path);

CREATE TABLE band(
    id BIGINT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    image_url VARCHAR(255) NULL
);

CREATE UNIQUE INDEX band_name_uk ON band(name);
