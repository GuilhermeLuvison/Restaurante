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
import models.ItemCardapio;

/**
 *
 * @author Guilherme Luvison
 */
public class ItemCardapioDaoJdbc implements ItemCardapioDao {

    @Override
    public void cadastrar(ItemCardapio itemCardapio) throws SQLException {
        String sql = "INSERT INTO itenscardapio (nome, ingredientes, categoria, tipo_prato, preco, tempo_preparo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, itemCardapio.getNome());
            pstmt.setString(2, itemCardapio.getIngredientes());
            pstmt.setString(3, itemCardapio.getCategoria());
            pstmt.setString(4, itemCardapio.getTipoPrato());
            pstmt.setDouble(5, itemCardapio.getPreco());
            pstmt.setString(6, itemCardapio.getTempoPreparo());
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<ItemCardapio> listar() throws SQLException {
        List<ItemCardapio> itensCardapio = new ArrayList<>();
        String sql = "SELECT codigo, nome, ingredientes, categoria, tipo_prato, preco, tempo_preparo FROM itenscardapio ORDER BY codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                itensCardapio.add(mapearItemCardapio(rs));
            }
        }
        return itensCardapio;
    }

    @Override
    public List<ItemCardapio> buscarPorNome(String termo) throws SQLException {
        List<ItemCardapio> itensCardapio = new ArrayList<>();
        String sql = "SELECT codigo, nome, ingredientes, categoria, tipo_prato, preco, tempo_preparo FROM itenscardapio WHERE nome ILIKE ? ORDER BY codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    itensCardapio.add(mapearItemCardapio(rs));
                }
            }
        }
        return itensCardapio;
    }

    @Override
    public boolean atualizar(ItemCardapio itemCardapio) throws SQLException {
        String sql = "UPDATE itenscardapio SET nome = ?, ingredientes = ?, categoria = ?, preco = ?, tempo_preparo = ? WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, itemCardapio.getNome());
            pstmt.setString(2, itemCardapio.getIngredientes());
            pstmt.setString(3, itemCardapio.getCategoria());
            pstmt.setDouble(4, itemCardapio.getPreco());
            pstmt.setString(5, itemCardapio.getTempoPreparo());
            pstmt.setInt(6, itemCardapio.getCodigo());
            return pstmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean remover(int codigo) throws SQLException {
        String sql = "DELETE FROM itenscardapio WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            return pstmt.executeUpdate() > 0;
        }
    }

    private ItemCardapio mapearItemCardapio(ResultSet rs) throws SQLException {
        ItemCardapio ic = new ItemCardapio();
        ic.setCodigo(rs.getInt("codigo"));
        ic.setNome(rs.getString("nome"));
        ic.setIngredientes(rs.getString("ingredientes"));
        ic.setCategoria(rs.getString("categoria"));
        ic.setTipoPrato(rs.getString("tipo_prato"));
        ic.setPreco(rs.getDouble("preco"));
        ic.setTempoPreparo(rs.getString("tempo_preparo"));
        return ic;
    }
}
