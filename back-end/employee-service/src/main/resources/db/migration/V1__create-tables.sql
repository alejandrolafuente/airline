CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS employee_table (
        employee_id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
        user_id VARCHAR(24) NOT NULL UNIQUE,
        name VARCHAR(50) NOT NULL,
        cpf VARCHAR(11) NOT NULL UNIQUE,
        email VARCHAR(50) NOT NULL UNIQUE,
        phone_number VARCHAR(11) NOT NULL UNIQUE,
        status VARCHAR(8) NOT NULL
    );