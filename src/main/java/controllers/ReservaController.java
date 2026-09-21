/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import models.Reserva;
import org.apache.commons.validator.GenericValidator;

/**
 *
 * @author Guilherme Luvison
 */
public class ReservaController {

    // Atributos
    String url = "jdbc:postgresql://localhost:5432/restaurante";
    String usuario = "postgres";
    String senha = "postgres";

    // Formatar data e hora para dd/mm/yyyy
    private static final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Status válidos para cadastro e atualização, conforme chave CHECK do PostgreSQL
    public static final List<String> statusValidos = Arrays.asList("Confirmada", "Pendente", "Cancelada");

    // Método de Cadastro
    public void cadastrar(int codigoCliente, int mesa, int qtdePessoas, String observacao, String dataReserva, String status) throws SQLException {
        validarCodigoCliente(codigoCliente); // Validação para código de cliente existente
        validarMesa(mesa);
        validarQtdePessoas(qtdePessoas);
        LocalDate data = validarDataReserva(dataReserva); // Inserção de data da reserva para fazer a validação
        validarStatus(status);

        String sql = "INSERT INTO reservas (codigo_cliente, mesa, quantidade_pessoas, observacao, data_reserva, status) VALUES (?, ?, ?, ?, ?, ?)"; // INSERT com campo de chave estrangeira

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigoCliente); // Inserção do código do cliente vindo da tabela clientes
            pstmt.setInt(2, mesa);
            pstmt.setInt(3, qtdePessoas);
            pstmt.setString(4, observacao);
            pstmt.setDate(5, Date.valueOf(data)); // pstmt agora insere o dado como Date
            pstmt.setString(6, status);
            pstmt.executeUpdate();
        } catch (SQLException e) { // Violação de Chave Estrangeira do PostgreSQL
            if ("23503".equals(e.getSQLState())) {
                throw new IllegalArgumentException("Cliente não encontrado. Selecione um cliente válido.");
            }
            throw e;
        }
    }

    // Método de Listagem (agora usando JOIN para trazer o nome do cliente)
    public List<Reserva> listar() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT r.codigo, r.codigo_cliente, c.nome AS nome_cliente, r.mesa, r.quantidade_pessoas, r.observacao, r.data_reserva, r.status FROM reservas r JOIN clientes c ON r.codigo_cliente = c.codigo ORDER BY r.codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                reservas.add(mapearReserva(rs));
            }
        }
        return reservas;
    }

    // Método de Busca (busca pelo nome do cliente usando JOIN, já que não existe mais o campo nome_cliente em reservas)
    public List<Reserva> buscarPorNome(String termo) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT r.codigo, r.codigo_cliente, c.nome AS nome_cliente, r.mesa, r.quantidade_pessoas, r.observacao, r.data_reserva, r.status FROM reservas r JOIN clientes c ON r.codigo_cliente = c.codigo WHERE c.nome ILIKE ? ORDER BY r.codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReserva(rs));
                }
            }
        }
        return reservas;
    }

    // Método de Atualização
    public void atualizar(int codigo, int novoQtdePessoas, String novoObservacao, String novoDataReserva, String novoStatus) throws SQLException {
        validarQtdePessoas(novoQtdePessoas);
        LocalDate data = validarDataReserva(novoDataReserva); // Inserção de nova data da reserva para fazer a validação
        validarStatus(novoStatus);

        String sql = "UPDATE reservas SET quantidade_pessoas = ?, observacao = ?, data_reserva = ?, status = ?  WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, novoQtdePessoas);
            pstmt.setString(2, novoObservacao);
            pstmt.setDate(3, Date.valueOf(data)); // pstmt agora atualiza o dado como Date
            pstmt.setString(4, novoStatus);
            pstmt.setInt(5, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhuma reserva encontrada com o Código " + codigo + ".");
            }
        }
    }

    // Método de Remoção
    public void remover(int codigo) throws SQLException {
        String sql = "DELETE FROM reservas WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas == 0) {
                throw new IllegalArgumentException("Nenhuma reserva encontrada com o Código " + codigo + ".");
            }
        }
    }

    // Métodos de Validação
    private void validarCodigoCliente(int codigoCliente) { // Método agora valida se existe cliente na tabela clientes
        if (codigoCliente <= 0) {
            throw new IllegalArgumentException("Selecione um cliente válido.");
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

    private LocalDate validarDataReserva(String dataReserva) {
        // Campo não pode estar vazio
        if (GenericValidator.isBlankOrNull(dataReserva)) {
            throw new IllegalArgumentException("Data de Reserva inválida: não pode ficar em branco!");
        }

        // A data deve estar em formato DD/MM/YYYY
        LocalDate data;
        try {
            data = LocalDate.parse(dataReserva, formatoData);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data de Reserva inválida: use o formato DD/MM/YYYY!");
        }

        return data;
    }

    private void validarStatus(String status) {
        // Campo não pode estar vazio
        if (GenericValidator.isBlankOrNull(status)) {
            throw new IllegalArgumentException("Status inválido: não pode ficar em branco!");
        }

        // Somente status válidos registrados na chave CHECK do banco de dados
        if (!statusValidos.contains(status)) {
            throw new IllegalArgumentException("Status inválido: deve ser Confirmada, Presente ou Cancelada.");
        }
    }

    // Método de Mapeamento
    private Reserva mapearReserva(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setCodigo(rs.getInt("codigo"));
        r.setCodigoCliente(rs.getInt("codigo_cliente")); // Mapea código do cliente existente
        r.setNomeCliente(rs.getString("nome_cliente"));
        r.setMesa(rs.getInt("mesa"));
        r.setQtdePessoas(rs.getInt("quantidade_pessoas"));
        r.setObservacao(rs.getString("observacao"));
        r.setDataReserva(formatarData(rs.getDate("data_reserva"))); // Puxa uma data ao invés de uma String com o método formatarData
        r.setStatus(rs.getString("status"));
        return r;
    }

    // Método de Formatação de Data
    private String formatarData(java.sql.Date data) {
        return data == null ? "" : data.toLocalDate().format(formatoData);
    }
}
