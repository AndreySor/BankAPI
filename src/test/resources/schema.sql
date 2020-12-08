CREATE TABLE sber_users (
    user_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50),
);

CREATE TABLE sber_accounts (
    account_id BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(30),
    owner_id BIGINT REFERENCES sber_users(user_id)
)

CREATE TABLE sber_cards (
    card_id BIGSERIAL PRIMARY KEY,
    card_number VARCHAR(30),
    account_id BIGINT REFERENCES sber_accounts(account_id);
    owner_id BIGINT REFERENCES sber_users(user_id)
);
