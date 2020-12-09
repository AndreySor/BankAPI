DROP TABLE IF EXISTS sber_cards;

DROP TABLE IF EXISTS sber_accounts;

DROP TABLE IF EXISTS sber_users;


CREATE TABLE IF NOT EXISTS sber_users (
    user_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS sber_accounts (
    account_id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(30) UNIQUE,
    balance DECIMAL(15,2),
    owner_id BIGINT REFERENCES sber_users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sber_cards (
    card_id BIGSERIAL PRIMARY KEY,
    card_number VARCHAR(30) UNIQUE,
    account_id BIGINT REFERENCES sber_accounts(account_id) ON DELETE CASCADE,
    owner_id BIGINT REFERENCES sber_users(user_id) ON DELETE CASCADE
);
