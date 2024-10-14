CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE
    IF NOT EXISTS client_table (
        client_id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
        user_id VARCHAR(24) NOT NULL UNIQUE, --e.g. "66e09f6ce9f42c7ea80ec9b6"
        cpf VARCHAR(11) NOT NULL UNIQUE,
        name VARCHAR(50) NOT NULL, --e.g "Carlos Gonçalves"
        email VARCHAR(50) NOT NULL UNIQUE,
        address_type VARCHAR(60) NOT NULL, -- e.g. "Rua Carlos Cadamuro"
        number VARCHAR(20) NOT NULL, -- e.g "645"
        complement VARCHAR(60), --e.g "Casa 3"
        cep VARCHAR(8) NOT NULL,
        city VARCHAR(50) NOT NULL, -- e.g "São Paulo"
        state VARCHAR(2) NOT NULL, -- e.g "SP"
        miles INT NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS transaction_table (
        transaction_id UUID PRIMARY KEY DEFAULT uuid_generate_v4 (),
        client_id UUID NOT NULL,
        transaction_date TIMESTAMP WITH TIME ZONE NOT NULL,
        money_value NUMERIC(10, 2),
        miles_quantity INT NOT NULL,
        transaction_type VARCHAR(6) NOT NULL,
        description VARCHAR(25) NOT NULL,
        FOREIGN KEY (client_id) REFERENCES client_table (client_id)
    );