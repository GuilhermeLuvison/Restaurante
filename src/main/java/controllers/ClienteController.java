/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.ClienteDao;
import dao.ClienteDaoJdbc;
import database.SqlState;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import models.Cliente;
import org.apache.commons.validator.GenericValidator;
import utils.Validador;

/**
 *
 * @author Guilherme Luvison
 */
public class ClienteController {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final ClienteDao clienteDao;

    public ClienteController() {
        this(new ClienteDaoJdbc());
    }

    public ClienteController(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public void cadastrar(String nome, String cpf, String telefone, String email, String dataNascimento) throws SQLException {
        Validador.validarNome(nome);
        cpf = formatarCpf(cpf);
        validarCpf(cpf);
        validarDataNascimento(dataNascimento);

        Cliente cliente = new Cliente(nome, cpf, telefone, email, dataNascimento, null);

        try {
            clienteDao.cadastrar(cliente);
        } catch (SQLException e) {
            if (SqlState.UNIQUE_VIOLATION.equals(e.getSQLState())) {
                throw new IllegalArgumentException("Já existe um cliente cadastrado com este CPF.");
            }
            throw e;
        }
    }

    public List<Cliente> listar() throws SQLException {
        return clienteDao.listar();
    }

    public List<Cliente> buscarPorNome(String termo) throws SQLException {
        return clienteDao.buscarPorNome(termo);
    }

    public void atualizar(int codigo, String novoNome, String novoTelefone, String novoEmail) throws SQLException {
        Validador.validarNome(novoNome);

        Cliente cliente = new Cliente();
        cliente.setCodigo(codigo);
        cliente.setNome(novoNome);
        cliente.setTelefone(novoTelefone);
        cliente.setEmail(novoEmail);

        if (!clienteDao.atualizar(cliente)) {
            throw new IllegalArgumentException("Nenhum cliente encontrado com o Código " + codigo + ".");
        }

    }

    public void remover(int codigo) throws SQLException {
        if (!clienteDao.remover(codigo)) {
            throw new IllegalArgumentException("Nenhum cliente encontrado com o Código " + codigo + ".");
        }
    }

    private void validarCpf(String cpf) {
        if (GenericValidator.isBlankOrNull(cpf)) {
            throw new IllegalArgumentException("CPF inválido: não pode ficar em branco!");
        }

        // Certifica de que o CPF digitado tenha exatamente 11 dígitos
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido: deve conter 11 dígitos!");
        }
    }

    private LocalDate validarDataNascimento(String dataNascimento) {
        if (GenericValidator.isBlankOrNull(dataNascimento)) {
            throw new IllegalArgumentException("Data de Nascimento inválida: não pode ficar em branco!");
        }

        // A data deve estar em formato DD/MM/YYYY
        LocalDate data;
        try {
            data = LocalDate.parse(dataNascimento, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data de Nascimento inválida: use o formato DD/MM/YYYY!");
        }

        // A data de nascimento deve ter 18 anos ou mais de diferença da data atual (Chave Check no PostgreSQL)
        if (data.isAfter(LocalDate.now().minusYears(18))) {
            throw new IllegalArgumentException("Data de Nascimento inválida: cliente deve ter pelo menos 18 anos!");
        }

        return data;
    }

    private String formatarCpf(String cpf) {
        return cpf == null ? null : cpf.replaceAll("[^0-9]", "");
    }
}
