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
import java.util.ArrayList;
import java.util.List;
import models.Cliente;
import org.apache.commons.validator.GenericValidator;

/**
 *
 * @author Guilherme Luvison
 */
public class ClienteController {

    // Atributos
    private String url = "jdbc:postgresql://localhost:5432/restaurante";
    private String usuario = "postgres";
    private String senha = "postgres";

    // Método de Cadastro
    public void cadastrar(String nome, String cpf, String telefone, String email, String dataNascimento) throws SQLException {
        validarNome(nome);
        validarCpf(cpf);
        validarDataNascimento(dataNascimento);

        String sql = "INSERT INTO clientes (nome, cpf, telefone, email, data_nascimento) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, cpf);
            pstmt.setString(3, telefone);
            pstmt.setString(4, email);
            pstmt.setString(5, dataNascimento);
            pstmt.executeUpdate();
        }
    }

    // Método de Listagem
    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT codigo, nome, cpf, telefone, email, data_nascimento, data_cadastro FROM clientes ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }
        return clientes;
    }

    // Método de Busca
    public List<Cliente> buscarPorNome(String termo) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT codigo, nome, cpf, telefone, email, data_nascimento, data_cadastro FROM clientes WHERE nome ILIKE ? ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearCliente(rs));
                }
            }
        }
        return clientes;
    }

    // Método de Atualização
    public void atualizar(int codigo, String novoNome, String novoTelefone, String novoEmail) throws SQLException {
        validarNome(novoNome);

        String sql = "UPDATE clientes SET nome = ?, telefone = ?, email = ? WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, novoNome);
            pstmt.setString(2, novoTelefone);
            pstmt.setString(3, novoEmail);
            pstmt.setInt(4, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhum cliente encontrado com o Código " + codigo + ".");
            }
        }
    }

    // Método de Remoção
    public void remover(int codigo) throws SQLException {
        String sql = "DELETE FROM clientes WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhum cliente encontrado com o Código " + codigo + ".");
            }
        }
    }

    // Métodos de Validação
    private void validarNome(String nome) {
        if (GenericValidator.isBlankOrNull(nome)) {
            throw new IllegalArgumentException("Nome inválido: não pode ficar em branco! Tente novamente.");
        }
    }

    private void validarCpf(String cpf) {
        if (GenericValidator.isBlankOrNull(cpf)) {
            throw new IllegalArgumentException("CPF inválido: não pode ficar em branco! Tente novamente.");
        }
    }

    private void validarDataNascimento(String dataNascimento) {
        if (GenericValidator.isBlankOrNull(dataNascimento)) {
            throw new IllegalArgumentException("Data de Nascimento inválida: não pode ficar em branco! Tente novamente.");
        }
    }

    /// Método de Mapeamento
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setCodigo(rs.getInt("codigo"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setDataNascimento(rs.getString("data_nascimento"));
        c.setDataCadastro(rs.getString("data_cadastro"));
        return c;
    }
}
