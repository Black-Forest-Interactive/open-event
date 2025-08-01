-- category
CREATE SEQUENCE category_seq;
CREATE TABLE category
(
    id       BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('category_seq'::regclass),
    name     VARCHAR(255)                NOT NULL,
    icon_url VARCHAR(255)                NOT NULL,

    created  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated  TIMESTAMP WITHOUT TIME ZONE
);

-- account
CREATE SEQUENCE account_seq;
CREATE TABLE account
(
    id              BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('account_seq'::regclass),
    external_id     VARCHAR(255) UNIQUE,
    name            VARCHAR(255)                NOT NULL,
    icon_url        VARCHAR(255)                NOT NULL,
    last_login_date TIMESTAMP WITHOUT TIME ZONE,
    service_account BOOLEAN                     NOT NULL,
    idp_linked      BOOLEAN                     NOT NULL,

    last_sync       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated         TIMESTAMP WITHOUT TIME ZONE
);


-- profile
CREATE TABLE profile
(
    id              BIGINT                      NOT NULL PRIMARY KEY REFERENCES account (id),

    email           VARCHAR(255) UNIQUE,
    phone           VARCHAR(255),
    mobile          VARCHAR(255),

    first_name      VARCHAR(255)                NOT NULL,
    last_name       VARCHAR(255)                NOT NULL,

    date_of_birth   VARCHAR(255),
    gender          VARCHAR(255),
    profile_picture VARCHAR(255),
    website         VARCHAR(255),

    language        VARCHAR(50),

    created         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated         TIMESTAMP WITHOUT TIME ZONE
);

-- profile
CREATE TABLE preferences
(
    id            BIGINT                      NOT NULL PRIMARY KEY REFERENCES account (id),
    email         TEXT                        NOT NULL,
    communication TEXT                        NOT NULL,
    notification  TEXT                        NOT NULL,

    created       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated       TIMESTAMP WITHOUT TIME ZONE
);

-- address
CREATE SEQUENCE address_seq;
CREATE TABLE address
(
    id              BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('address_seq'::regclass),
    street          VARCHAR(255)                NOT NULL,
    street_number   VARCHAR(255)                NOT NULL,
    zip             VARCHAR(255)                NOT NULL,
    city            VARCHAR(255)                NOT NULL,
    country         VARCHAR(255)                NOT NULL,
    additional_info VARCHAR(255)                NOT NULL,

    lat             DOUBLE PRECISION            NOT NULL,
    lon             DOUBLE PRECISION            NOT NULL,

    account_id      BIGINT                      NOT NULL REFERENCES account (id),

    created         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated         TIMESTAMP WITHOUT TIME ZONE
);


-- announcement
CREATE SEQUENCE announcement_seq;
CREATE TABLE announcement
(
    id        BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('announcement_seq'::regclass),
    subject   VARCHAR(255)                NOT NULL,
    content   TEXT                        NOT NULL,
    author_id BIGINT                      NOT NULL REFERENCES account (id),

    created   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated   TIMESTAMP WITHOUT TIME ZONE
);

-- event
CREATE SEQUENCE event_seq;
CREATE TABLE event
(
    id               BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('event_seq'::regclass),
    owner_id         BIGINT                      NOT NULL REFERENCES account (id),

    start            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    finish           TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    title            VARCHAR(255)                NOT NULL,
    short_text       VARCHAR(255)                NOT NULL,
    long_text        TEXT                        NOT NULL,
    image_url        VARCHAR(255)                NOT NULL,
    icon_url         VARCHAR(255)                NOT NULL,

    has_location     BOOLEAN                     NOT NULL,
    has_registration BOOLEAN                     NOT NULL,
    published        BOOLEAN                     NOT NULL,

    tags             TEXT                        NOT NULL,

    created          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated          TIMESTAMP WITHOUT TIME ZONE
);

-- event category
CREATE TABLE event_category
(
    event_id    BIGINT NOT NULL references event (id),
    category_id BIGINT NOT NULL references category (id),
    PRIMARY KEY (event_id, category_id)
);
-- event announcement
CREATE TABLE event_announcement
(
    event_id        BIGINT NOT NULL references event (id),
    announcement_id BIGINT NOT NULL references announcement (id),
    PRIMARY KEY (event_id, announcement_id)
);

-- location
CREATE SEQUENCE location_seq;
CREATE TABLE location
(
    id              BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('location_seq'::regclass),

    event_id        BIGINT                      NOT NULL REFERENCES event (id),

    street          VARCHAR(255)                NOT NULL,
    street_number   VARCHAR(255)                NOT NULL,
    zip             VARCHAR(255)                NOT NULL,
    city            VARCHAR(255)                NOT NULL,
    country         VARCHAR(255)                NOT NULL,
    additional_info VARCHAR(255)                NOT NULL,

    lat             DOUBLE PRECISION            NOT NULL,
    lon             DOUBLE PRECISION            NOT NULL,

    size            INT                         NOT NULL,

    created         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated         TIMESTAMP WITHOUT TIME ZONE
);

-- registration
CREATE SEQUENCE registration_seq;
CREATE TABLE registration
(
    id                 BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('registration_seq'::regclass),
    event_id           BIGINT                      NOT NULL REFERENCES event (id),

    max_guest_amount   INT                         NOT NULL,
    interested_allowed BOOLEAN                     NOT NULL,
    tickets_enabled    BOOLEAN                     NOT NULL,

    created            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated            TIMESTAMP WITHOUT TIME ZONE
);

-- participant
CREATE SEQUENCE participant_seq;
CREATE TABLE participant
(
    id              BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('participant_seq'::regclass),
    registration_id BIGINT                      NOT NULL REFERENCES registration (id),
    account_id      BIGINT                      NOT NULL REFERENCES account (id),

    size            BIGINT                      NOT NULL,
    status          VARCHAR(255)                NOT NULL,
    rank            INT                         NOT NULL,
    waiting_list    BOOLEAN                     NOT NULL,

    created         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated         TIMESTAMP WITHOUT TIME ZONE
);

-- mail_job
CREATE SEQUENCE mail_job_seq;
CREATE TABLE mail_job
(
    id      BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('mail_job_seq'::regclass),
    status  VARCHAR(255)                NOT NULL,
    title   TEXT                        NOT NULL,

    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE
);


-- mail_job_content
CREATE SEQUENCE mail_job_content_seq;
CREATE TABLE mail_job_content
(
    id        BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('mail_job_content_seq'::regclass),
    mail_json TEXT   NOT NULL,
    from_json TEXT   NOT NULL,
    to_json   TEXT   NOT NULL,
    bcc_json  TEXT   NOT NULL,

    job_id    BIGINT NOT NULL UNIQUE REFERENCES mail_job (id)
);

-- mail_job_history
CREATE SEQUENCE mail_job_history_seq;
CREATE TABLE mail_job_history
(
    id        BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('mail_job_history_seq'::regclass),
    message   TEXT                        NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    job_id    BIGINT                      NOT NULL REFERENCES mail_job (id)
);


-- setting
CREATE SEQUENCE setting_seq;
CREATE TABLE setting
(
    id      BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('setting_seq'::regclass),
    key     VARCHAR(255)                NOT NULL UNIQUE,
    value   TEXT                        NOT NULL,
    type    VARCHAR(255)                NOT NULL,

    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE
);

-- default settings
INSERT INTO setting(key, value, type, created)
VALUES ('url.help', 'http://localhost', 'URL', now());

INSERT INTO setting(key, value, type, created)
VALUES ('url.terms-and-conditions', 'http://localhost', 'URL', now());

INSERT INTO setting(key, value, type, created)
VALUES ('url.share', 'http://localhost', 'URL', now());

INSERT INTO setting(key, value, type, created)
VALUES ('mail.from-address', 'mail@test.com', 'EMAIL', now());

INSERT INTO setting(key, value, type, created)
VALUES ('mail.reply-to-address', 'mail@test.com', 'EMAIL', now());

INSERT INTO setting(key, value, type, created)
VALUES ('mail.default-admin-address', 'mail@test.com', 'EMAIL', now());

INSERT INTO setting(key, value, type, created)
VALUES ('text.title', 'APP.Title', 'STRING', now());

INSERT INTO setting(key, value, type, created)
VALUES ('default.language', 'en', 'STRING', now());

-- notification
CREATE SEQUENCE notification_setting_seq;
CREATE TABLE notification_setting
(
    id      BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('notification_setting_seq'::regclass),
    name    VARCHAR(255)                NOT NULL,
    enabled BOOLEAN                     NOT NULL,

    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE
);

CREATE SEQUENCE notification_scheme_seq;
CREATE TABLE notification_scheme
(
    id      BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('notification_scheme_seq'::regclass),
    name    VARCHAR(255)                NOT NULL,
    enabled BOOLEAN                     NOT NULL,

    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE notification_scheme_subscriber_relation
(
    scheme_id  BIGINT NOT NULL references notification_scheme (id),
    account_id BIGINT NOT NULL references account (id),
    PRIMARY KEY (scheme_id, account_id)
);


CREATE SEQUENCE notification_type_seq;
CREATE TABLE notification_type
(
    id          BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('notification_type_seq'::regclass),

    key         VARCHAR(255)                NOT NULL UNIQUE,
    name        VARCHAR(255)                NOT NULL,
    description TEXT                        NOT NULL,

    created     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated     TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE notification_type_scheme_relation
(
    type_id   BIGINT NOT NULL references notification_type (id),
    scheme_id BIGINT NOT NULL references notification_scheme (id),
    PRIMARY KEY (type_id, scheme_id)
);



CREATE SEQUENCE notification_template_seq;
CREATE TABLE notification_template
(
    id      BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('notification_template_seq'::regclass),
    type_id BIGINT                      NOT NULL references notification_type (id),

    subject VARCHAR(255)                NOT NULL,
    lang    VARCHAR(255)                NOT NULL,
    content TEXT                        NOT NULL,
    plain   BOOLEAN                     NOT NULL,

    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE
);


-- audit
CREATE SEQUENCE audit_log_entry_seq;
CREATE TABLE audit_log_entry
(
    id           BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('audit_log_entry_seq'::regclass),
    timestamp    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    actor        VARCHAR(255)                NOT NULL,
    level        VARCHAR(255)                NOT NULL,
    message      TEXT                        NOT NULL,
    request      TEXT                        NOT NULL,
    reference_id VARCHAR(255)                NOT NULL,
    reference    TEXT                        NOT NULL,
    source       VARCHAR(255)                NOT NULL
);

-- history
CREATE SEQUENCE history_entry_seq;
CREATE TABLE history_entry
(
    id        BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('history_entry_seq'::regclass),

    event_id  BIGINT                      NOT NULL references event (id),
    actor_id  BIGINT                      NOT NULL references account (id),

    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    type      VARCHAR(255)                NOT NULL,
    message   VARCHAR(255)                NOT NULL,
    source    VARCHAR(255)                NOT NULL,
    info      VARCHAR(255)                NOT NULL
);

-- share
CREATE TABLE share
(
    id       VARCHAR(40) PRIMARY KEY,
    event_id BIGINT                      NOT NULL references event (id),
    enabled  BOOLEAN                     NOT NULL,

    created  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated  TIMESTAMP WITHOUT TIME ZONE
);


-- activity source
CREATE SEQUENCE activity_source_seq;
CREATE TABLE activity_source
(
    id      BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('activity_source_seq'::regclass),
    key     VARCHAR(255)                NOT NULL,

    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated TIMESTAMP WITHOUT TIME ZONE
);


-- activity type
CREATE SEQUENCE activity_type_seq;
CREATE TABLE activity_type
(
    id        BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('activity_type_seq'::regclass),
    key       VARCHAR(255)                NOT NULL,
    source_id BIGINT                      NOT NULL references activity_source (id),

    created   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated   TIMESTAMP WITHOUT TIME ZONE
);

-- activity
CREATE SEQUENCE activity_seq;
CREATE TABLE activity
(
    id           BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('activity_seq'::regclass),
    title        VARCHAR(255)                NOT NULL,
    actor_id     BIGINT                      NOT NULL references account (id),

    source_id    BIGINT                      NOT NULL references activity_source (id),
    type_id      BIGINT                      NOT NULL references activity_type (id),

    reference_id BIGINT                      NOT NULL,

    created      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated      TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE activity_subscriber
(
    activity_id BIGINT                      NOT NULL references activity (id),
    account_id  BIGINT                      NOT NULL references account (id),
    read        BOOLEAN                     NOT NULL,
    timestamp   TIMESTAMP WITHOUT TIME ZONE NOT NULL,

    PRIMARY KEY (activity_id, account_id)
);

CREATE TABLE activity_source_subscriber
(
    account_id BIGINT NOT NULL references account (id),
    source_id  BIGINT NOT NULL references activity_source (id),
    PRIMARY KEY (account_id, source_id)
);

CREATE TABLE activity_type_subscriber
(
    account_id BIGINT NOT NULL references account (id),
    type_id    BIGINT NOT NULL references activity_type (id),
    PRIMARY KEY (account_id, type_id)
);

-- issue

CREATE SEQUENCE issue_seq;
CREATE TABLE issue
(
    id          BIGINT                      NOT NULL PRIMARY KEY DEFAULT nextval('issue_seq'::regclass),
    subject     TEXT                        NOT NULL,
    description TEXT                        NOT NULL,
    error       TEXT                        NOT NULL,
    url         TEXT                        NOT NULL,

    status      VARCHAR(30)                 NOT NULL,

    client_ip   VARCHAR(30)                 NOT NULL,
    user_agent  TEXT                        NOT NULL,

    account_id  BIGINT                      NOT NULL references account (id),

    created     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated     TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE participant_external
(
    id                          VARCHAR(40) PRIMARY KEY,

    event_id                    BIGINT                      NOT NULL references event (id),

    first_name                  VARCHAR(255)                NOT NULL,
    last_name                   VARCHAR(255)                NOT NULL,
    email                       VARCHAR(255)                NOT NULL,
    phone                       VARCHAR(255),
    mobile                      VARCHAR(255),
    language                    VARCHAR(50),
    size                        BIGINT                      NOT NULL,

    expires                     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    code                        VARCHAR(10)                 NOT NULL,
    invalid_confirmation_trials INT                         NOT NULL,

    created                     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated                     TIMESTAMP WITHOUT TIME ZONE
);