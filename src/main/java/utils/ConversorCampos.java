/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author Guilherme Luvison
 */
public class ConversorCampos {

    private ConversorCampos() {
    }

    public static double converterPreco(String texto) {
        try {
            return Double.parseDouble(texto.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Preço inválido: digite um número (formato XX.XX).");
        }
    }

    public static int converterInteiro(String texto, String nomeCampo) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(nomeCampo + " inválida: digite um número.");
        }
    }
}
