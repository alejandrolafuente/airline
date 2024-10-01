INSERT INTO
    client_table (
        user_id,
        cpf,
        name,
        email,
        address_type,
        number,
        complement,
        cep,
        city,
        state,
        miles
    )
VALUES
    (
        '66e09f6ce9f42c7ea80ec9b6',
        '53947582048',
        'Carlos Gonçalves',
        'carlosgoncalves@example.com',
        'Rua Carlos Cadamuro',
        '645',
        'Casa 3',
        '01001000',
        'São Paulo',
        'SP',
        500
    ),
    (
        '77a19f7cf9e42d7bb90dc8a7',
        '75086334071',
        'Mariana Souza',
        'marianasouza@example.com',
        'Avenida Paulista',
        '1500',
        NULL,
        '01311000',
        'São Paulo',
        'SP',
        0
    ),
    (
        '88b29f8dfae42e8cc01ec9b8',
        '81077957092',
        'João Pereira',
        'joaopereira@example.com',
        'Rua dos Pinheiros',
        '720',
        'Apto 101',
        '05422020',
        'São Paulo',
        'SP',
        0
    ),
    (
        '99c39f9dfa342f9dd12fd9b9',
        '41937009092',
        'Ana Lima',
        'analima@example.com',
        'Avenida Brasil',
        '1200',
        NULL,
        '22290030',
        'Rio de Janeiro',
        'RJ',
        0
    );

INSERT INTO
    transaction_table (
        client_id,
        transaction_date,
        miles_quantity,
        money_input,
        miles_output,
        t_description
    )
VALUES
    (
        (
            SELECT
                client_id
            FROM
                client_table
            WHERE
                user_id = '66e09f6ce9f42c7ea80ec9b6'
        ), -- Primeiro cliente
        '2024-09-25T10:30:00Z',
        100, -- 100 milhas compradas
        500.00, -- 5 reais por milha
        100, -- 100 milhas saíram da conta
        'MILES PURCHASING'
    ),
    (
        (
            SELECT
                client_id
            FROM
                client_table
            WHERE
                user_id = '66e09f6ce9f42c7ea80ec9b6'
        ),
        '2024-09-26T14:45:00Z',
        200, -- 200 milhas compradas
        1000.00,
        200,
        'MILES PURCHASING'
    ),
    (
        (
            SELECT
                client_id
            FROM
                client_table
            WHERE
                user_id = '66e09f6ce9f42c7ea80ec9b6'
        ),
        '2024-09-27T09:15:00Z',
        50, -- 50 milhas compradas
        250.00,
        50,
        'MILES PURCHASING'
    ),
    (
        (
            SELECT
                client_id
            FROM
                client_table
            WHERE
                user_id = '66e09f6ce9f42c7ea80ec9b6'
        ),
        '2024-09-28T16:00:00Z',
        150, -- 150 milhas compradas
        750.00,
        150,
        'MILES PURCHASING'
    );