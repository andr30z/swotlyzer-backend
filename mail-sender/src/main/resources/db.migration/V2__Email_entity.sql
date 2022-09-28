CREATE SEQUENCE IF NOT EXISTS hibernate_sequence AS bigint START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;

CREATE TABLE emails
(
    id             BIGINT NOT NULL,
    content        TEXT,
    email_from     VARCHAR(255),
    email_to       VARCHAR(255),
    status         INTEGER,
    owner_ref      VARCHAR(255),
    send_date_mail TIMESTAMP WITHOUT TIME ZONE,
    subject        VARCHAR(255),
    CONSTRAINT emails_pkey PRIMARY KEY (id)
);