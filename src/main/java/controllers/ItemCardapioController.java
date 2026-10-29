/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dao.ItemCardapioDao;
import dao.ItemCardapioDaoJdbc;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import models.ItemCardapio;
import org.apache.commons.validator.GenericValidator;
import utils.Validador;

/**
 *
 * @author Guilherme Luvison
 */
public class ItemCardapioController {

    // Tipos de Prato válidos para cadastro - conforme chave CHECK do PostgreSQL
    public static final List<String> TIPOS_PRATO_VALIDOS = Arrays.asList("Entrada", "Prato Principal", "Sobremesa");

    private final ItemCardapioDao itemCardapioDao;

    public ItemCardapioController() {
        this(new ItemCardapioDaoJdbc());
    }

    public ItemCardapioController(ItemCardapioDao itemCardapioDao) {
        this.itemCardapioDao = itemCardapioDao;
    }

    public void cadastrar(String nome, String ingredientes, String categoria, String tipoPrato, double preco, String tempoPreparo) throws SQLException {
        Validador.validarNome(nome);
        validarIngredientes(ingredientes);
        validarCategoria(categoria);
        validarTipoPrato(tipoPrato);
        validarPreco(preco);
        validarTempoPreparo(tempoPreparo);

        ItemCardapio itemCardapio = new ItemCardapio(nome, ingredientes, categoria, tipoPrato, preco, tempoPreparo);
        itemCardapioDao.cadastrar(itemCardapio);
    }

    public List<ItemCardapio> listar() throws SQLException {
        return itemCardapioDao.listar();
    }

    public List<ItemCardapio> buscarPorNome(String termo) throws SQLException {
        return itemCardapioDao.buscarPorNome(termo);
    }

    public void atualizar(int codigo, String novoNome, String novosIngredientes, String novaCategoria, double novoPreco, String novoTempoPreparo) throws SQLException {
        Validador.validarNome(novoNome);
        validarIngredientes(novosIngredientes);
        validarCategoria(novaCategoria);
        validarPreco(novoPreco);
        validarTempoPreparo(novoTempoPreparo);

        ItemCardapio itemCardapio = new ItemCardapio();
        itemCardapio.setCodigo(codigo);
        itemCardapio.setNome(novoNome);
        itemCardapio.setIngredientes(novosIngredientes);
        itemCardapio.setCategoria(novaCategoria);
        itemCardapio.setPreco(novoPreco);
        itemCardapio.setTempoPreparo(novoTempoPreparo);

        if (!itemCardapioDao.atualizar(itemCardapio)) {
            throw new IllegalArgumentException("Nenhum item de cardápio encontrado com o Código " + codigo + ".");
        }
    }

    public void remover(int codigo) throws SQLException {
        if (!itemCardapioDao.remover(codigo)) {
            throw new IllegalArgumentException("Nenhum item de cardápio encontrado com o Código " + codigo + ".");
        }
    }

    private void validarIngredientes(String ingredientes) {
        if (GenericValidator.isBlankOrNull(ingredientes)) {
            throw new IllegalArgumentException("Ingredientes inválidos: não pode ficar em branco!");
        }
    }

    private void validarCategoria(String categoria) {
        if (GenericValidator.isBlankOrNull(categoria)) {
            throw new IllegalArgumentException("Categoria inválida: não pode ficar em branco!");
        }

        // Não pode ser digitado mais de 100 caracteres - VARCHAR(100) no PostgreSQL
        if (categoria.length() > 100) {
            throw new IllegalArgumentException("Categoria inválida: máximo de 100 caracteres.");
        }
    }

    private void validarTipoPrato(String tipoPrato) {
        if (GenericValidator.isBlankOrNull(tipoPrato)) {
            throw new IllegalArgumentException("Tipo de Prato inválido: não pode ficar em branco!");
        }

        // Somente pratos válidos registrados na chave CHECK do banco de dados
        if (!TIPOS_PRATO_VALIDOS.contains(tipoPrato)) {
            throw new IllegalArgumentException("Tipo de Prato inválido: deve ser Entrada, Prato Principal ou Sobremesa.");
        }
    }

    private void validarPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço inválido: não pode ser negativo!");
        }
    }

    private void validarTempoPreparo(String tempoPreparo) {
        if (GenericValidator.isBlankOrNull(tempoPreparo)) {
            throw new IllegalArgumentException("Tempo de Preparo inválido: não pode ficar em branco!");
        }

        // Não pode ser digitado mais de 100 caracteres - VARCHAR(100) no PostgreSQL
        if (tempoPreparo.length() > 100) {
            throw new IllegalArgumentException("Tempo de Preparo inválido: máximo de 100 caracteres.");
        }
    }
}
