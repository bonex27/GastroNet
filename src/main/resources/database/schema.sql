
DROP TABLE IF EXISTS Customers;
drop table  if exists  attendant;

-- Creazione tabella Ingrediente
CREATE TABLE IF NOT EXISTS Ingrediente (
                                           id INTEGER PRIMARY KEY AUTOINCREMENT,
                                           nome TEXT NOT NULL,
                                           disponibile INTEGER NOT NULL,
                                           impegnato INTEGER NOT NULL
);

-- Creazione tabella Categoria
CREATE TABLE IF NOT EXISTS Categoria (
                                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                                         nome TEXT NOT NULL
);

-- Creazione tabella prodotto
CREATE TABLE IF NOT EXISTS Prodotto (
                                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                                        nome TEXT NOT NULL,
                                        prezzo REAL NOT NULL,
                                        categoria_id INTEGER,
                                        FOREIGN KEY (categoria_id) REFERENCES Categoria(id)
);

-- Creazione tabella Prodotto_Ingrediente (relazione molti-a-molti)
CREATE TABLE IF NOT EXISTS Prodotto_Ingrediente (
                                                    prodotto_id INTEGER,
                                                    ingrediente_id INTEGER,
                                                    quantita INTEGER NOT NULL,
                                                    FOREIGN KEY (prodotto_id) REFERENCES Prodotto(id),
                                                    FOREIGN KEY (ingrediente_id) REFERENCES Ingrediente(id),
                                                    PRIMARY KEY (prodotto_id, ingrediente_id)
);

-- Creazione tabella domainModel.Order
CREATE TABLE IF NOT EXISTS Ordine (
                                      id INTEGER PRIMARY KEY AUTOINCREMENT,
                                      cliente_id INTEGER NOT NULL,
                                      stato TEXT NOT NULL,
                                      FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
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