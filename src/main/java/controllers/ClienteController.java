/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.ArrayList;
import models.Cliente;

/**
 *
 * @author guilherme.freitas4
 */
public class ClienteController {

    private ArrayList<Cliente> clientes = new ArrayList();
    private int codigo = 1;

    public void salvar(Cliente cl) {
        cl.setCodigo(codigo);
        clientes.add(cl);
        codigo++;
    }

    public ArrayList<Cliente> listar() {
        return clientes;
    }

    public Cliente procurar(int codigo) {
        for (int i = 0; i < clientes.size(); i++) {
            Cliente temp = clientes.get(i);
            if (temp.getCodigo() == codigo) {
                return temp;
            }
        }
        return null;
    }

    public void editar(Cliente cl) {
        Cliente temp = procurar(cl.getCodigo());
        temp.setNome(cl.getNome());
        temp.setCpf(cl.getCpf());
        temp.setTelefone(cl.getTelefone());
        temp.setEmail(cl.getEmail());
        temp.setDataNascimento(cl.getDataNascimento());
        temp.setDataCadastro(cl.getDataCadastro());
    }

    public void excluir(int codigo) {
        Cliente temp = procurar(codigo);
        if (temp != null) {
            clientes.remove(temp);
        }
    }
}
