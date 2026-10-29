/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import org.apache.commons.validator.GenericValidator;

/**
 *
 * @author Guilherme Luvison
 */
public class Validador {

    private Validador() {
    }

    public static void validarNome(String nome) {
        if (GenericValidator.isBlankOrNull(nome)) {
            throw new IllegalArgumentException("Nome inválido: não pode ficar em branco!");
        }
    }

    public static void validarCodigoCliente(int codigoCliente) {
        if (codigoCliente <= 0) {
            throw new IllegalArgumentException("Selecione um cliente válido.");
        }
    }

    public static void validarMesa(int mesa) {
        if (mesa <= 0) {
            throw new IllegalArgumentException("Número da mesa inválido: não pode ser negativo ou igual a 0!");
        }
    }

    public static void validarQuantidadePessoas(int quantidadePessoas) {
        if (quantidadePessoas <= 0) {
            throw new IllegalArgumentException("Quantidade de pessoas inválida: não pode ser negativo ou igual a 0!");
        }
    }
}
