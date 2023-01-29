CREATE TABLE accommodation
(
    id SERIAL PRIMARY KEY,
    address CHARACTER VARYING(100) NOT NULL,
    checkin_time TIMESTAMP NOT NULL,
    checkout_time TIMESTAMP NOT NULL,
    booking_tickets CHARACTER VARYING(50) NOT NULL
);

CREATE TABLE office
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    address CHARACTER VARYING(100) NOT NULL,
    description text
);

CREATE TABLE destination
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    address CHARACTER VARYING(100) NOT NULL,
    description text,
    office_id CHARACTER VARYING(36) REFERENCES office(id),
    seat_place CHARACTER VARYING(70) NOT NULL
);

CREATE TABLE tripDto
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    trip_status CHARACTER VARYING(15) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    office_id CHARACTER VARYING(36) REFERENCES office(id)
);

CREATE TABLE transportation
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    dtype CHARACTER VARYING(5) NOT NULL,
    date TIMESTAMP NOT NULL,
    from_where CHARACTER VARYING(50) NOT NULL,
    to_where CHARACTER VARYING(50) NOT NULL,
    departure_point CHARACTER VARYING(50) NOT NULL,
    cruise_number CHARACTER VARYING(50) NOT NULL,
    carrier_name CHARACTER VARYING(50) NOT NULL,
    cost INTEGER NOT NULL,
    recommended_datetime TIMESTAMP NOT NULL
);

CREATE TABLE request
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    trip_id CHARACTER VARYING(36) REFERENCES trip(id) NOT NULL,
    status_id INTEGER NOT NULL,
    description text,
    worker_id CHARACTER VARYING(36) REFERENCES user(id) NOT NULL,
    office_id CHARACTER VARYING(36) REFERENCES office(id),
    accommodation_id CHARACTER VARYING(36) REFERENCES accommodation(id),
    comment text,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    transport_to CHARACTER VARYING(50) NOT NULL,
    transport_from CHARACTER VARYING(50) NOT NULL,
    tickets CHARACTER VARYING(100) NOT NULL
);

CREATE TABLE notification
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    request_id CHARACTER VARYING(36) REFERENCES request(id),
    watched BOOLEAN
);

CREATE TABLE trip
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    /*request_id CHARACTER VARYING(36) REFERENCES request(id)
      Я не совсем понял зачем так, если реквест ссылается на этот трип, посмотри пж и рассуди
     */
    trip_status CHARACTER VARYING(15) NOT NULL,
    accommodation_id CHARACTER VARYING(36) REFERENCES accommodation(id) NOT NULL,
    destination text
);

CREATE TABLE requestDto
(
    id CHARACTER VARYING(36) PRIMARY KEY,
    status_id INTEGER NOT NULL,
    description text,
    worker_id CHARACTER VARYING(36) REFERENCES user(id) NOT NULL,
    start_date TIMESTAMP NOT NULL
);