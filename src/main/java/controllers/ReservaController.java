/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.ReservaDao;
import dao.ReservaDaoJdbc;
import database.SqlState;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import models.Reserva;
import org.apache.commons.validator.GenericValidator;
import utils.Validador;

/**
 *
 * @author Guilherme Luvison
 */
public class ReservaController {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Status válidos para cadastro e atualização, conforme chave CHECK do PostgreSQL
    public static final List<String> STATUS_VALIDOS = Arrays.asList("Confirmada", "Pendente", "Cancelada");

    private final ReservaDao reservaDao;

    public ReservaController() {
        this(new ReservaDaoJdbc());
    }

    public ReservaController(ReservaDao reservaDao) {
        this.reservaDao = reservaDao;
    }

    public void cadastrar(int codigoCliente, int mesa, int qtdePessoas, String observacao, String dataReserva, String status) throws SQLException {
        Validador.validarCodigoCliente(codigoCliente);
        Validador.validarMesa(mesa);
        Validador.validarQtdePessoas(qtdePessoas);
        validarDataReserva(dataReserva);
        validarStatus(status);

        Reserva reserva = new Reserva();
        reserva.setCodigoCliente(codigoCliente);
        reserva.setMesa(mesa);
        reserva.setQtdePessoas(qtdePessoas);
        reserva.setObservacao(observacao);
        reserva.setDataReserva(dataReserva);
        reserva.setStatus(status);

        try {
            reservaDao.cadastrar(reserva);
        } catch (SQLException e) {
            if (SqlState.FK_VIOLATION.equals(e.getSQLState())) {
                throw new IllegalArgumentException("Cliente não encontrado. Selecione um cliente válido.");
            }
            throw e;
        }
    }

    public List<Reserva> listar() throws SQLException {
        return reservaDao.listar();
    }

    public List<Reserva> buscarPorNome(String termo) throws SQLException {
        return reservaDao.buscarPorNome(termo);
    }

    public void atualizar(int codigo, int novoQtdePessoas, String novoObservacao, String novoDataReserva, String novoStatus) throws SQLException {
        Validador.validarQtdePessoas(novoQtdePessoas);
        validarDataReserva(novoDataReserva);
        validarStatus(novoStatus);

        Reserva reserva = new Reserva();
        reserva.setCodigo(codigo);
        reserva.setQtdePessoas(novoQtdePessoas);
        reserva.setObservacao(novoObservacao);
        reserva.setDataReserva(novoDataReserva);
        reserva.setStatus(novoStatus);

        if (!reservaDao.atualizar(reserva)) {
            throw new IllegalArgumentException("Nenhuma reserva encontrada com o Código " + codigo + ".");
        }

    }

    public void remover(int codigo) throws SQLException {
        if (!reservaDao.remover(codigo)) {
            throw new IllegalArgumentException("Nenhuma reserva encontrada com o Código " + codigo + ".");
        }
    }

    private void validarDataReserva(String dataReserva) {
        if (GenericValidator.isBlankOrNull(dataReserva)) {
            throw new IllegalArgumentException("Data de Reserva inválida: não pode ficar em branco!");
        }

        LocalDate data;
        try {
            data = LocalDate.parse(dataReserva, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data de Reserva inválida: use o formato DD/MM/YYYY!");
        }
    }

    private void validarStatus(String status) {
        if (GenericValidator.isBlankOrNull(status)) {
            throw new IllegalArgumentException("Status inválido: não pode ficar em branco!");
        }

        // Somente status válidos registrados na chave CHECK do banco de dados
        if (!STATUS_VALIDOS.contains(status)) {
            throw new IllegalArgumentException("Status inválido: deve ser Confirmada, Pendente ou Cancelada.");
        }
    }
}
