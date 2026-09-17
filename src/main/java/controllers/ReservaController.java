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
import models.Reserva;
import org.apache.commons.validator.GenericValidator;

/**
 *
 * @author Guilherme Luvison
 */
public class ReservaController {

    // Atributos
    String url = "jdbc:postgresql://localhost:5432/restaurante";
    String usuario = "postgres";
    String senha = "postgres";

    // Método de Cadastro
    public void cadastrar(String nomeCliente, int mesa, int qtdePessoas, String observacao, String dataReserva, String status) throws SQLException {
        validarNomeCliente(nomeCliente);
        validarMesa(mesa);
        validarQtdePessoas(qtdePessoas);
        validarDataReserva(dataReserva);
        validarStatus(status);

        String sql = "INSERT INTO reservas (nome_cliente, mesa, quantidade_pessoas, observacao, data_reserva, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, nomeCliente);
            pstmt.setInt(2, mesa);
            pstmt.setInt(3, qtdePessoas);
            pstmt.setString(4, observacao);
            pstmt.setString(5, dataReserva);
            pstmt.setString(6, status);
            pstmt.executeUpdate();
        }
    }

    // Método de Listagem
    public List<Reserva> listar() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT codigo, nome_cliente, mesa, quantidade_pessoas, observacao, data_reserva, status FROM reservas ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                reservas.add(mapearReserva(rs));
            }
        }
        return reservas;
    }

    // Método de Busca
    public List<Reserva> buscarPorNome(String termo) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT codigo, nome_cliente, mesa, quantidade_pessoas, observacao, data_reserva, status FROM reservas WHERE nome_cliente ILIKE ? ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReserva(rs));
                }
            }
        }
        return reservas;
    }

    // Método de Atualização
    public void atualizar(int codigo, int novoQtdePessoas, String novoObservacao, String novoDataReserva, String novoStatus) throws SQLException {
        validarQtdePessoas(novoQtdePessoas);
        validarDataReserva(novoDataReserva);
        validarStatus(novoStatus);

        String sql = "UPDATE reservas SET quantidade_pessoas = ?, observacao = ?, data_reserva = ?, status = ?  WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, novoQtdePessoas);
            pstmt.setString(2, novoObservacao);
            pstmt.setString(3, novoDataReserva);
            pstmt.setString(4, novoStatus);
            pstmt.setInt(5, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhum cliente encontrado com o Código " + codigo + ".");
            }
        }
    }

    // Método de Remoção
    public void remover(int codigo) throws SQLException {
        String sql = "DELETE FROM reservas WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhum cliente encontrado com o Código " + codigo + ".");
            }
        }
    }

    // Métodos de Validação
    private void validarNomeCliente(String nomeCliente) {
        if (GenericValidator.isBlankOrNull(nomeCliente)) {
            throw new IllegalArgumentException("Nome inválido: não pode ficar em branco! Tente novamente.");
        }
    }

    private void validarMesa(int mesa) {
        if (mesa <= 0) {
            throw new IllegalArgumentException("Número da mesa inválido: não pode ser negativo ou igual a 0! Tente novamente.");
        }
    }

    private void validarQtdePessoas(int qtdePessoas) {
        if (qtdePessoas <= 0) {
            throw new IllegalArgumentException("Quantidade de pessoas inválido: não pode ser negativo ou igual a 0! Tente novamente.");
        }
    }

    private void validarDataReserva(String dataReserva) {
        if (GenericValidator.isBlankOrNull(dataReserva)) {
            throw new IllegalArgumentException("Data da Reserva inválida: não pode ficar em branco! Tente novamente.");
        }
    }

    private void validarStatus(String status) {
        if (GenericValidator.isBlankOrNull(status)) {
            throw new IllegalArgumentException("Status inválido: não pode ficar em branco! Tente novamente.");
        }
    }

    // Método de Impressão de Cliente Específico
    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setCodigo(rs.getInt("codigo"));
        r.setNomeCliente(rs.getString("nome_cliente"));
        r.setMesa(rs.getInt("mesa"));
        r.setQtdePessoas(rs.getInt("quantidade_pessoas"));
        r.setObservacao(rs.getString("observacao"));
        r.setDataReserva(rs.getString("data_reserva"));
        r.setStatus(rs.getString("status"));
        return r;
    }
}
