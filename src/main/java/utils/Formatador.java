/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Guilherme Luvison
 */
public class Formatador {

    public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Formatador() {
    }

    public static String formatarData(java.sql.Date data) {
        return data == null ? "" : data.toLocalDate().format(FORMATO_DATA);
    }

    public static String formatarDataHora(Timestamp dataHora) {
        return dataHora == null ? "" : dataHora.toLocalDateTime().format(FORMATO_DATA_HORA);
    }
}
