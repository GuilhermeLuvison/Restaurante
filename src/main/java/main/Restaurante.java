/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package main;

import java.awt.EventQueue;
import views.TelaInicial;

/**
 *
 * @author Guilherme Luvison
 */
public class Restaurante {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TelaInicial view = new TelaInicial();
            view.setVisible(true);
        });
    }
}
