CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE users
(
    id       BIGINT NOT NULL,
    email    VARCHAR(255),
    name     VARCHAR(255),
    phone    VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);