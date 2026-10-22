/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.ItemPedidoDao;
import dao.ItemPedidoDaoJdbc;
import database.ConexaoBanco;
import database.SqlState;
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

    private final ItemPedidoDao itemPedidoDao;

    public ItemPedidoController() {
        this(new ItemPedidoDaoJdbc());
    }

    public ItemPedidoController(ItemPedidoDao itemPedidoDao) {
        this.itemPedidoDao = itemPedidoDao;
    }

    public void cadastrar(int codigoPedido, int codigoItemCardapio, int quantidade, double precoUnitario, String observacao) throws SQLException {
        validarCodigoPedido(codigoPedido);
        validarCodigoItemCardapio(codigoItemCardapio);
        validarQuantidade(quantidade);
        validarPrecoUnitario(precoUnitario);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setCodigoPedido(codigoPedido);
        itemPedido.setCodigoItemCardapio(codigoItemCardapio);
        itemPedido.setQuantidade(quantidade);
        itemPedido.setPrecoUnitario(precoUnitario);
        itemPedido.setObservacao(observacao);

        try {
            itemPedidoDao.cadastrar(itemPedido);
        } catch (SQLException e) {
            if (SqlState.FK_VIOLATION.equals(e.getSQLState())) {
                throw new IllegalArgumentException("Pedido ou Item de Cardápio não encontrados. Verifique os dados selecionados.");
            }
            throw e;
        }
    }

    public List<ItemPedido> listar() throws SQLException {
        return itemPedidoDao.listar();
    }

    public List<ItemPedido> buscarPorNome(String termo) throws SQLException {
        return itemPedidoDao.buscarPorNome(termo);
    }

    public void atualizar(int codigo, int novaQuantidade, double novoPrecoUnitario, String novaObservacao) throws SQLException {
        validarQuantidade(novaQuantidade);
        validarPrecoUnitario(novoPrecoUnitario);

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setCodigo(codigo);
        itemPedido.setQuantidade(novaQuantidade);
        itemPedido.setPrecoUnitario(novoPrecoUnitario);
        itemPedido.setObservacao(novaObservacao);

        if (!itemPedidoDao.atualizar(itemPedido)) {
            throw new IllegalArgumentException("Nenhum item de pedido encontrado com o Código " + codigo + ".");
        }

    }

    public void remover(int codigo) throws SQLException {
        if (!itemPedidoDao.remover(codigo)) {
            throw new IllegalArgumentException("Nenhum item de pedido encontrado com o Código " + codigo + ".");
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
}
