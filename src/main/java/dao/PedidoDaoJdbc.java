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
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import models.Pedido;
import utils.Formatador;

/**
 *
 * @author Guilherme Luvison
 */
public class PedidoDaoJdbc implements PedidoDao {

    private static final String SELECT_PEDIDOS = "SELECT p.codigo, p.codigo_cliente, c.nome AS nome_cliente, p.codigo_reserva, p.mesa, p.quantidade_pessoas, p.momento_pedido, p.status FROM pedidos p JOIN clientes c ON p.codigo_cliente = c.codigo";

    @Override
    public void cadastrar(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (codigo_cliente, codigo_reserva, mesa, quantidade_pessoas, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, pedido.getCodigoCliente());
            if (pedido.getCodigoReserva() == null) { // Código de reserva é opcional. If envia NULL quando não há reserva vinculada
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, pedido.getCodigoReserva());
            }
            pstmt.setInt(3, pedido.getMesa());
            pstmt.setInt(4, pedido.getQtdePessoas());
            pstmt.setString(5, pedido.getStatus());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Pedido> listar() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = SELECT_PEDIDOS + " ORDER BY p.codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        }
        return pedidos;
    }

    @Override
    public List<Pedido> buscarPorNome(String termo) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = SELECT_PEDIDOS + " WHERE c.nome ILIKE ? ORDER BY p.codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        }
        return pedidos;
    }

    @Override
    public boolean atualizar(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET mesa = ?, quantidade_pessoas = ?, status = ?  WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, pedido.getMesa());
            pstmt.setInt(2, pedido.getQtdePessoas());
            pstmt.setString(3, pedido.getStatus());
            pstmt.setInt(4, pedido.getCodigo());
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean remover(int codigo) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setCodigo(rs.getInt("codigo"));
        p.setCodigoCliente(rs.getInt("codigo_cliente"));
        p.setNomeCliente(rs.getString("nome_cliente"));
        p.setCodigoReserva(rs.getObject("codigo_reserva", Integer.class)); // getObject com o tipo Integer.class retorna NULL quando a coluna está vazia, getInt devolveria 0.
        p.setMesa(rs.getInt("mesa"));
        p.setQtdePessoas(rs.getInt("quantidade_pessoas"));
        p.setMomentoPedido(Formatador.formatarDataHora(rs.getTimestamp("momento_pedido")));
        p.setStatus(rs.getString("status"));
        return p;
    }
}
