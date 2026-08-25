/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Cliente;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.EmailValidator;
import resources.Entrada;

/**
 *
 * @author Guilherme Luvison
 */
public class ClienteController {

    // Atributos
    String url = "jdbc:postgresql://localhost:5432/restaurante";
    String usuario = "postgres";
    String senha = "postgres";

    // Método de Cadastro
    public void cadastrar() {
        String nome;
        do {
            nome = Entrada.leiaString("Nome do Cliente:");
            if (GenericValidator.isBlankOrNull(nome)) {
                System.out.println("Nome inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(nome));

        String cpf;
        do {
            cpf = Entrada.leiaString("CPF do Cliente:");
            if (!EmailValidator.getInstance().isValid(cpf)) {
                System.out.println("CPF inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (!EmailValidator.getInstance().isValid(cpf));

        String telefone = Entrada.leiaString("Telefone do Cliente (Opcional):");
        String email = Entrada.leiaString("Email do Cliente (Opcional):");

        String dataNascimento;
        do {
            dataNascimento = Entrada.leiaString("Data de Nascimento do Cliente:");
            if (!EmailValidator.getInstance().isValid(dataNascimento)) {
                System.out.println("Data de Nascimento inválida: não pode ficar em branco! Tente novamente.");
            }
        } while (!EmailValidator.getInstance().isValid(dataNascimento));

        String sql = "INSERT INTO clientes (nome, cpf, telefone, email, data_nascimento) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, cpf);
            pstmt.setString(3, telefone);
            pstmt.setString(4, email);
            pstmt.setString(5, dataNascimento);
            pstmt.executeUpdate();
            System.out.println("Cliente cadastrado com sucesso!");
            System.out.println("");
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Listagem
    public void listar() {
        String sql = "SELECT oodigo, nome, cpf, telefone, email, data_nascimento, data_cadastro FROM clientes ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            System.out.println("===[CLIENTES CADASTRADOS]===");
            while (rs.next()) {
                imprimeCliente(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Busca
    public void buscarPorNome() {
        String termo = Entrada.leiaString("Digite o nome (ou parte dele) para buscar:");
        String sql = "SELECT codigo, nome, cpf, telefone, email, data_nascimento, data_cadastro FROM clientes WHERE nome ILIKE ? ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean encontrouAlgum = false;
                System.out.println("===[CLIENTES ENCONTRADOS]===");
                while (rs.next()) {
                    imprimeCliente(rs);
                    encontrouAlgum = true;
                }
                if (!encontrouAlgum) {
                    System.out.println("Nenhum cliente encontrado com esse nome.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Atualização
    public void atualizar() {
        int codigo = Entrada.leiaInt("Digite o Código do cliente que deseja atualizar:");

        String novoNome;
        do {
            novoNome = Entrada.leiaString("Novo nome:");
            if (!EmailValidator.getInstance().isValid(novoNome)) {
                System.out.println("Nome inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (!EmailValidator.getInstance().isValid(novoNome));

        String novoTelefone = Entrada.leiaString("Novo telefone (Repita caso não queira atualizar/adicionar):");
        String novoEmail = Entrada.leiaString("Novo email (Repita caso não queira atualizar/adicionar):");

        String sql = "UPDATE clientes SET nome = ?, telefone = ?, email = ? WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, novoNome);
            pstmt.setString(2, novoTelefone);
            pstmt.setString(3, novoEmail);
            pstmt.setInt(4, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Dados atualizados com sucesso!");
                System.out.println("");
            } else {
                System.out.println("Nenhum cliente encontrado com o Código " + codigo + ".");
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Remoção
    public void remover() {
        int codigo = Entrada.leiaInt("Digite o Código do cliente que deseja remover:");
        boolean confirma = Entrada.leiaBoolean("Tem certeza que deseja remover o cliente de Código " + codigo + "?");

        if (!confirma) {
            System.out.println("Remoção cancelada.");
            return;
        }

        String sql = "DELETE FROM clientes WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Cliente removido com sucesso!");
                System.out.println("");
            } else {
                System.out.println("Nenhum cliente encontrado com o Código " + codigo + ".");
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Impressão de Cliente Específico
    private void imprimeCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setCodigo(rs.getInt("codigo"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setDataNascimento(rs.getString("dataNascimento"));
        c.setDataCadastro(rs.getString("dataCadastro"));
        c.imprimeAtributos();
    }
}
