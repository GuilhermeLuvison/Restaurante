/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.ArrayList;
import models.Reserva;

/**
 *
 * @author guilherme.freitas4
 */
public class ReservaController {

    private ArrayList<Reserva> reservas = new ArrayList();
    private int codigo = 1;

    public void salvar(Reserva r) {
        r.setCodigo(codigo);
        reservas.add(r);
        codigo++;
    }

    public ArrayList<Reserva> listar() {
        return reservas;
    }

    public Reserva procurar(int codigo) {
        for (int i = 0; i < reservas.size(); i++) {
            Reserva temp = reservas.get(i);
            if (temp.getCodigo() == codigo) {
                return temp;
            }
        }
        return null;
    }

    public void editar(Reserva r) {
        Reserva temp = procurar(r.getCodigo());
        temp.setNomeCliente(r.getNomeCliente());
        temp.setMesa(r.getMesa());
        temp.setQtdePessoas(r.getQtdePessoas());
        temp.setObservacao(r.getObservacao());
        temp.setDataReserva(r.getDataReserva());
        temp.setStatus(r.getStatus());
    }

    public void excluir(int codigo) {
        Reserva temp = procurar(codigo);
        if (temp != null) {
            reservas.remove(temp);
        }
    }
}
