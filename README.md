# Restaurante v3.0

## Introdução

Este repositório contém um projeto feito na disciplina Programação Orientada a Objetos, do curso Técnico em Informática da Univates, e que foi e ainda está sendo atualizado com os conhecimentos obtidos na disciplina Programação Avançada.

Através dos meus conhecimentos em Java, Orientação a Objetos, Java Swing, padrão MVC, padrão DAO e PostgreSQL, criei um programa de cadastro de clientes, itens de cardápio, reservas, pedidos e itens de pedido de um restaurante, assim como opções para listagem/busca de cada cadastrado, e suporte a visualização, edição e exclusão de dados do sistema.

Um destaque para este projeto: este é o primeiro (postado no GitHub) que contém a interface gráfica construída com Java Swing.

Agradecido pela atenção e espero que goste do projeto, e do que ele ainda irá se tornar🎉👀

## Tecnologias utilizadas

- Apache Commons Validator
- Apache Maven
- Java 21
- Java Swing (interface gráfica)
- JDBC (driver PostgreSQL)
- PostgreSQL
- Padrão MVC (Model-View-Controller)
- Padrão DAO (Data Access Object)

## Estrutura do projeto

```
Restaurante/
├── README.md
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   ├── controllers/  # Regras de negócio e validações
        │   ├── dao/          # Acesso ao banco (interfaces e implementações JDBC)
        │   ├── database/     # Conexão com o PostgreSQL (banco de dados)
        │   ├── main/         # Classe principal (ponto de entrada)
        │   ├── models/       # Entidades (Cliente, ItemCardapio, Reserva, Pedido, ItemPedido)
        │   ├── utils/        # Validação, conversão e formatação de campos
        │   └── views/        # Telas em Java Swing (JFrame e JDialog)
        └── resources/
            └── sql/
                ├── creates.sql  # Criação das tabelas
                └── inserts.sql  # Dados de teste
```

## Funcionalidades

- Cadastro, listagem/busca, visualização/edição e exclusão de **Clientes**
- Cadastro, listagem/busca, visualização/edição e exclusão de **Itens de Cardápio**
- Cadastro, listagem/busca, visualização/edição e exclusão de **Reservas**
- Cadastro, listagem/busca, visualização/edição e exclusão de **Pedidos**
- Cadastro, listagem/busca, visualização/edição e exclusão de **Itens de Pedido**

## Detalhe importante⚠️ | Como rodar o programa e requisitos de execução

### Requisitos

- **Java 21** instalado na sua máquina
- **PostgreSQL** instalado e em execução
- Uma IDE Java — de preferência **Apache NetBeans IDE** (por conta do Java Swing), mas também funciona com Inteliij IDEA, Eclipse ou VS Code (com extensões Java instaladas)

### Passo a passo

1. Baixe este projeto (download .zip) ou clone o repositório com `git clone`, e salve no seu dispositivo local
2. Crie um banco de dados chamado `restaurante` no PostgreSQL
3. Rode o script `src/main/resources/sql/creates.sql` para criar as tabelas
4. (Opcional) Rode o script `src/main/resources/sql/inserts.sql` para popular o banco com dados de teste
5. Abra o arquivo `src/main/java/database/ConexaoBanco.java` e ajuste `USUARIO` e `SENHA` conforme a configuração do seu PostgreSQL local
6. Abra o projeto na sua IDE de preferência e execute a classe `src/main/java/main/Restaurante.java`, ou então gere o executável (próxima seção) e rode o `.jar`

### Gerando o executável (.jar)

O projeto está configurado com o `maven-assembly-plugin` para gerar um **JAR executável com todas as dependências** (o driver do PostgreSQL e o Commons Validator ficam dentro do próprio arquivo).

1. Confirme que o Maven está instalado: `mvn -v`
2. Faça os passos 2 a 5 acima (banco de dados e `ConexaoBanco.java`) **antes** de gerar o JAR
3. Na pasta do projeto (onde está o `pom.xml`), rode:
   ```
   mvn clean package
   ```
4. O executável será criado em `target/`. Rode com:
   ```
   java -jar target/Restaurante-3.0-jar-with-dependencies.jar
   ```

> **Atenção:** o usuário e a senha do banco ficam escritos no código (`ConexaoBanco.java`). Se você trocar a senha depois de gerar o JAR, será preciso gerar o JAR novamente. O PostgreSQL também precisa estar em execução, com o banco `restaurante` criado, mesmo rodando fora da IDE.