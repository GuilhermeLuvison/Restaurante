/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import database.ConexaoBanco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import models.Cliente;
import org.apache.commons.validator.GenericValidator;

/**
 *
 * @author Guilherme Luvison
 */
public class ClienteController {

    private static final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void cadastrar(String nome, String cpf, String telefone, String email, String dataNascimento) throws SQLException {
        validarNome(nome);
        cpf = formatarCpf(cpf);
        validarCpf(cpf);
        LocalDate dataNasc = validarDataNascimento(dataNascimento);

        String sql = "INSERT INTO clientes (nome, cpf, telefone, email, data_nascimento) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, cpf);
            pstmt.setString(3, telefone);
            pstmt.setString(4, email);
            pstmt.setDate(5, java.sql.Date.valueOf(dataNasc));
            pstmt.executeUpdate();
        } catch (SQLException e) { // Violação de Chave Única do PostgreSQL
            if ("23505".equals(e.getSQLState())) {
                throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF.");
            }
            throw e;
        }
    }

    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT codigo, nome, cpf, telefone, email, data_nascimento, data_cadastro FROM clientes ORDER BY codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        }
        return clientes;
    }

    public List<Cliente> buscarPorNome(String termo) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT codigo, nome, cpf, telefone, email, data_nascimento, data_cadastro FROM clientes WHERE nome ILIKE ? ORDER BY codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearCliente(rs));
                }
            }
        }
        return clientes;
    }

    public void atualizar(int codigo, String novoNome, String novoTelefone, String novoEmail) throws SQLException {
        validarNome(novoNome);

        String sql = "UPDATE clientes SET nome = ?, telefone = ?, email = ? WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
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

    public void remover(int codigo) throws SQLException {
        String sql = "DELETE FROM clientes WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhum cliente encontrado com o Código " + codigo + ".");
            }
        }
    }

    private void validarNome(String nome) {
        if (GenericValidator.isBlankOrNull(nome)) {
            throw new IllegalArgumentException("Nome inválido: não pode ficar em branco!");
        }
    }

    private void validarCpf(String cpf) {
        if (GenericValidator.isBlankOrNull(cpf)) {
            throw new IllegalArgumentException("CPF inválido: não pode ficar em branco!");
        }

        // Certifica de que o CPF digitado tenha exatamente 11 dígitos
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido: deve conter 11 dígitos!");
        }
    }

    private LocalDate validarDataNascimento(String dataNascimento) {
        if (GenericValidator.isBlankOrNull(dataNascimento)) {
            throw new IllegalArgumentException("Data de Nascimento inválida: não pode ficar em branco!");
        }

        // A data deve estar em formato DD/MM/YYYY
        LocalDate data;
        try {
            data = LocalDate.parse(dataNascimento, formatoData);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data de Nascimento inválida: use o formato DD/MM/YYYY!");
        }

        // A data de nascimento deve ter 18 anos ou mais de diferença da data atual (Chave Check no PostgreSQL)
        if (data.isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("Data de Nascimento inválida: cliente deve ter pelo menos 18 anos!");
        }

        return data;
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setCodigo(rs.getInt("codigo"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setDataNascimento(formatarData(rs.getDate("data_nascimento")));
        c.setDataCadastro(formatarData(rs.getDate("data_cadastro")));
        return c;
    }

    private String formatarCpf(String cpf) {
        return cpf == null ? null : cpf.replaceAll("[^0-9]", "");
    }

    private String formatarData(java.sql.Date data) {
        return data == null ? "" : data.toLocalDate().format(formatoData);
    }
}
