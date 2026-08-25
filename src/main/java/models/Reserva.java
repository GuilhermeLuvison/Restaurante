/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Guilherme Luvison
 */
public class Reserva {

    // Atributos
    private int codigo;
    private String nomeCliente;
    private int mesa;
    private int qtdePessoas;
    private String observacao;
    private String dataReserva;
    private String status;

    // Métodos Construtores
    public Reserva(String nomeCliente, int mesa, int qtdePessoas, String observacao, String dataReserva, String status) {
        this.nomeCliente = nomeCliente;
        this.mesa = mesa;
        this.qtdePessoas = qtdePessoas;
        this.observacao = observacao;
        this.dataReserva = dataReserva;
        this.status = status;
    }

    public Reserva() {
    }

    // Getters e Setters
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public int getQtdePessoas() {
        return qtdePessoas;
    }

    public void setQtdePessoas(int qtdePessoas) {
        this.qtdePessoas = qtdePessoas;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(String dataReserva) {
        this.dataReserva = dataReserva;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Método de Impressão
    public void imprimeAtributos() {
        System.out.println("DETALHES DA RESERVA:");
        System.out.println("Código: " + codigo);
        System.out.println("Nome do Cliente: " + nomeCliente);
        System.out.println("Mesa: " + mesa);
        System.out.println("Quantidade de Pessoas: " + qtdePessoas);
        System.out.println("Observação: " + observacao);
        System.out.println("Data da Reserva: " + dataReserva);
        System.out.println("Status: " + status);
        System.out.println("");
    }
}
