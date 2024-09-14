CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE airport_table (
    airport_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(3),
    name VARCHAR(50),
    city VARCHAR(50),
    state VARCHAR(50)
);

CREATE TABLE flight_table (
    flight_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(8),
    flight_date TIMESTAMP, 
    departure_airport VARCHAR(3),
    arrival_airport VARCHAR(3),
    flight_price NUMERIC(10, 2), 
    total_seats INTEGER,
    occupied_seats INTEGER,
    flight_status VARCHAR(25),
    airport_id UUID NOT NULL, -- foreign key
    FOREIGN KEY (airport_id) REFERENCES airport_table (airport_id) 
);
