INSERT INTO customer(social_security_number, first_name, last_name, email, address, phone_number)
VALUES
    ('19850101-1234', 'Anna', 'Svensson',
     'anna.svensson@outlook.se', 'Svenssongatan 85', null),
    ('19900215-5678', 'Erik', 'Johansson',
     'erikjohan90@gmail.com', 'Schrödingergatn 1', null),
    ('19751230-9101', 'Maria', 'Lindberg',
     'maria.lindberg75@hotmail.com', 'Schrödingergatan 4', null),
    ('19881122-3456', 'Johan', 'Karlsson',
     'johankarl1988@yahoo.com', 'Karlssonsgatan 19', '0716866556'),
    ('19950505-7890', 'Elin', 'Andersson',
     'elin.andersson1@outlook.se', 'Anderssongatan 5', '0737878788');

INSERT INTO car(price_per_day, brand, model, plate_number, booked, in_service)
VALUES
    (661.5, 'Renault', 'Captur', 'MLB84A', true, false),
    (990.0, 'Peugeot', '3008', 'ABC123', false, true),
    (1365.0, 'Volvo', 'V90 Plug in', 'YXH32E', false, false),
    (1138.5, 'Volvo', 'XC40', 'PRE12B', true, false),
    (1260.0, 'Volvo', 'V60 Plug in', 'FCV91H', false, false);