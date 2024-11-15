CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE
    IF NOT EXISTS booking_query_table (
        booking_id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
        booking_command_id UUID NOT NULL,
        booking_code VARCHAR(6) NOT NULL,
        flight_code VARCHAR(8) NOT NULL,
        booking_date TIMESTAMP WITH TIME ZONE NOT NULL,
        status_command_id UUID NOT NULL,
        status_code INTEGER NOT NULL,
        status_acronym VARCHAR(3) NOT NULL,
        status_description VARCHAR(15) NOT NULL,
        money_spent NUMERIC(10, 2) NOT NULL,
        miles_spent INTEGER NOT NULL,
        seats_number INTEGER NOT NULL,
        user_id VARCHAR(24) NOT NULL,
        transaction_id UUID NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS change_query_table (
        change_id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
        command_change_id UUID NOT NULL,
        change_date TIMESTAMP WITH TIME ZONE NOT NULL,
        booking_code VARCHAR(6) NOT NULL,
        i_status_command_id UUID NOT NULL,
        i_status_code INTEGER NOT NULL,
        i_status_acronym VARCHAR(3) NOT NULL,
        i_status_description VARCHAR(15) NOT NULL,
        f_status_command_id UUID NOT NULL,
        f_status_code INTEGER NOT NULL,
        f_status_acronym VARCHAR(3) NOT NULL,
        f_status_description VARCHAR(15) NOT NULL
    );