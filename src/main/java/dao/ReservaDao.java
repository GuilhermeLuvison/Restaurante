/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.SQLException;
import java.util.List;
import models.Reserva;

/**
 *
 * @author Guilherme Luvison
 */
public interface ReservaDao {

    void cadastrar(Reserva reserva) throws SQLException;

    List<Reserva> listar() throws SQLException;

    List<Reserva> buscarPorNomeCliente(String termo) throws SQLException;

    boolean atualizar(Reserva reserva) throws SQLException;

    boolean remover(int codigo) throws SQLException;
}
