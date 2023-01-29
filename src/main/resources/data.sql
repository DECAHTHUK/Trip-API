CREATE TABLE users
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    email CHARACTER VARYING(100) NOT NULL,
    password CHARACTER VARYING(100) NOT NULL,
    user_name CHARACTER VARYING(50) NOT NULL,
    surname CHARACTER VARYING(50) NOT NULL,
    user_role CHARACTER VARYING(50) NOT NULL
);
CREATE INDEX idx_users_email on users (email);

CREATE TABLE user_subordinator
(
    boss_id CHARACTER VARYING(36),
    subordinator_id CHARACTER VARYING(36)
);
CREATE INDEX idx_boss_subordinator ON user_subordinator (boss_id, subordinator_id);

CREATE TABLE notification
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    request_id CHARACTER VARYING(36) REFERENCES request (id),
    watched BOOLEAN,
    user_id CHARACTER VARYING(36) REFERENCES users (id)
);
CREATE INDEX idx_notification_user_id ON notification (user_id, watched);

CREATE TABLE accommodation
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    address CHARACTER VARYING(100) NOT NULL,
    checkin_time TIMESTAMP,
    checkout_time TIMESTAMP,
    booking_tickets TEXT
);

CREATE TABLE office
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    address CHARACTER VARYING(100) NOT NULL,
    description TEXT
);

CREATE TABLE destination
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    address CHARACTER VARYING(100) NOT NULL,
    description TEXT,
    office_id CHARACTER VARYING(36) REFERENCES office(id),
    seat_place TEXT
);

CREATE TABLE transportation
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    dtype CHARACTER VARYING(5) NOT NULL,
    start_date TIMESTAMP,
    from_where CHARACTER VARYING(50),
    to_where CHARACTER VARYING(50),
    departure_point CHARACTER VARYING(50),
    recommended_datetime TIMESTAMP
);

CREATE TABLE request
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    trip_id CHARACTER VARYING(36) REFERENCES trip(id) NOT NULL,
    status CHARACTER VARYING(20) NOT NULL,
    description TEXT,
    worker_id CHARACTER VARYING(36) REFERENCES users(id) NOT NULL,
    office_id CHARACTER VARYING(36) REFERENCES office(id),
    comment TEXT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    transport_to CHARACTER VARYING(50),
    transport_from CHARACTER VARYING(50),
    tickets CHARACTER VARYING(100)
);
CREATE INDEX idx_request_worker_id on request (worker_id);
CREATE INDEX idx_request_worker_id_status on request (worker_id, status);

CREATE TABLE trip
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    request_id CHARACTER VARYING(36) REFERENCES request(id),
    trip_status CHARACTER VARYING(10) NOT NULL,
    accommodation_id CHARACTER VARYING(36) REFERENCES accommodation (id),
    destination CHARACTER VARYING(36) REFERENCES destination (id)
);
