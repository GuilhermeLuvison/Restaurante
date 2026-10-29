/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Guilherme Luvison
 */
public class Pedido {

    private int codigo;
    private int codigoCliente; // Chave estrangeira obrigatória para clientes.codigo - usado no INSERT/UPDATE
    private String nomeCliente; // Exibição (JOIN com clientes)
    private Integer codigoReserva; // Chave estrangeira opcional para reserva.codigo - usado no INSERT caso o campo não esteja vazio
    private int mesa;
    private int quantidadePessoas;
    private String momentoPedido;
    private String status;

    public Pedido(int codigoCliente, String nomeCliente, Integer codigoReserva, int mesa, int quantidadePessoas, String momentoPedido, String status) {
        this.codigoCliente = codigoCliente;
        this.nomeCliente = nomeCliente;
        this.codigoReserva = codigoReserva;
        this.mesa = mesa;
        this.quantidadePessoas = quantidadePessoas;
        this.momentoPedido = momentoPedido;
        this.status = status;
    }

    public Pedido() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Integer getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(Integer codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public int getMesa() {
        return mesa;
    }

    public void setMesa(int mesa) {
        this.mesa = mesa;
    }

    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public void setQuantidadePessoas(int quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }

    public String getMomentoPedido() {
        return momentoPedido;
    }

    public void setMomentoPedido(String momentoPedido) {
        this.momentoPedido = momentoPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Método para mostrar código e nome do cliente na ComboBox de CadastrarItemPedido
    @Override
    public String toString() {
        return codigo + " - " + nomeCliente;
    }
}
