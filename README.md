# Restaurante v2.0

## Introdução

Este repositório contém um projeto feito na disciplina Programação Orientada a Objetos, do curso Técnico em Informática da Univates, e que foi e ainda está sendo atualizado com os conhecimentos obtidos na disciplina Programação Avançada.

Através dos meus conhecimentos em Java, Orientação a Objetos, Java Swing, padrão MVC e PostgreSQL, criei um programa de cadastro de clientes, itens de cardápio, reservas, pedidos e itens de pedido de um restaurante, assim como opções para listagem/busca de cada cadastrado, e suporte a edição e exclusão de dados do sistema.

Um destaque para este projeto: este é o primeiro (postado no GitHub) que contém a interface gráfica construída com Java Swing.

Agradecido pela atenção e espero que goste do projeto, e do que ele ainda irá se tornar🎉👀

## Tecnologias utilizadas

- Java 21
- Java Swing (interface gráfica)
- PostgreSQL
- Maven Java
- Padrão MVC (Model-View-Controller)

## Estrutura do projeto

```
Restaurante/
├── README.md
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   ├── main/         # Classe principal (ponto de entrada)
        │   ├── database/     # Conexão com o PostgreSQL (banco de dados)
        │   ├── models/       # Entidades (Cliente, ItemCardapio, Reserva, Pedido, ItemPedido)
        │   ├── controllers/  # Regras de negócio e acesso ao banco
        │   └── views/        # Telas em Java Swing (JFrame e JDialog)
        └── resources/
            └── sql/
                ├── creates.sql  # Criação das tabelas
                └── inserts.sql  # Dados de teste
```

## Funcionalidades

- Cadastro, listagem/busca, edição e exclusão de **Clientes**
- Cadastro, listagem/busca, edição e exclusão de **Itens de Cardápio**
- Cadastro, listagem/busca, edição e exclusão de **Reservas**
- Cadastro, listagem/busca, edição e exclusão de **Pedidos**
- Cadastro, listagem/busca, edição e exclusão de **Itens de Pedido**

## Detalhe importante⚠️ | Como rodar o programa e requisitos de execução

### Requisitos

- **Java 21** instalado na sua máquina
- **PostgreSQL** instalado e em execução
- Uma IDE Java — de preferência **Apache NetBeans IDE** (por conta do Java Swing), mas também funciona com Inteliij IDEA, Eclipse ou VS Code (com extensões Java instaladas)

### Passo a passo

1. Baixe este projeto (download .zip) e salve no seu dispositivo local
2. Crie um banco de dados chamado `restaurante` no PostgreSQL
3. Rode o script `src/main/resources/sql/creates.sql` para criar as tabelas
4. (Opcional) Rode o script `src/main/resources/sql/inserts.sql` para popular o banco com dados de teste
5. Abra o arquivo `src/main/java/database/ConexaoBanco.java` e ajuste `USUARIO` e `SENHA` conforme a configuração do seu PostgreSQL local
6. Abra o projeto na sua IDE de preferência e execute a classe `src/main/java/main/Restaurante.java`