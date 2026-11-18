/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import database.ConexaoBanco;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Usuario;

/**
 *
 * @author guilherme.freitas4
 */
public class UsuarioDaoJdbc implements UsuarioDao {

    private static final String SQL_AUTENTICAR = "SELECT codigo, nome, login FROM usuarios WHERE login = ? AND senha = ?";
    private static final String SQL_SALVAR = "INSERT INTO usuarios (nome, login, senha) VALUES (?, ?, ?)";

    @Override
    public Usuario autenticar(String login, String senha) throws SQLException {
        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(SQL_AUTENTICAR)) {
            pstmt.setString(1, login);
            pstmt.setString(2, gerarHash(senha));
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
                return null;
            }
        }
    }

    @Override
    public void salvar(String nome, String login, String senha) throws SQLException {
        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(SQL_SALVAR)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, login);
            pstmt.setString(3, gerarHash(senha));
            pstmt.executeUpdate();
        }
    }

    private Usuario mapearUsuario(ResultSet resultado) throws SQLException {
        Usuario usuario = new Usuario(resultado.getString("nome"), resultado.getString("login"));
        usuario.setCodigo(resultado.getInt("codigo"));
        return usuario;
    }

    private String gerarHash(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 indisponível na JVM", e);
        }
    }
}
