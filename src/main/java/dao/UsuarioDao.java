/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.sql.SQLException;
import models.Usuario;

/**
 *
 * @author Guilherme Luvison
 */
public interface UsuarioDao {

    Usuario autenticar(String login, String senha) throws SQLException;

    void salvar(String nome, String login, String senha) throws SQLException;
}
