/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.PedidoDao;
import dao.PedidoDaoJdbc;
import database.SqlState;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import models.Pedido;
import org.apache.commons.validator.GenericValidator;
import utils.Validador;

/**
 *
 * @author Guilherme Luvison
 */
public class PedidoController {

    // Status válidos para cadastro e atualização, conforme chave CHECK do PostgreSQL
    public static final List<String> STATUS_VALIDOS = Arrays.asList("Entregue", "Em preparo", "Cancelado");
    private final PedidoDao pedidoDao;

    public PedidoController() {
        this(new PedidoDaoJdbc());
    }

    public PedidoController(PedidoDao pedidoDao) {
        this.pedidoDao = pedidoDao;
    }

    public void cadastrar(int codigoCliente, Integer codigoReserva, int mesa, int quantidadePessoas, String status) throws SQLException {
        Validador.validarCodigoCliente(codigoCliente);
        validarCodigoReserva(codigoReserva);
        Validador.validarMesa(mesa);
        Validador.validarQuantidadePessoas(quantidadePessoas);
        validarStatus(status);

        Pedido pedido = new Pedido();
        pedido.setCodigoCliente(codigoCliente);
        pedido.setCodigoReserva(codigoReserva);
        pedido.setMesa(mesa);
        pedido.setQuantidadePessoas(quantidadePessoas);
        pedido.setStatus(status);

        try {
            pedidoDao.cadastrar(pedido);
        } catch (SQLException e) {
            if (SqlState.FK_VIOLATION.equals(e.getSQLState())) {
                throw new IllegalArgumentException("Cliente ou Reservas não encontrados. Verifique os dados selecionados.");
            }
            throw e;
        }
    }

    public List<Pedido> listar() throws SQLException {
        return pedidoDao.listar();
    }

    public List<Pedido> buscarPorNomeCliente(String termo) throws SQLException {
        return pedidoDao.buscarPorNomeCliente(termo);
    }

    public void atualizar(int codigo, int novaMesa, int novaQuantidadePessoas, String novoStatus) throws SQLException {
        Validador.validarMesa(novaMesa);
        Validador.validarQuantidadePessoas(novaQuantidadePessoas);
        validarStatus(novoStatus);

        Pedido pedido = new Pedido();
        pedido.setCodigo(codigo);
        pedido.setMesa(novaMesa);
        pedido.setQuantidadePessoas(novaQuantidadePessoas);
        pedido.setStatus(novoStatus);

        if (!pedidoDao.atualizar(pedido)) {
            throw new IllegalArgumentException("Nenhum pedido encontrado com o Código " + codigo + ".");
        }

    }

    public void remover(int codigo) throws SQLException {
        if (!pedidoDao.remover(codigo)) {
            throw new IllegalArgumentException("Nenhum pedido encontrado com o Código " + codigo + ".");
        }
    }

    private void validarCodigoReserva(Integer codigoReserva) {
        if (codigoReserva != null && codigoReserva <= 0) {
            throw new IllegalArgumentException("Reserva inválida.");
        }
    }

    private void validarStatus(String status) {
        if (GenericValidator.isBlankOrNull(status)) {
            throw new IllegalArgumentException("Status inválido: não pode ficar em branco!");
        }

        // Somente status válidos registrados na chave CHECK do banco de dados
        if (!STATUS_VALIDOS.contains(status)) {
            throw new IllegalArgumentException("Status inválido: deve ser Entregue, Em preparo ou Cancelado.");
        }
    }
}
