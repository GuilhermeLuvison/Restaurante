/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.ArrayList;
import models.Cardapio;

/**
 *
 * @author guilherme.freitas4
 */
public class CardapioController {
    
    private ArrayList<Cardapio> cardapios = new ArrayList();
    private int codigo = 1;
    
    public void salvar(Cardapio ca) {
        ca.setCodigo(codigo);
        cardapios.add(ca);
        codigo++;
    }
    
    public ArrayList<Cardapio> listar() {
        return cardapios;
    }
    
    public Cardapio procurar(int codigo) {
        for (int i = 0; i < cardapios.size(); i++) {
            Cardapio temp = cardapios.get(i);
            if (temp.getCodigo() == codigo) {
                return temp;
            }
        }
        return null;
    }
    
    public void editar(Cardapio ca) {
        Cardapio temp = procurar(ca.getCodigo());
        temp.setNome(ca.getNome());
        temp.setIngredientes(ca.getIngredientes());
        temp.setCategoria(ca.getCategoria());
        temp.setTipoPrato(ca.getTipoPrato());
        temp.setPreco(ca.getPreco());
        temp.setTempoPreparo(ca.getTempoPreparo());
    }
    
    public void excluir(int codigo) {
        Cardapio temp = procurar(codigo);
        if (temp != null) {
            cardapios.remove(temp);
        }
    }
}
