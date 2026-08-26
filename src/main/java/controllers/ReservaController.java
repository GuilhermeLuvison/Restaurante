/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Reserva;
import org.apache.commons.validator.GenericValidator;
import resources.Entrada;

/**
 *
 * @author Guilherme Luvison
 */
public class ReservaController {

    // Atributos
    String url = "jdbc:postgresql://localhost:5432/restaurante";
    String usuario = "postgres";
    String senha = "postgres";

    // Método de Cadastro
    public void cadastrar() {
        String nomeCliente;
        do {
            nomeCliente = Entrada.leiaString("Nome do Cliente:");
            if (GenericValidator.isBlankOrNull(nomeCliente)) {
                System.out.println("Nome inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(nomeCliente));

        int mesa;
        do {
            mesa = Entrada.leiaInt("Número da mesa:");
            if (mesa <= 0) {
                System.out.println("Número da mesa inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (mesa <= 0);

        int qtdePessoas;
        do {
            qtdePessoas = Entrada.leiaInt("Quantidade de Pessoas:");
            if (qtdePessoas <= 0) {
                System.out.println("Quantidade de pessoas inválida: não pode ficar em branco! Tente novamente.");
            }
        } while (qtdePessoas <= 0);

        String observacao = Entrada.leiaString("Observação (Opcional):");

        String dataReserva;
        do {
            dataReserva = Entrada.leiaString("Data da Reserva:");
            if (GenericValidator.isBlankOrNull(dataReserva)) {
                System.out.println("Data da Reserva inválida: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(dataReserva));

        String status;
        do {
            status = Entrada.leiaString("Status:");
            if (GenericValidator.isBlankOrNull(status)) {
                System.out.println("Status inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(status));

        String sql = "INSERT INTO reservas (nome_cliente, mesa, quantidade_pessoas, observacao, data_reserva, status) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, nomeCliente);
            pstmt.setInt(2, mesa);
            pstmt.setInt(3, qtdePessoas);
            pstmt.setString(4, observacao);
            pstmt.setString(5, dataReserva);
            pstmt.setString(6, status);
            pstmt.executeUpdate();
            System.out.println("Reserva cadastrada com sucesso!");
            System.out.println("");
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Listagem
    public void listar() {
        String sql = "SELECT codigo, nome_cliente, mesa, quantidade_pessoas, observacao, data_reserva, status FROM reservas ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            System.out.println("===[RESERVAS CADASTRADAS]===");
            while (rs.next()) {
                imprimeReserva(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Busca
    public void buscarPorNome() {
        String termo = Entrada.leiaString("Digite o nome do cliente (ou parte dele) para buscar:");
        String sql = "SELECT codigo, nome_cliente, mesa, quantidade_pessoas, observacao, data_reserva, status FROM reservas WHERE nome_c"
                + "liente ILIKE ? ORDER BY codigo";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setString(1, "%" + termo + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                boolean encontrouAlgum = false;
                System.out.println("===[RESERVAS ENCONTRADAS]===");
                while (rs.next()) {
                    imprimeReserva(rs);
                    encontrouAlgum = true;
                }
                if (!encontrouAlgum) {
                    System.out.println("Nenhuma reserva encontrada com esse cliente.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Atualização
    public void atualizar() {
        int codigo = Entrada.leiaInt("Digite o Código da reserva que deseja atualizar:");

        int novoQtdePessoas;
        do {
            novoQtdePessoas = Entrada.leiaInt("Nova quantidade de pessoas:");
            if (novoQtdePessoas <= 0) {
                System.out.println("Quantidade de pessoas inválida: não pode ficar em branco! Tente novamente.");
            }
        } while (novoQtdePessoas <= 0);

        String novoObservacao = Entrada.leiaString("Nova Observação (Repita caso não queira atualizar/adicionar):");

        String novoDataReserva;
        do {
            novoDataReserva = Entrada.leiaString("Nova data da reserva:");
            if (GenericValidator.isBlankOrNull(novoDataReserva)) {
                System.out.println("Data de reserva inválida: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(novoDataReserva));

        String novoStatus;
        do {
            novoStatus = Entrada.leiaString("Novo status:");
            if (GenericValidator.isBlankOrNull(novoStatus)) {
                System.out.println("Status inválido: não pode ficar em branco! Tente novamente.");
            }
        } while (GenericValidator.isBlankOrNull(novoStatus));

        String sql = "UPDATE reservas SET quantidade_pessoas = ?, observacao = ?, data_reserva = ?, status = ?  WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, novoQtdePessoas);
            pstmt.setString(2, novoObservacao);
            pstmt.setString(3, novoDataReserva);
            pstmt.setString(4, novoStatus);
            pstmt.setInt(5, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Dados atualizados com sucesso!");
                System.out.println("");
            } else {
                System.out.println("Nenhuma reserva encontrada com o Código " + codigo + ".");
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Remoção
    public void remover() {
        int codigo = Entrada.leiaInt("Digite o Código da reserva que deseja remover:");
        boolean confirma = Entrada.leiaBoolean("Tem certeza que deseja remover a reserva de Código " + codigo + "?");

        if (!confirma) {
            System.out.println("Remoção cancelada.");
            return;
        }

        String sql = "DELETE FROM reservas WHERE codigo = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuario, senha); PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            pstmt.setInt(1, codigo);
            int linhas = pstmt.executeUpdate();
            if (linhas > 0) {
                System.out.println("Reserva removida com sucesso!");
                System.out.println("");
            } else {
                System.out.println("Nenhum reserva encontrada com o Código " + codigo + ".");
            }
        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    // Método de Impressão de Cliente Específico
    private void imprimeReserva(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setCodigo(rs.getInt("codigo"));
        r.setNomeCliente(rs.getString("nome_cliente"));
        r.setMesa(rs.getInt("mesa"));
        r.setQtdePessoas(rs.getInt("quantidade_pessoas"));
        r.setObservacao(rs.getString("observacao"));
        r.setDataReserva(rs.getString("data_reserva"));
        r.setStatus(rs.getString("status"));
        r.imprimeAtributos();
    }
}
