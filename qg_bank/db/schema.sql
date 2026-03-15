CREATE DATABASE IF NOT EXISTS qg_bank CHARACTER SET utf8mb4;
USE qg_bank;

CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    role ENUM('admin','user') NOT NULL DEFAULT 'user'
);


CREATE TABLE transaction_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    type ENUM('存款','取款','转出','转入') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    target_user_id INT NULL,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS financial_products (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    user_id INT NOT NULL COMMENT '用户ID，关联users表',
    product_name VARCHAR(50) NOT NULL COMMENT '产品名称（活期理财/定期理财）',
    amount DECIMAL(15, 2) NOT NULL COMMENT '投资金额',
    rate DECIMAL(5, 4) NOT NULL COMMENT '年化收益率',
    term INT NOT NULL DEFAULT 0 COMMENT '期限（月），活期理财为0',
    buy_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '购买时间',
    status VARCHAR(20) DEFAULT '持有中' COMMENT '状态（持有中/已赎回）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='理财产品记录表';
