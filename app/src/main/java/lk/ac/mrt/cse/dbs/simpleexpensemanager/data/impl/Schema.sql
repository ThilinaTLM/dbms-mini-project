CREATE TABLE account (
    account_no VARCHAR(50) PRIMARY KEY,
    bank_name VARCHAR(100) NOT NULL,
    account_holder_name VARCHAR(150) NOT NULL,
    balance DECIMAL(17, 2) UNSIGNED DEFAULT 0
);

CREATE TABLE transaction (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    date DATE NOT NULL,
    account_no VARCHAR(50) NOT NULL,
    expense_type VARCHAR(20) NOT NULL,
    amount DECIMAL(17, 2) UNSIGNED DEFAULT 0
);
