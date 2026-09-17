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
import models.ItemCardapio;
import org.apache.commons.validator.GenericValidator;

/**
 *
 * @author Guilherme Luvison
 */
public class ItemCardapioController {

    // Atributos
    String url = "jdbc:postgresql://localhost:5432/restaurante";
    String usuario = "postgres";
    String senha = "postgres";

    // Método de Cadastro
    public void cadastrar(String nome, String ingredientes, String categoria, String tipoPrato, double preco, String tempoPreparo) throws SQLException {
        validarNome(nome);
        validarIngredientes(ingredientes);
        validarCategoria(categoria);
        validarTipoPrato(tipoPrato);
        validarPreco(preco);
        validarTempoPreaparo(tempoPreparo);

        String sql = "INSERT INTO itenscardapio (nome, ingredientes, categoria, tipo_prato, preco, tempo_preparo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, ingredientes);
            pstmt.setString(3, categoria);
            pstmt.setString(4, tipoPrato);
            pstmt.setDouble(5, preco);
            pstmt.setString(6, tempoPreparo);
            pstmt.executeUpdate();
        }
    }

    // Método de Listagem
    public List<ItemCardapio> listar() throws SQLException {
        List<ItemCardapio> itensCardapio = new ArrayList<>();
        String sql = "SELECT codigo, nome, ingredientes, categoria, tipo_prato, preco, tempo_preparo FROM itenscardapio ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                itensCardapio.add(mapearItemCardapio(rs));
            }
        }
        return itensCardapio;
    }

    // Método de Busca
    public List<ItemCardapio> buscarPorNome(String termo) throws SQLException {
        List<ItemCardapio> itensCardapio = new ArrayList<>();
        String sql = "SELECT codigo, nome, ingredientes, categoria, tipo_prato, preco, tempo_preparo FROM itenscardapio WHERE nome ILIKE ? ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    itensCardapio.add(mapearItemCardapio(rs));
                }
            }
        }
        return itensCardapio;
    }

    // Método de Atualização
    public void atualizar(int codigo, String novoNome, String novoIngredientes, String novoTipoPrato, double novoPreco, String novoTempoPreparo) throws SQLException {
        validarNome(novoNome);
        validarIngredientes(novoIngredientes);
        validarTipoPrato(novoTipoPrato);
        validarPreco(novoPreco);
        validarTempoPreaparo(novoTempoPreparo);

        String sql = "UPDATE itenscardapio SET nome = ?, ingredientes = ?, tipo_prato = ?, preco = ?, tempo_preparo = ? WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, novoNome);
            pstmt.setString(2, novoIngredientes);
            pstmt.setString(3, novoTipoPrato);
            pstmt.setDouble(4, novoPreco);
            pstmt.setString(5, novoTempoPreparo);
            pstmt.setInt(6, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhum cliente encontrado com o Código " + codigo + ".");
            }
        }
    }

    // Método de Remoção
    public void remover(int codigo) throws SQLException {
        String sql = "DELETE FROM itenscardapio WHERE codigo = ?";

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

    private void validarIngredientes(String ingredientes) {
        if (GenericValidator.isBlankOrNull(ingredientes)) {
            throw new IllegalArgumentException("Ingredientes inválidos: não pode ficar em branco! Tente novamente.");
        }
    }

    private void validarCategoria(String categoria) {
        if (GenericValidator.isBlankOrNull(categoria)) {
            throw new IllegalArgumentException("Categoria inválida: não pode ficar em branco! Tente novamente.");
        }
    }

    private void validarTipoPrato(String tipoPrato) {
        if (GenericValidator.isBlankOrNull(tipoPrato)) {
            throw new IllegalArgumentException("Tipo de Prato inválido: não pode ficar em branco! Tente novamente.");
        }
    }

    private void validarPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço inválido: não pode ser negativo! Tente novamente.");
        }
    }

    private void validarTempoPreaparo(String tempoPreparo) {
        if (GenericValidator.isBlankOrNull(tempoPreparo)) {
            throw new IllegalArgumentException("Tempo de Preparo inválido: não pode ficar em branco! Tente novamente.");
        }
    }

    // Método de Mapeamento
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
