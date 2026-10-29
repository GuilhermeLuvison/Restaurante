/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.List;
import models.ItemPedido;

/**
 *
 * @author Guilherme Luvison
 */
public interface ItemPedidoDao {

    void cadastrar(ItemPedido itemPedido) throws SQLException;

    List<ItemPedido> listar() throws SQLException;

    List<ItemPedido> buscarPorNomeItemCardapio(String termo) throws SQLException;

    boolean atualizar(ItemPedido itemPedido) throws SQLException;

    boolean remover(int codigo) throws SQLException;
}
