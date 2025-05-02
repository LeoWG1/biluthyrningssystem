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
    (10, '2025-01-01', '2025-02-01', true, 1, 1),
    (10, '2025-02-15', '2025-03-05', true, 2, 2),
    (10, '2025-03-10', '2025-04-05', true, 3, 3),
    (10, '2002-03-10', '2002-04-05', false, 4, 4),
    (10, '2025-04-15', '2025-04-20', true, 5, 5);
