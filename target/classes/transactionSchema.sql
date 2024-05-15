DROP TABLE IF EXISTS transaction;

CREATE TABLE transaction (
    transaction_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    account_id INT NOT NULL,
    vendor_name VARCHAR(100) NOT NULL,
    transaction_date DATE NOT NULL,
    transaction_amount DECIMAL(10, 2) NOT NULL,
    transaction_description VARCHAR(500),
    transaction_category VARCHAR(50) NOT NULL,
)