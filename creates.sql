-- CREATE TABLE (Em uso)
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

-- CREATE TABLE (Será implementado)
CREATE TABLE clientes (
    codigo SERIAL NOT NULL,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(15) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(255),
    data_nascimento DATE NOT NULL,
    data_cadastro DATE DEFAULT CURRENT_DATE,
    CONSTRAINT PK_clientes PRIMARY KEY (codigo),
    CONSTRAINT UN_cpfclientes UNIQUE (cpf),
    CONSTRAINT CHK_data_nascimento CHECK (data_nascimento <= CURRENT_DATE - INTERVAL '18 YEARS')
);

CREATE TABLE itenscardapio (
    codigo SERIAL NOT NULL,
    nome VARCHAR(255) NOT NULL,
    ingredientes VARCHAR(255) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    tipo_prato VARCHAR(20) NOT NULL,
    preco DECIMAL(10, 2) NOT NULL,
    tempo_preparo VARCHAR(100) NOT NULL,
    CONSTRAINT PK_itenscardapio PRIMARY KEY (codigo),
    CONSTRAINT CHK_tipo_prato CHECK (tipo_prato = 'Entrada' OR tipo_prato = 'Prato Principal' OR tipo_prato = 'Sobremesa')
);

CREATE TABLE reservas (
    codigo SERIAL NOT NULL,
    codigo_cliente INT NOT NULL,
    mesa INT NOT NULL,
    quantidade_pessoas INT NOT NULL,
    observacao VARCHAR(255),
    data_reserva DATE NOT NULL,
    status VARCHAR(15) NOT NULL,
    CONSTRAINT PK_reservas PRIMARY KEY (codigo),
    CONSTRAINT FK_codigo_cliente_reservas FOREIGN KEY (codigo_cliente) REFERENCES clientes,
    CONSTRAINT CHK_status CHECK (status = 'Confirmada' OR status = 'Pendente' OR status = 'Cancelada')
);

CREATE TABLE pedidos (
    codigo SERIAL NOT NULL,
    codigo_cliente INT NOT NULL,
    codigo_reserva INT,
    mesa INT NOT NULL,
    quantidade_pessoas INT NOT NULL,
    momento_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(15),
    CONSTRAINT PK_pedidos PRIMARY KEY (codigo),
    CONSTRAINT FK_codigo_cliente_pedidos FOREIGN KEY (codigo_cliente) REFERENCES clientes ON DELETE RESTRICT, -- RESTRICT: Pedido ativo impede a exclusão de um Cliente
    CONSTRAINT FK_codigo_reserva_pedidos FOREIGN KEY (codigo_reserva) REFERENCES reservas ON DELETE SET NULL, -- SET NULL: Pedido continua existindo após a exclusão de uma Reserva
    CONSTRAINT CHK_status CHECK (status = 'Entregue' OR status = 'Em preparo' OR status = 'Cancelado')
);

CREATE TABLE itenspedido (
    codigo SERIAL NOT NULL,
    codigo_pedido INT NOT NULL,
    codigo_itenscardapio INT NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    preco_total DECIMAL(10, 2) GENERATED ALWAYS AS (quantidade * preco_unitario) STORED,
    observacao VARCHAR(255),
    CONSTRAINT PK_itenspedido PRIMARY KEY (codigo),
    CONSTRAINT FK_codigo_pedido_itenspedido FOREIGN KEY (codigo_pedido) REFERENCES pedidos,
    CONSTRAINT FK_codigo_itenscardapio_itenspedido FOREIGN KEY (codigo_itenscardapio) REFERENCES itenscardapio
);