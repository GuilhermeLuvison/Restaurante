-- Clientes (7 registros)
INSERT INTO clientes (nome, cpf, telefone, email, data_nascimento)
VALUES
    ('Ana Beatriz Souza', '12345678901', '+55 51 9 9123-4567', 'ana.souza@email.com', '1995-03-14'),
    ('Carlos Eduardo Lima', '23456789012', '+55 51 9 9234-5678', 'carlos.lima@email.com', '1988-07-22'),
    ('Fernanda Oliveira', '34567890123', '+55 51 9 9345-6789', 'fernanda.oliveira@email.com', '2000-11-05'),
    ('Guilherme Alves', '45678901234', '+55 51 9 9456-7890', 'guilherme.alves@email.com', '1992-01-30'),
    ('Juliana Pereira', '56789012345', '+55 51 9 9567-8901', 'juliana.pereira@email.com', '1998-09-18'),
    ('Marcos Vinicius Costa', '67890123456', '+55 51 9 9678-9012', 'marcos.costa@email.com', '1985-05-09'),
    ('Patricia Santos', '78901234567', '+55 51 9 9789-0123', 'patricia.santos@email.com', '2003-12-25');

-- Itens de Cardápio (7 registros)
INSERT INTO itenscardapio (nome, ingredientes, categoria, tipo_prato, preco, tempo_preparo)
VALUES
    ('Bruschetta Tradicional', 'Pão italiano, tomate, alho, manjericão, azeite', 'Entradas', 'Entrada', 22.00, '15 minutos'),
    ('Carpaccio de Carne', 'Carne bovina, rúcula, parmesão, alcaparras', 'Entradas', 'Entrada', 35.00, '10 minutos'),
    ('Risoto de Camarão', 'Arroz arbóreo, camarão, vinho branco, parmesão', 'Massas', 'Prato Principal', 68.00, '30 minutos'),
    ('Filé à Parmegiana', 'Filé mignon, molho de tomate, queijo, batata frita', 'Carnes', 'Prato Principal', 72.00, '35 minutos'),
    ('Salmão Grelhado', 'Salmão, legumes salteados, purê de batata', 'Peixes', 'Prato Principal', 65.00, '25 minutos'),
    ('Petit Gateau', 'Chocolate, sorvete de creme, calda de morango', 'Sobremesas', 'Sobremesa', 28.00, '20 minutos'),
    ('Tiramisu', 'Café, mascarpone, biscoito champagne, cacau', 'Sobremesas', 'Sobremesa', 26.00, '15 minutos');

-- Reservas (5 registros)
INSERT INTO reservas (codigo_cliente, mesa, quantidade_pessoas, observacao, data_reserva, status)
VALUES
    (1, 4, 2, 'Mesa perto da janela, se possível', '2026-10-02', 'Confirmada'),
    (2, 7, 4, NULL, '2026-10-03', 'Confirmada'),
    (3, 1, 2, 'Aniversário de casamento', '2026-10-05', 'Pendente'),
    (5, 10, 6, 'Comemoração em grupo', '2026-10-06', 'Confirmada'),
    (6, 3, 3, NULL, '2026-09-30', 'Cancelada');

-- Pedidos (5 registros)
INSERT INTO pedidos (codigo_cliente, codigo_reserva, mesa, quantidade_pessoas, momento_pedido, status)
VALUES
    (1, 1, 4, 2, '2026-10-02 19:30:00', 'Entregue'),
    (2, 2, 7, 4, '2026-10-03 20:15:00', 'Em preparo'),
    (4, NULL, 5, 1, '2026-09-28 12:45:00', 'Entregue'),
    (5, 4, 10, 6, '2026-10-06 21:00:00', 'Em preparo'),
    (7, NULL, 2, 2, '2026-09-28 13:10:00', 'Cancelado');

-- Itens de Pedido (3 registros)
INSERT INTO itenspedido (codigo_pedido, codigo_itenscardapio, quantidade, preco_unitario, observacao)
VALUES
    (1, 3, 2, 68.00, 'Sem cebola'),
    (1, 6, 2, 28.00, NULL),
    (3, 4, 1, 72.00, 'Ponto da carne: bem passado');