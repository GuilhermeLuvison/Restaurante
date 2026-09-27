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
import java.util.ArrayList;
import java.util.List;
import models.ItemPedido;

/**
 *
 * @author Guilherme Luvison
 */
public class ItemPedidoController {

    public void cadastrar(int codigoPedido, int codigoItemCardapio, int quantidade, double precoUnitario, String observacao) throws SQLException {
        validarCodigoPedido(codigoPedido);
        validarCodigoItemCardapio(codigoItemCardapio);
        validarQuantidade(quantidade);
        validarPrecoUnitario(precoUnitario);

        String sql = "INSERT INTO itenspedido (codigo_pedido, codigo_itenscardapio, quantidade, preco_unitario, observacao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigoPedido);
            pstmt.setInt(2, codigoItemCardapio);
            pstmt.setInt(3, quantidade);
            pstmt.setDouble(4, precoUnitario);
            pstmt.setString(5, observacao);
            pstmt.executeUpdate();
        } catch (SQLException e) { // Violação de Chave Estrangeira do PostgreSQL (caso não tenha pedido ou itemCardapio cadastrados)
            if ("23503".equals(e.getSQLState())) {
                throw new IllegalArgumentException("Pedido ou Item de Cardápio não encontrados. Verifique os dados selecionados.");
            }
            throw e;
        }
    }

    public List<ItemPedido> listar() throws SQLException {
        List<ItemPedido> itensPedido = new ArrayList<>();
        String sql = "SELECT ip.codigo, ip.codigo_pedido, ip.codigo_itenscardapio, ic.nome AS nome_itemcardapio, ip.quantidade, ip.preco_unitario, ip.preco_total, ip.observacao FROM itenspedido ip JOIN itenscardapio ic ON ip.codigo_itenscardapio = ic.codigo ORDER BY ip.codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                itensPedido.add(mapearItemPedido(rs));
            }
        }
        return itensPedido;
    }

    public List<ItemPedido> buscarPorNome(String termo) throws SQLException {
        List<ItemPedido> itensPedido = new ArrayList<>();
        String sql = "SELECT ip.codigo, ip.codigo_pedido, ip.codigo_itenscardapio, ic.nome AS nome_itemcardapio, ip.quantidade, ip.preco_unitario, ip.preco_total, ip.observacao FROM itenspedido ip JOIN itenscardapio ic ON ip.codigo_itenscardapio = ic.codigo WHERE ic.nome ILIKE ? ORDER BY ip.codigo";

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

    public void atualizar(int codigo, int novaQuantidade, double novoPrecoUnitario, String novaObservacao) throws SQLException {
        validarQuantidade(novaQuantidade);
        validarPrecoUnitario(novoPrecoUnitario);

        String sql = "UPDATE itenspedido SET quantidade = ?, preco_unitario = ?, observacao = ?  WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, novaQuantidade);
            pstmt.setDouble(2, novoPrecoUnitario);
            pstmt.setString(3, novaObservacao);
            pstmt.setInt(4, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhum item de pedido encontrado com o Código " + codigo + ".");
            }
        }
    }

    public void remover(int codigo) throws SQLException {
        String sql = "DELETE FROM itenspedido WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhum item de pedido encontrado com o Código " + codigo + ".");
            }
        }
    }

    private void validarCodigoPedido(int codigoPedido) {
        if (codigoPedido <= 0) {
            throw new IllegalArgumentException("Selecione um pedido válido.");
        }
    }

    private void validarCodigoItemCardapio(int codigoItemCardapio) {
        if (codigoItemCardapio <= 0) {
            throw new IllegalArgumentException("Selecione um item de cardápio válido.");
        }
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade de itens inválido: não pode ser negativo ou igual a 0!");
        }
    }

    private void validarPrecoUnitario(double precoUnitario) {
        if (precoUnitario < 0) {
            throw new IllegalArgumentException("Preço unitário inválido: não pode ser negativo!");
        }
    }

    private ItemPedido mapearItemPedido(ResultSet rs) throws SQLException {
        ItemPedido ip = new ItemPedido();
        ip.setCodigo(rs.getInt("codigo"));
        ip.setCodigoPedido(rs.getInt("codigo_pedido"));
        ip.setCodigoItemCardapio(rs.getInt("codigo_itemcardapio"));
        ip.setNomeItemCardapio(rs.getString("nome_itemcardapio"));
        ip.setQuantidade(rs.getInt("quantidade"));
        ip.setPrecoUnitario(rs.getDouble("preco_unitario"));
        ip.setPrecoTotal(rs.getDouble("preco_total"));
        ip.setObservacao(rs.getString("observacao"));
        return ip;
    }
}
