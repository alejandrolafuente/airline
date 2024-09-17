-- CWB -> GRU
INSERT INTO
    flight_table (
        flight_id,
        code,
        flight_date,
        departure_airport_id,
        arrival_airport_id,
        flight_price,
        total_seats,
        occupied_seats,
        flight_status
    )
VALUES
    (
        uuid_generate_v4 (),
        'TADS2568',
        '2024-09-17T23:00:00+00:00', -- Usando o formato ISO 8601 com fuso horário
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'CWB'
        ),
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'GRU'
        ),
        350.00,
        180,
        120,
        'CONFIRMED'
    );

-- GRU -> CWB
INSERT INTO
    flight_table (
        flight_id,
        code,
        flight_date,
        departure_airport_id,
        arrival_airport_id,
        flight_price,
        total_seats,
        occupied_seats,
        flight_status
    )
VALUES
    (
        uuid_generate_v4 (),
        'TADS2569',
        '2024-09-18T14:00:00+00:00', -- Usando o formato ISO 8601 com fuso horário
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'GRU'
        ),
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'CWB'
        ),
        320.00,
        180,
        110,
        'CONFIRMED'
    );

-- BSB -> GIG
INSERT INTO
    flight_table (
        flight_id,
        code,
        flight_date,
        departure_airport_id,
        arrival_airport_id,
        flight_price,
        total_seats,
        occupied_seats,
        flight_status
    )
VALUES
    (
        uuid_generate_v4 (),
        'TADS2570',
        '2024-09-18T15:00:00+00:00', -- Usando o formato ISO 8601 com fuso horário
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'BSB'
        ),
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'GIG'
        ),
        450.00,
        200,
        150,
        'CONFIRMED'
    );

-- GIG -> BSB
INSERT INTO
    flight_table (
        flight_id,
        code,
        flight_date,
        departure_airport_id,
        arrival_airport_id,
        flight_price,
        total_seats,
        occupied_seats,
        flight_status
    )
VALUES
    (
        uuid_generate_v4 (),
        'TADS2571',
        '2024-09-19T12:00:00+00:00', -- Usando o formato ISO 8601 com fuso horário
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'GIG'
        ),
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'BSB'
        ),
        460.00,
        200,
        140,
        'CONFIRMED'
    );

-- GRU -> BSB
INSERT INTO
    flight_table (
        flight_id,
        code,
        flight_date,
        departure_airport_id,
        arrival_airport_id,
        flight_price,
        total_seats,
        occupied_seats,
        flight_status
    )
VALUES
    (
        uuid_generate_v4 (),
        'TADS2572',
        '2024-09-19T15:00:00+00:00', -- Usando o formato ISO 8601 com fuso horário
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'GRU'
        ),
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'BSB'
        ),
        480.00,
        180,
        160,
        'CONFIRMED'
    );

-- BSB -> GRU
INSERT INTO
    flight_table (
        flight_id,
        code,
        flight_date,
        departure_airport_id,
        arrival_airport_id,
        flight_price,
        total_seats,
        occupied_seats,
        flight_status
    )
VALUES
    (
        uuid_generate_v4 (),
        'TADS2573',
        '2024-09-20T08:00:00+00:00', -- Usando o formato ISO 8601 com fuso horário
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'BSB'
        ),
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'GRU'
        ),
        490.00,
        180,
        165,
        'CONFIRMED'
    );

-- CWB -> GIG
INSERT INTO
    flight_table (
        flight_id,
        code,
        flight_date,
        departure_airport_id,
        arrival_airport_id,
        flight_price,
        total_seats,
        occupied_seats,
        flight_status
    )
VALUES
    (
        uuid_generate_v4 (),
        'TADS2574',
        '2024-09-20T10:00:00+00:00', -- Usando o formato ISO 8601 com fuso horário
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'CWB'
        ),
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'GIG'
        ),
        470.00,
        170,
        130,
        'CONFIRMED'
    );

-- GIG -> CWB
INSERT INTO
    flight_table (
        flight_id,
        code,
        flight_date,
        departure_airport_id,
        arrival_airport_id,
        flight_price,
        total_seats,
        occupied_seats,
        flight_status
    )
VALUES
    (
        uuid_generate_v4 (),
        'TADS2575',
        '2024-09-20T14:00:00+00:00', -- Usando o formato ISO 8601 com fuso horário
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'GIG'
        ),
        (
            SELECT
                airport_id
            FROM
                airport_table
            WHERE
                code = 'CWB'
        ),
        460.00,
        170,
        140,
        'CONFIRMED'
    );