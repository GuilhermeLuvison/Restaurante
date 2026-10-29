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
import java.util.ArrayList;
import java.util.List;
import models.ItemPedido;

/**
 *
 * @author Guilherme Luvison
 */
public class ItemPedidoDaoJdbc implements ItemPedidoDao {

    private static final String SELECT_ITENSPEDIDO = "SELECT ip.codigo, ip.codigo_pedido, ip.codigo_itenscardapio, ic.nome AS nome_itemcardapio, ip.quantidade, ip.preco_unitario, ip.preco_total, ip.observacao FROM itenspedido ip JOIN itenscardapio ic ON ip.codigo_itenscardapio = ic.codigo";

    @Override
    public void cadastrar(ItemPedido itemPedido) throws SQLException {
        String sql = "INSERT INTO itenspedido (codigo_pedido, codigo_itenscardapio, quantidade, preco_unitario, observacao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, itemPedido.getCodigoPedido());
            pstmt.setInt(2, itemPedido.getCodigoItemCardapio());
            pstmt.setInt(3, itemPedido.getQuantidade());
            pstmt.setDouble(4, itemPedido.getPrecoUnitario());
            pstmt.setString(5, itemPedido.getObservacao());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<ItemPedido> listar() throws SQLException {
        List<ItemPedido> itensPedido = new ArrayList<>();
        String sql = SELECT_ITENSPEDIDO + " ORDER BY ip.codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                itensPedido.add(mapearItemPedido(rs));
            }
        }
        return itensPedido;
    }

    @Override
    public List<ItemPedido> buscarPorNomeItemCardapio(String termo) throws SQLException {
        List<ItemPedido> itensPedido = new ArrayList<>();
        String sql = SELECT_ITENSPEDIDO + " WHERE ic.nome ILIKE ? ORDER BY ip.codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    itensPedido.add(mapearItemPedido(rs));
                }
            }
        }
        return itensPedido;
    }

    @Override
    public boolean atualizar(ItemPedido itemPedido) throws SQLException {
        String sql = "UPDATE itenspedido SET quantidade = ?, preco_unitario = ?, observacao = ?  WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, itemPedido.getQuantidade());
            pstmt.setDouble(2, itemPedido.getPrecoUnitario());
            pstmt.setString(3, itemPedido.getObservacao());
            pstmt.setInt(4, itemPedido.getCodigo());
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean remover(int codigo) throws SQLException {
        String sql = "DELETE FROM itenspedido WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            return pstmt.executeUpdate() > 0;
        }
    }

    private ItemPedido mapearItemPedido(ResultSet rs) throws SQLException {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setCodigo(rs.getInt("codigo"));
        itemPedido.setCodigoPedido(rs.getInt("codigo_pedido"));
        itemPedido.setCodigoItemCardapio(rs.getInt("codigo_itenscardapio"));
        itemPedido.setNomeItemCardapio(rs.getString("nome_itemcardapio"));
        itemPedido.setQuantidade(rs.getInt("quantidade"));
        itemPedido.setPrecoUnitario(rs.getDouble("preco_unitario"));
        itemPedido.setPrecoTotal(rs.getDouble("preco_total"));
        itemPedido.setObservacao(rs.getString("observacao"));
        return itemPedido;
    }
}
