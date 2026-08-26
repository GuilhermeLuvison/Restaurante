-- CREATE TABLE
CREATE TABLE clientes (
    codigo SERIAL NOT NULL PRIMARY KEY,
    nome VARCHAR(255),
    cpf VARCHAR(15),
    telefone VARCHAR(20),
    email VARCHAR(255),
    data_nascimento VARCHAR(10),
    data_cadastro DATE DEFAULT CURRENT_DATE
);

CREATE TABLE itenscardapio (
    codigo SERIAL NOT NULL PRIMARY KEY,
    nome VARCHAR(255),
    ingredientes VARCHAR(255),
    categoria VARCHAR(255),
    tipo_prato VARCHAR(255),
    preco DECIMAL(10, 2),
    tempo_preparo VARCHAR(255)
);

CREATE TABLE reservas (
    codigo SERIAL NOT NULL PRIMARY KEY,
    nome_cliente VARCHAR(255),
    mesa INT,
    quantidade_pessoas INT,
    observacao VARCHAR(255),
    data_reserva VARCHAR(10),
    status VARCHAR(255)
);