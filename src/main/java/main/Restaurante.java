/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package main;

import java.awt.EventQueue;
import views.TelaLogin;

/**
 *
 * @author Guilherme Luvison
 */
public class Restaurante {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TelaLogin view = new TelaLogin();
            view.setVisible(true);
        });
    }
}
