/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Guilherme Luvison
 */
public class ItemPedido {

    private int codigo;
    private int codigoPedido;
    private int codigoItemCardapio;
    private String nomeItemCardapio; // Exibição (JOIN com itenscardapio)
    private int quantidade;
    private double precoUnitario;
    private double precoTotal;
    private String observacao;

    public ItemPedido(int codigoPedido, String nomeItemCardapio, int quantidade, double precoUnitario, double precoTotal, String observacao) {
        this.codigoPedido = codigoPedido;
        this.nomeItemCardapio = nomeItemCardapio;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.precoTotal = precoTotal;
        this.observacao = observacao;
    }

    public ItemPedido() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(int codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public int getCodigoItemCardapio() {
        return codigoItemCardapio;
    }

    public void setCodigoItemCardapio(int codigoItemCardapio) {
        this.codigoItemCardapio = codigoItemCardapio;
    }

    public String getNomeItemCardapio() {
        return nomeItemCardapio;
    }

    public void setNomeItemCardapio(String nomeItemCardapio) {
        this.nomeItemCardapio = nomeItemCardapio;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
