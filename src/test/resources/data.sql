INSERT INTO sber_users(name)
    VALUES ('Andrey');
INSERT INTO sber_users(name)
    VALUES ('Sergey');
INSERT INTO sber_users(name)
    VALUES ('Anton');
INSERT INTO sber_users(name)
    VALUES ('Zaur');
INSERT INTO sber_users(name)
    VALUES ('Marsel');

INSERT INTO sber_account(account_number, owner_id)
    VALUES ('4356 3245 1234 7345', 5);
INSERT INTO sber_account(account_number, owner_id)
    VALUES ('4356 3834 1234 2855', 1);
INSERT INTO sber_account(account_number, owner_id)
    VALUES ('7356 9264 7634 2534', 3);
INSERT INTO sber_account(account_number, owner_id)
    VALUES ('2054 6334 2299 8376', 2);
INSERT INTO sber_account(account_number, owner_id)
    VALUES ('9934 9264 3456 4423', 4);

INSERT INTO sber_cards(card_number, account_id, owner_id)
    VALUES ('4356 3245 1234 7345', 1, 5);
INSERT INTO sber_account(account_number, owner_id)
    VALUES ('4356 3834 1234 2855', 2, 1);
INSERT INTO sber_account(account_number, owner_id)
    VALUES ('7356 9264 7634 2534', 3, 3);
INSERT INTO sber_account(account_number, owner_id)
    VALUES ('2054 6334 2299 8376', 4, 2);
INSERT INTO sber_account(account_number, owner_id)
    VALUES ('9934 9264 3456 4423', 5, 4);
