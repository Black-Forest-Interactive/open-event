CREATE SEQUENCE _user_seq;
CREATE TABLE _user
(
    id         BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('_user_seq'::regclass),
    external_id VARCHAR(255)                NOT NULL,
    user_name   VARCHAR(255)                NOT NULL,
    email      VARCHAR(255)                NOT NULL,
    first_name  VARCHAR(255)                NOT NULL,
    last_name   VARCHAR(255)                NOT NULL,
    created    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated    TIMESTAMP WITHOUT TIME ZONE
);
