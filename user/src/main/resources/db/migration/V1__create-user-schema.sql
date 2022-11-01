CREATE SEQUENCE _user_seq;
CREATE TABLE _user
(
    id          BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('_user_seq'::regclass),
    external_id VARCHAR(255)                NOT NULL,
    type        VARCHAR(255)                NOT NULL,
    user_name   VARCHAR(255)                NOT NULL,
    first_name  VARCHAR(255)                NOT NULL,
    last_name   VARCHAR(255)                NOT NULL,
    email       VARCHAR(255)                NOT NULL,
    mobile      VARCHAR(255)                NOT NULL,
    phone       VARCHAR(255)                NOT NULL,
    created     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated     TIMESTAMP WITHOUT TIME ZONE
);

CREATE SEQUENCE _group_seq;
CREATE TABLE _group
(
    id      BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('_group_seq'::regclass),
    name    VARCHAR(255)                NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE group_user_relation
(
    group_id BIGINT NOT NULL,
    user_id  BIGINT NOT NULL,
    CONSTRAINT fk_mapping_group FOREIGN KEY (group_id) REFERENCES _group (id),
    CONSTRAINT fk_mapping_user FOREIGN KEY (user_id) REFERENCES _user (id)
);

CREATE SEQUENCE location_seq;
CREATE TABLE location
(
    id       BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('location_seq'::regclass),
    capacity INTEGER                     NOT NULL,
    created  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated  TIMESTAMP WITHOUT TIME ZONE
);

CREATE SEQUENCE address_seq;
CREATE TABLE address
(
    id              BIGINT       NOT NULL PRIMARY KEY DEFAULT nextval('address_seq'::regclass),
    location_id     BIGINT       NOT NULL,
    street          VARCHAR(255) NOT NULL,
    street_number   VARCHAR(20)  NOT NULL,
    zip             VARCHAR(20)  NOT NULL,
    city            VARCHAR(255) NOT NULL,
    country         VARCHAR(255) NOT NULL,
    additional_info VARCHAR(255) NOT NULL,
    CONSTRAINT fk_address_location FOREIGN KEY (location_id) REFERENCES location (id)
);

CREATE SEQUENCE geo_location_seq;
CREATE TABLE geo_location
(
    id          BIGINT           NOT NULL PRIMARY KEY DEFAULT nextval('geo_location_seq'::regclass),
    location_id BIGINT           NOT NULL,
    lat         DOUBLE PRECISION NOT NULL,
    lon         DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_geo_location_location FOREIGN KEY (location_id) REFERENCES location (id)
);
