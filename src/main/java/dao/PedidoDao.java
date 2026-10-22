/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.List;
import models.Pedido;

/**
 *
 * @author Guilherme Luvison
 */
public interface PedidoDao {

    void cadastrar(Pedido pedido) throws SQLException;

    List<Pedido> listar() throws SQLException;

    List<Pedido> buscarPorNome(String termo) throws SQLException;

    boolean atualizar(Pedido pedido) throws SQLException;

    boolean remover(int codigo) throws SQLException;
}
