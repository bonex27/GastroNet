
DROP TABLE IF EXISTS Customers;
DROP TABLE   if exists  attendant;
DROP TABLE   if exists  Products;
DROP TABLE   if exists  Category;
DROP TABLE   if exists  "Order";




-- Creazione tabella Categoria
CREATE TABLE IF NOT EXISTS Category (
                                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                                         description TEXT NOT NULL
);

-- Creazione tabella prodotto
CREATE TABLE IF NOT EXISTS Products (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        name TEXT NOT NULL,
                                        description TEXT NOT NULL,
                                        price REAL NOT NULL,
                                        id_category INTEGER,
                                        stock INTEGER,
                                        FOREIGN KEY (id_category) REFERENCES Category(id)
);




-- Creazione tabella Cliente
CREATE TABLE IF NOT EXISTS Customers (
                                       fiscal_code TEXT PRIMARY KEY,
                                       name TEXT NOT NULL,
                                       surname TEXT NOT NULL,
                                       payment_method TEXT not null
);

-- Table: Attendant
CREATE TABLE IF NOT EXISTS attendant
(
    fiscal_code TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    iban        TEXT NOT NULL
);

-- Creazione tabella domainModel.Order
CREATE TABLE IF NOT EXISTS "Order" (
                                     id INTEGER PRIMARY KEY AUTOINCREMENT,
                                     cliente_id INTEGER NOT NULL,
                                     stato TEXT NOT NULL,
                                     FOREIGN KEY (cliente_id) REFERENCES Customers(fiscal_code)
);