CREATE TABLE addresses (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       city VARCHAR(50) NOT NULL,
       country VARCHAR(50) NOT NULL,
       street VARCHAR(100) NOT NULL,
       postal_code VARCHAR(20) NOT NULL,
       type VARCHAR(20) NOT NULL,
       user_id BIGINT,
       CONSTRAINT fk_addresses_user_id
           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_addresses_user_id ON addresses(user_id);