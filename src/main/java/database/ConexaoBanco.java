/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Guilherme Luvison
 */
public class ConexaoBanco {

    private static final String URL = "jdbc:postgresql://localhost:5432/restaurante";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    public static Connection obter() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
