/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import database.ConexaoBanco;
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

/**
 *
 * @author Guilherme Luvison
 */
public class PedidoController {

    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Status válidos para cadastro e atualização, conforme chave CHECK do PostgreSQL
    public static final List<String> STATUS_VALIDOS = Arrays.asList("Entregue", "Em preparo", "Cancelado");

    public void cadastrar(int codigoCliente, Integer codigoReserva, int mesa, int qtdePessoas, String status) throws SQLException {
        validarCodigoCliente(codigoCliente);
        validarCodigoReserva(codigoReserva);
        validarMesa(mesa);
        validarQtdePessoas(qtdePessoas);
        validarStatus(status);

        String sql = "INSERT INTO reservas (codigo_cliente, codigo_reserva, mesa, quantidade_pessoas, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigoCliente);
            if (codigoReserva == null) { // Código de reserva é opcional. If envia NULL quando não há reserva vinculada
                pstmt.setNull(2, Types.INTEGER);
            } else {
                pstmt.setInt(2, codigoReserva);
            }
            pstmt.setInt(3, mesa);
            pstmt.setInt(4, qtdePessoas);
            pstmt.setString(5, status);
            pstmt.executeUpdate();
        } catch (SQLException e) { // Violação de Chave Estrangeira do PostgreSQL (caso não tenha cliente ou reserva cadastrados)
            if ("23503".equals(e.getSQLState())) {
                throw new IllegalArgumentException("Cliente ou Reservas não encontrados. Verifique os dados selecionados.");
            }
            throw e;
        }
    }

    public List<Pedido> listar() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.codigo, p.codigo_cliente, c.nome AS nome_cliente, p.codigo_reserva, p.mesa, p.quantidade_pessoas, p.momento_pedido, p.status FROM pedidos p JOIN clientes c ON p.codigo_cliente = c.codigo ORDER BY p.codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                pedidos.add(mapearPedido(rs));
            }
        }
        return pedidos;
    }

    public List<Pedido> buscarPorNome(String termo) throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT p.codigo, p.codigo_cliente, c.nome AS nome_cliente, p.codigo_reserva, p.mesa, p.quantidade_pessoas, p.momento_pedido, p.status FROM pedidos p JOIN clientes c ON p.codigo_cliente = c.codigo WHERE c.nome ILIKE ? ORDER BY p.codigo";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    pedidos.add(mapearPedido(rs));
                }
            }
        }
        return pedidos;
    }

    public void atualizar(int codigo, int novoMesa, int novoQtdePessoas, String novoStatus) throws SQLException {
        validarMesa(novoMesa);
        validarQtdePessoas(novoQtdePessoas);
        validarStatus(novoStatus);

        String sql = "UPDATE pedidos SET mesa = ?, quantidade_pessoas = ?, status = ?  WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, novoMesa);
            pstmt.setInt(2, novoQtdePessoas);
            pstmt.setString(3, novoStatus);
            pstmt.setInt(4, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhum pedido encontrada com o Código " + codigo + ".");
            }
        }
    }

    public void remover(int codigo) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE codigo = ?";

        try (Connection conexao = ConexaoBanco.obter(); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhum pedido encontrado com o Código " + codigo + ".");
            }
        }
    }

    private void validarCodigoCliente(int codigoCliente) {
        if (codigoCliente <= 0) {
            throw new IllegalArgumentException("Selecione um cliente válido.");
        }
    }

    private void validarCodigoReserva(Integer codigoReserva) {
        if (codigoReserva != null && codigoReserva <= 0) {
            throw new IllegalArgumentException("Reserva inválida.");
        }
    }

    private void validarMesa(int mesa) {
        if (mesa <= 0) {
            throw new IllegalArgumentException("Número da mesa inválido: não pode ser negativo ou igual a 0!");
        }
    }

    private void validarQtdePessoas(int qtdePessoas) {
        if (qtdePessoas <= 0) {
            throw new IllegalArgumentException("Quantidade de pessoas inválida: não pode ser negativo ou igual a 0!");
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

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setCodigo(rs.getInt("codigo"));
        p.setCodigoCliente(rs.getInt("codigo_cliente"));
        p.setNomeCliente(rs.getString("nome_cliente"));
        p.setCodigoReserva(rs.getObject("codigo_reserva", Integer.class)); // getObject com o tipo Integer.class retorna NULL quando a coluna está vazia, getInt devolveria 0.
        p.setMesa(rs.getInt("mesa"));
        p.setQtdePessoas(rs.getInt("quantidade_pessoas"));
        p.setMomentoPedido(formatarDataHora(rs.getTimestamp("momento_pedido")));
        p.setStatus(rs.getString("status"));
        return p;
    }

    private String formatarDataHora(Timestamp dataHora) {
        return dataHora == null ? "" : dataHora.toLocalDateTime().format(FORMATO_DATA_HORA);
    }
}
