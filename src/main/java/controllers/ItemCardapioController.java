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
import models.ItemCardapio;
import org.apache.commons.validator.GenericValidator;
import resources.Entrada;

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
    public void cadastrar() {
        String nome;
        do {
            nome = Entrada.leiaString("Nome do Item de Cardápio:");
            if (GenericValidator.isBlankOrNull(nome)) {
                System.out.println("Nome inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(nome));

        String ingredientes;
        do {
            ingredientes = Entrada.leiaString("Ingredientes:");
            if (GenericValidator.isBlankOrNull(ingredientes)) {
                System.out.println("Ingredientes inválidos: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(ingredientes));

        String categoria;
        do {
            categoria = Entrada.leiaString("Categoria:");
            if (GenericValidator.isBlankOrNull(categoria)) {
                System.out.println("Categoria inválida: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(categoria));

        String tipoPrato;
        do {
            tipoPrato = Entrada.leiaString("Tipo de Prato:");
            if (GenericValidator.isBlankOrNull(tipoPrato)) {
                System.out.println("Tipo de Prato inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(tipoPrato));

        double preco = Entrada.leiaDouble("Preço (Obrigatório:");

        String tempoPreparo;
        do {
            tempoPreparo = Entrada.leiaString("Tempo de Preparo:");
            if (GenericValidator.isBlankOrNull(tempoPreparo)) {
                System.out.println("Tempo de Preparo inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(tempoPreparo));

        String sql = "INSERT INTO itenscardapio (nome, ingredientes, categoria, tipo_prato, preco, tempo_preparo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, ingredientes);
            pstmt.setString(3, categoria);
            pstmt.setString(4, tipoPrato);
            pstmt.setDouble(5, preco);
            pstmt.setString(6, tempoPreparo);
            pstmt.executeUpdate();
            System.out.println("Item de Cardápio cadastrado com sucesso!");
            System.out.println("");
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Listagem
    public void listar() {
        String sql = "SELECT codigo, nome, ingredientes, categoria, tipo_prato, preco, tempo_preparo FROM itenscardapio ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            System.out.println("===[ITENS DO CARDÁPIO CADASTRADOS]===");
            while (rs.next()) {
                imprimeItensCardapio(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Busca
    public void buscarPorNome() {
        String termo = Entrada.leiaString("Digite o nome (ou parte dele) para buscar:");
        String sql = "SELECT codigo, nome, ingredientes, categoria, tipo_prato, preco, tempo_preparo FROM itenscardapio WHERE nome ILIKE ? ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean encontrouAlgum = false;
                System.out.println("===[ITENS DE CARDÁPIO ENCONTRADOS]===");
                while (rs.next()) {
                    imprimeItensCardapio(rs);
                    encontrouAlgum = true;
                }
                if (!encontrouAlgum) {
                    System.out.println("Nenhum item de cardápio encontrado com esse nome.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Atualização
    public void atualizar() {
        int codigo = Entrada.leiaInt("Digite o Código do item de cardápio que deseja atualizar:");

        String novoNome;
        do {
            novoNome = Entrada.leiaString("Novo nome:");
            if (GenericValidator.isBlankOrNull(novoNome)) {
                System.out.println("Nome inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(novoNome));

        String novoIngredientes;
        do {
            novoIngredientes = Entrada.leiaString("Novos ingredientes:");
            if (GenericValidator.isBlankOrNull(novoIngredientes)) {
                System.out.println("Ingredientes inválidos: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(novoIngredientes));

        String novoTipoPrato;
        do {
            novoTipoPrato = Entrada.leiaString("Novo tipo de prato:");
            if (GenericValidator.isBlankOrNull(novoTipoPrato)) {
                System.out.println("Tipo de Prato inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(novoTipoPrato));

        double novoPreco = Entrada.leiaDouble("Novo preço:");

        String novoTempoPreparo;
        do {
            novoTempoPreparo = Entrada.leiaString("Novo tempo de preparo:");
            if (GenericValidator.isBlankOrNull(novoTempoPreparo)) {
                System.out.println("Tempo de Preparo inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(novoTempoPreparo));

        String sql = "UPDATE itenscardapio SET nome = ?, ingredientes = ?, tipo_prato = ?, preco = ?, tempo_preparo = ? WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, novoNome);
            pstmt.setString(2, novoIngredientes);
            pstmt.setString(3, novoTipoPrato);
            pstmt.setDouble(4, novoPreco);
            pstmt.setString(5, novoTempoPreparo);
            pstmt.setInt(6, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Item de Cardápio atualizado com sucesso!");
                System.out.println("");
            } else {
                System.out.println("Nenhum item de cardápio encontrado com o Código " + codigo + ".");
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Remoção
    public void remover() {
        int codigo = Entrada.leiaInt("Digite o Código do item de cardápio que deseja remover:");
        boolean confirma = Entrada.leiaBoolean("Tem certeza que deseja remover o item de cardápio de Código " + codigo + "?");

        if (!confirma) {
            System.out.println("Remoção cancelada.");
            return;
        }

        String sql = "DELETE FROM itenscardapio WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Item de Cardápio removido com sucesso!");
                System.out.println("");
            } else {
                System.out.println("Nenhum item de cardápio encontrado com o Código " + codigo + ".");
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Impressão de Cliente Específico
    private void imprimeItensCardapio(ResultSet rs) throws SQLException {
        ItemCardapio ic = new ItemCardapio();
        ic.setCodigo(rs.getInt("codigo"));
        ic.setNome(rs.getString("nome"));
        ic.setIngredientes(rs.getString("ingredientes"));
        ic.setCategoria(rs.getString("categoria"));
        ic.setTipoPrato(rs.getString("tipo_prato"));
        ic.setPreco(rs.getDouble("preco"));
        ic.setTempoPreparo(rs.getString("tempo_preparo"));
        ic.imprimeAtributos();
    }
}
