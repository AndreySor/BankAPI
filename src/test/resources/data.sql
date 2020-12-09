INSERT INTO sber_users(first_name, last_name) VALUES ('Andrey', 'Sidorov');
INSERT INTO sber_users(first_name, last_name) VALUES ('Sergey', 'Larin');
INSERT INTO sber_users(first_name, last_name) VALUES ('Anton', 'Zubov');
INSERT INTO sber_users(first_name, last_name) VALUES ('Zaur', 'Ivanov');
INSERT INTO sber_users(first_name, last_name) VALUES ('Marsel', 'Abramov');

INSERT INTO sber_accounts(account_number, owner_id, balance) VALUES ('4356 3245 1234 7345', 5, 245.30);
INSERT INTO sber_accounts(account_number, owner_id, balance) VALUES ('4356 3834 1234 2855', 1, 30045.23);
INSERT INTO sber_accounts(account_number, owner_id, balance) VALUES ('7356 9264 7634 2534', 3, 2000.00);
INSERT INTO sber_accounts(account_number, owner_id, balance) VALUES ('2054 6334 2299 8376', 2, 7043.54);
--INSERT INTO sber_accounts(account_number, owner_id, balance)
--    VALUES ('2545 6334 4595 8376', 2, 17043.54);
INSERT INTO sber_accounts(account_number, owner_id, balance) VALUES ('9934 9264 3456 4423', 4, 12900.40);

INSERT INTO sber_cards(card_number, account_id, owner_id) VALUES ('4356 3245 1234 7345', 1, 5);
INSERT INTO sber_cards(card_number, account_id, owner_id) VALUES ('4356 3834 1234 2855', 2, 1);
INSERT INTO sber_cards(card_number, account_id, owner_id) VALUES ('7356 9264 7634 2534', 3, 3);
INSERT INTO sber_cards(card_number, account_id, owner_id) VALUES ('2054 6334 2299 8376', 4, 2);
--INSERT INTO sber_cards(card_number, account_id, owner_id)
--    VALUES ('3333 4444 5555 6666', 4, 2);
INSERT INTO sber_cards(card_number, account_id, owner_id) VALUES ('9934 9264 3456 4423', 5, 4);
