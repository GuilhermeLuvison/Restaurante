/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.List;
import models.ItemCardapio;

/**
 *
 * @author Guilherme Luvison
 */
public interface ItemCardapioDao {

    void cadastrar(ItemCardapio itemCardapio) throws SQLException;

    List<ItemCardapio> listar() throws SQLException;

    List<ItemCardapio> buscarPorNome(String termo) throws SQLException;

    boolean atualizar(ItemCardapio itemCardapio) throws SQLException;

    boolean remover(int codigo) throws SQLException;
}
