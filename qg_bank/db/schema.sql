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

