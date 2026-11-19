/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.UsuarioDao;
import dao.UsuarioDaoJdbc;
import database.SqlState;
import java.sql.SQLException;
import models.Usuario;
import org.apache.commons.validator.GenericValidator;
import utils.Validador;

/**
 *
 * @author Guilherme Luvison
 */
public class UsuarioController {

    private final UsuarioDao usuarioDao;

    public UsuarioController() {
        this(new UsuarioDaoJdbc());
    }

    public UsuarioController(UsuarioDao usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    public Usuario autenticar(String login, String senha) throws SQLException {
        validarLogin(login);
        validarSenha(senha);

        Usuario usuario = usuarioDao.autenticar(login.trim(), senha);
        if (usuario == null) {
            throw new IllegalArgumentException("Login ou senha incorretos.");
        }
        return usuario;
    }

    public void salvar(String nome, String login, String senha) throws SQLException {
        Validador.validarNome(nome);
        validarLogin(login);
        validarSenha(senha);

        try {
            usuarioDao.salvar(nome.trim(), login.trim(), senha);
        } catch (SQLException e) {
            if (SqlState.UNIQUE_VIOLATION.equals(e.getSQLState())) {
                throw new IllegalArgumentException("Já existe um usuário cadastrado com este login.");
            }
            throw e;
        }
    }

    private void validarLogin(String login) {
        if (GenericValidator.isBlankOrNull(login)) {
            throw new IllegalArgumentException("Login inválido: não pode ficar em branco!");
        }
    }

    private void validarSenha(String senha) {
        if (GenericValidator.isBlankOrNull(senha)) {
            throw new IllegalArgumentException("Senha inválida: não pode ficar em branco!");
        }
    }
}
