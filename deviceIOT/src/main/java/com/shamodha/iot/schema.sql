-- src/main/resources/schema.sql

-- ---------------------------------------
-- Users table
-- ---------------------------------------
CREATE DATABASE IF NOT EXISTS greenhouse_db;
USE greenhouse_db;
CREATE TABLE IF NOT EXISTS users (
                                     id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- ---------------------------------------
-- Devices table (ThermoHygrometer)
-- ---------------------------------------
CREATE TABLE IF NOT EXISTS devices (
                                       device_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    name VARCHAR(100) NOT NULL,
    zone_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
    );