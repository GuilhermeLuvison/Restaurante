/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import database.ConexaoBanco;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.Reserva;
import org.apache.commons.validator.GenericValidator;

/**
 *
 * @author Guilherme Luvison
 */
public class ReservaController {

    private static final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Status válidos para cadastro e atualização, conforme chave CHECK do PostgreSQL
    public static final List<String> statusValidos = Arrays.asList("Confirmada", "Pendente", "Cancelada");

    public void cadastrar(int codigoCliente, int mesa, int qtdePessoas, String observacao, String dataReserva, String status) throws SQLException {
        validarCodigoCliente(codigoCliente);
        validarMesa(mesa);
        validarQtdePessoas(qtdePessoas);
        LocalDate data = validarDataReserva(dataReserva);
        validarStatus(status);

        String sql = "INSERT INTO reservas (codigo_cliente, mesa, quantidade_pessoas, observacao, data_reserva, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigoCliente);
            pstmt.setInt(2, mesa);
            pstmt.setInt(3, qtdePessoas);
            pstmt.setString(4, observacao);
            pstmt.setDate(5, Date.valueOf(data));
            pstmt.setString(6, status);
            pstmt.executeUpdate();
        } catch (SQLException e) { // Violação de Chave Estrangeira do PostgreSQL
            if ("23503".equals(e.getSQLState())) {
                throw new IllegalArgumentException("Cliente não encontrado. Selecione um cliente válido.");
            }
            throw e;
        }
    }

    public List<Reserva> listar() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT r.codigo, r.codigo_cliente, c.nome AS nome_cliente, r.mesa, r.quantidade_pessoas, r.observacao, r.data_reserva, r.status FROM reservas r JOIN clientes c ON r.codigo_cliente = c.codigo ORDER BY r.codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                reservas.add(mapearReserva(rs));
            }
        }
        return reservas;
    }

    public List<Reserva> buscarPorNome(String termo) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT r.codigo, r.codigo_cliente, c.nome AS nome_cliente, r.mesa, r.quantidade_pessoas, r.observacao, r.data_reserva, r.status FROM reservas r JOIN clientes c ON r.codigo_cliente = c.codigo WHERE c.nome ILIKE ? ORDER BY r.codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReserva(rs));
                }
            }
        }
        return reservas;
    }

    public void atualizar(int codigo, int novoQtdePessoas, String novoObservacao, String novoDataReserva, String novoStatus) throws SQLException {
        validarQtdePessoas(novoQtdePessoas);
        LocalDate data = validarDataReserva(novoDataReserva);
        validarStatus(novoStatus);

        String sql = "UPDATE reservas SET quantidade_pessoas = ?, observacao = ?, data_reserva = ?, status = ?  WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, novoQtdePessoas);
            pstmt.setString(2, novoObservacao);
            pstmt.setDate(3, Date.valueOf(data));
            pstmt.setString(4, novoStatus);
            pstmt.setInt(5, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhuma reserva encontrada com o Código " + codigo + ".");
            }
        }
    }

    public void remover(int codigo) throws SQLException {
        String sql = "DELETE FROM reservas WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhuma reserva encontrada com o Código " + codigo + ".");
            }
        }
    }

    private void validarCodigoCliente(int codigoCliente) {
        if (codigoCliente <= 0) {
            throw new IllegalArgumentException("Selecione um cliente válido.");
        }
    }

    private void validarMesa(int mesa) {
        if (mesa <= 0) {
            throw new IllegalArgumentException("Número da mesa inválido: não pode ser negativo ou igual a 0!");
        }
    }

    private void validarQtdePessoas(int qtdePessoas) {
        if (qtdePessoas <= 0) {
            throw new IllegalArgumentException("Quantidade de pessoas inválida: não pode ser negativo ou igual a 0!");
        }
    }

    private LocalDate validarDataReserva(String dataReserva) {
        if (GenericValidator.isBlankOrNull(dataReserva)) {
            throw new IllegalArgumentException("Data de Reserva inválida: não pode ficar em branco!");
        }

        LocalDate data;
        try {
            data = LocalDate.parse(dataReserva, formatoData);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data de Reserva inválida: use o formato DD/MM/YYYY!");
        }

        return data;
    }

    private void validarStatus(String status) {
        if (GenericValidator.isBlankOrNull(status)) {
            throw new IllegalArgumentException("Status inválido: não pode ficar em branco!");
        }

        // Somente status válidos registrados na chave CHECK do banco de dados
        if (!statusValidos.contains(status)) {
            throw new IllegalArgumentException("Status inválido: deve ser Confirmada, Presente ou Cancelada.");
        }
    }

    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setCodigo(rs.getInt("codigo"));
        r.setCodigoCliente(rs.getInt("codigo_cliente"));
        r.setNomeCliente(rs.getString("nome_cliente"));
        r.setMesa(rs.getInt("mesa"));
        r.setQtdePessoas(rs.getInt("quantidade_pessoas"));
        r.setObservacao(rs.getString("observacao"));
        r.setDataReserva(formatarData(rs.getDate("data_reserva")));
        r.setStatus(rs.getString("status"));
        return r;
    }

    private String formatarData(java.sql.Date data) {
        return data == null ? "" : data.toLocalDate().format(formatoData);
    }
}
