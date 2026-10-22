/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.PedidoDao;
import dao.PedidoDaoJdbc;
import database.ConexaoBanco;
import database.SqlState;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Status válidos para cadastro e atualização, conforme chave CHECK do PostgreSQL
    public static final List<String> STATUS_VALIDOS = Arrays.asList("Entregue", "Em preparo", "Cancelado");
    private final PedidoDao pedidoDao;

    public PedidoController() {
        this(new PedidoDaoJdbc());
    }

    public PedidoController(PedidoDao pedidoDao) {
        this.pedidoDao = pedidoDao;
    }

    public void cadastrar(int codigoCliente, Integer codigoReserva, int mesa, int qtdePessoas, String status) throws SQLException {
        Validador.validarCodigoCliente(codigoCliente);
        validarCodigoReserva(codigoReserva);
        Validador.validarMesa(mesa);
        Validador.validarQtdePessoas(qtdePessoas);
        validarStatus(status);

        Pedido pedido = new Pedido();
        pedido.setCodigoCliente(codigoCliente);
        pedido.setCodigoReserva(codigoReserva);
        pedido.setMesa(mesa);
        pedido.setQtdePessoas(qtdePessoas);
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

    public List<Pedido> buscarPorNome(String termo) throws SQLException {
        return pedidoDao.buscarPorNome(termo);
    }

    public void atualizar(int codigo, int novoMesa, int novoQtdePessoas, String novoStatus) throws SQLException {
        Validador.validarMesa(novoMesa);
        Validador.validarQtdePessoas(novoQtdePessoas);
        validarStatus(novoStatus);

        Pedido pedido = new Pedido();
        pedido.setCodigo(codigo);
        pedido.setMesa(novoMesa);
        pedido.setQtdePessoas(novoQtdePessoas);
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
