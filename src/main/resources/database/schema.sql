
DROP TABLE IF EXISTS Customers;
DROP TABLE   if exists  ProductOrder;
DROP TABLE   if exists  Attendant;
DROP TABLE   if exists  Products;
DROP TABLE   if exists  Category;
DROP TABLE   if exists  "Orders";


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
CREATE TABLE IF NOT EXISTS Attendant
(
    fiscal_code TEXT PRIMARY KEY,
    name        TEXT NOT NULL,
    surname     TEXT NOT NULL,
    iban        TEXT NOT NULL
);

-- Creazione tabella domainModel.Order
CREATE TABLE IF NOT EXISTS "Orders" (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        customer_id TEXT NOT NULL,
                                        state TEXT NOT NULL,
                                        FOREIGN KEY (customer_id) REFERENCES Customers(fiscal_code)
);

CREATE TABLE IF NOT EXISTS ProductOrder (
                                            order_id INTEGER NOT NULL,
                                            product_id TEXT NOT NULL,
                                            primary key (order_id,product_id),
                                            FOREIGN KEY (order_id) references "Orders"(id),
                                            FOREIGN KEY (product_id) references Products(id)
);