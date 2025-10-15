-- setup roles & authorities
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE authorities (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users_roles (
     user_id BIGINT NOT NULL,
     role_id BIGINT NOT NULL,
     PRIMARY KEY (user_id, role_id),
     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
     FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE roles_authorities (
     role_id BIGINT NOT NULL,
     authority_id BIGINT NOT NULL,
     PRIMARY KEY (role_id, authority_id),
     FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
     FOREIGN KEY (authority_id) REFERENCES authorities(id) ON DELETE CASCADE
);

-- init data
INSERT INTO authorities (name) VALUES ('READ_INFO');
INSERT INTO authorities (name) VALUES ('CREATE_INFO');
INSERT INTO authorities (name) VALUES ('DELETE_INFO');
INSERT INTO authorities (name) VALUES ('UPDATE_INFO');

INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_USER');

INSERT INTO roles_authorities (role_id, authority_id)
SELECT r.id, a.id
FROM roles r
CROSS JOIN authorities a
WHERE r.name = 'ROLE_ADMIN';

INSERT INTO roles_authorities (role_id, authority_id)
SELECT r.id, a.id
FROM roles r
JOIN authorities a ON a.name IN ('READ_INFO', 'CREATE_INFO')
WHERE r.name = 'ROLE_USER';