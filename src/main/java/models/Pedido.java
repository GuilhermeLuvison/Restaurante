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
    private int codigoCliente;
    private int codigoReserva;
    private int mesa;
    private int qtdePessoas;
    private String momentoPedido;
    private String status;

    public Pedido(int codigoCliente, int codigoReserva, int mesa, int qtdePessoas, String momentoPedido, String status) {
        this.codigoCliente = codigoCliente;
        this.codigoReserva = codigoReserva;
        this.mesa = mesa;
        this.qtdePessoas = qtdePessoas;
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

    public int getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(int codigoReserva) {
        this.codigoReserva = codigoReserva;
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
}
