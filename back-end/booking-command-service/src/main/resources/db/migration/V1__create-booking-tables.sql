CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE
    IF NOT EXISTS status_table (
        status_id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
        status_code INTEGER NOT NULL UNIQUE,
        status_acronym VARCHAR(3) NOT NULL UNIQUE,
        status_description VARCHAR(15) NOT NULL UNIQUE
    );

INSERT INTO
    status_table (status_code, status_acronym, status_description)
VALUES
    (1, 'BKD', 'BOOKED'),
    (2, 'CKN', 'CHECK-IN'),
    (3, 'CLD', 'CANCELLED'),
    (4, 'BRD', 'BOARDED'),
    (5, 'CPD', 'COMPLETED'),
    (6, 'NCP', 'NOT COMPLETED');

CREATE TABLE IF NOT EXISTS booking_table (
    booking_id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    booking_code VARCHAR(6) NOT NULL UNIQUE, 
    flight_code VARCHAR(8) NOT NULL,
    booking_date TIMESTAMP WITH TIME ZONE NOT NULL,
    booking_status UUID NOT NULL,
    money_spent NUMERIC(10, 2) NOT NULL, 
    miles_spent INTEGER NOT NULL,
    seats_number INTEGER NOT NULL,
    user_id VARCHAR(24) NOT NULL,
    transaction_id UUID NOT NULL UNIQUE,
    CONSTRAINT fk_booking_status FOREIGN KEY (booking_status) REFERENCES status_table (status_id)
);


CREATE TABLE IF NOT EXISTS change_table (
    change_id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
    change_date TIMESTAMP WITH TIME ZONE NOT NULL,
    booking UUID NOT NULL,
    initial_status UUID NOT NULL,
    final_status UUID NOT NULL,
    CONSTRAINT fk_booking FOREIGN KEY (booking) REFERENCES booking_table (booking_id),
    CONSTRAINT fk_initial_status FOREIGN KEY (initial_status) REFERENCES status_table (status_id),
    CONSTRAINT fk_final_status FOREIGN KEY (final_status) REFERENCES status_table (status_id)
);