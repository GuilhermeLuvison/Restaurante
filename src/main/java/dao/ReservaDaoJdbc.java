/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConexaoBanco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import models.Reserva;
import utils.Formatador;

/**
 *
 * @author Guilherme Luvison
 */
public class ReservaDaoJdbc implements ReservaDao {

    private static final String SELECT_RESERVAS = "SELECT r.codigo, r.codigo_cliente, c.nome AS nome_cliente, r.mesa, r.quantidade_pessoas, r.observacao, r.data_reserva, r.status FROM reservas r JOIN clientes c ON r.codigo_cliente = c.codigo";

    @Override
    public void cadastrar(Reserva reserva) throws SQLException {
        String sql = "INSERT INTO reservas (codigo_cliente, mesa, quantidade_pessoas, observacao, data_reserva, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, reserva.getCodigoCliente());
            pstmt.setInt(2, reserva.getMesa());
            pstmt.setInt(3, reserva.getQtdePessoas());
            pstmt.setString(4, reserva.getObservacao());
            pstmt.setDate(5, java.sql.Date.valueOf(LocalDate.parse(reserva.getDataReserva(), Formatador.FORMATO_DATA)));
            pstmt.setString(6, reserva.getStatus());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Reserva> listar() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = SELECT_RESERVAS + " ORDER BY r.codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                reservas.add(mapearReserva(rs));
            }
        }
        return reservas;
    }

    @Override
    public List<Reserva> buscarPorNome(String termo) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = SELECT_RESERVAS + " WHERE c.nome ILIKE ? ORDER BY r.codigo";

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

    @Override
    public boolean atualizar(Reserva reserva) throws SQLException {
        String sql = "UPDATE reservas SET quantidade_pessoas = ?, observacao = ?, data_reserva = ?, status = ?  WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, reserva.getQtdePessoas());
            pstmt.setString(2, reserva.getObservacao());
            pstmt.setDate(3, java.sql.Date.valueOf(LocalDate.parse(reserva.getDataReserva(), Formatador.FORMATO_DATA)));
            pstmt.setString(4, reserva.getStatus());
            pstmt.setInt(5, reserva.getCodigo());
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean remover(int codigo) throws SQLException {
        String sql = "DELETE FROM reservas WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            return pstmt.executeUpdate() > 0;
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
        r.setDataReserva(Formatador.formatarData(rs.getDate("data_reserva")));
        r.setStatus(rs.getString("status"));
        return r;
    }
}
