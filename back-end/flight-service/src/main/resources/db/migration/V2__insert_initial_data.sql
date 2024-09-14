INSERT INTO
    airport_table (airport_id, code, name, city, state)
VALUES
    (
        uuid_generate_v4 (),
        'CWB',
        'Aeroporto Afonso Pena',
        'Curitiba',
        'PR'
    );

INSERT INTO
    airport_table (airport_id, code, name, city, state)
VALUES
    (
        uuid_generate_v4 (),
        'GRU',
        'Aeroporto de Guarulhos',
        'São Paulo',
        'SP'
    );

-- departure from ctba to são paulo
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
        'FL1234',
        '2024-09-15 10:00:00',
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