/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.List;
import models.Cliente;

/**
 *
 * @author Guilherme Luvison
 */
public interface ClienteDao {

    void cadastrar(Cliente cliente) throws SQLException;

    List<Cliente> listar() throws SQLException;

    List<Cliente> buscarPorNome(String termo) throws SQLException;

    boolean atualizar(Cliente cliente) throws SQLException;

    boolean remover(int codigo) throws SQLException;
}
