INSERT INTO customers(social_security_number, first_name, last_name, email, address, phone_number)
VALUES
    ('19850101-1234', 'Anna', 'Svensson',
     'anna.svensson@outlook.se', 'Svenssongatan 85', null),
    ('19900215-5678', 'Erik', 'Johansson',
     'erikjohan90@gmail.com', 'Schrödingergatan 1', null),
    ('19751230-9101', 'Maria', 'Lindberg',
     'maria.lindberg75@hotmail.com', 'Schrödingergatan 4', null),
    ('19881122-3456', 'Johan', 'Karlsson',
     'johankarl1988@yahoo.com', 'Karlssonsgatan 19', '0716866556'),
    ('19950505-7890', 'Elin', 'Andersson',
     'elin.andersson1@outlook.se', 'Anderssongatan 5', '0737878788');

INSERT INTO cars(price_per_day, brand, model, plate_number, in_service)
VALUES
    (661.5, 'Renault', 'Captur', 'MLB84A', false),
    (990.0, 'Peugeot', '3008', 'ABC123', false),
    (1365.0, 'Volvo', 'V90 Plug in', 'YXH32E', false),
    (1138.5, 'Volvo', 'XC40', 'PRE12B', true),
    (1260.0, 'Volvo', 'V60 Plug in', 'FCV91H', false);

INSERT INTO orders (price, start_date, end_date, active, customer_id, car_id)
VALUES
    /* ACTIVE ORDERS */
    (1323, '2025-01-01', '2025-01-03', true, 1, 1),
    (4950, '2025-02-15', '2025-02-20', true, 2, 2),
    (6825, '2025-03-10', '2025-03-15', true, 3, 3),
    (7969, '2002-03-10', '2026-03-17', true, 4, 4),
    (6300, '2025-04-15', '2026-04-20', true, 5, 5),
    /* INACTIVE ORDERS */
    (2970, '2020-05-01', '2020-05-04', false, 1, 2),
    (2730, '2021-06-15', '2021-06-17', false, 2, 3),
    (5692, '2022-07-10', '2022-07-15', false, 3, 4),
    (6300, '2023-10-10', '2023-10-15', false, 4, 5),
    (3307, '2024-11-15', '2024-11-20', false, 5, 1);
