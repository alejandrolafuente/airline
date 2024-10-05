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