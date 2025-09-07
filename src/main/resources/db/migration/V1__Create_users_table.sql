CREATE TABLE users (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       user_id VARCHAR(36) NOT NULL UNIQUE,
       first_name VARCHAR(50) NOT NULL,
       last_name VARCHAR(50) NOT NULL,
       email VARCHAR(100) NOT NULL UNIQUE,
       encrypted_password VARCHAR(255) NOT NULL,
       email_verification_token VARCHAR(255),
       email_verification_status BOOLEAN NOT NULL
);