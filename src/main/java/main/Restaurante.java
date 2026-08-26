/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package main;

import controllers.ClienteController;
import controllers.ItemCardapioController;
import controllers.ReservaController;
import resources.Entrada;

/**
 *
 * @author Guilherme Luvison
 */
public class Restaurante {

    public static void main(String[] args) {
        // HomeView hv = new HomeView();
        // hv.setVisible(true);

        ClienteController cc = new ClienteController();
        ItemCardapioController icc = new ItemCardapioController();
        ReservaController rc = new ReservaController();

        int opcao = 0;
        do {
            opcao = Entrada.leiaInt("Restaurante:\n"
                    + "[1] Cadastrar um novo cliente\n"
                    + "[2] Listar clientes cadastrados\n"
                    + "[3] Buscar cliente por nome\n"
                    + "[4] Atualizar dados de um cliente\n"
                    + "[5] Remover um cliente do sistema\n"
                    + "\n"
                    + "[6] Cadastrar um novo item de cardápio\n"
                    + "[7] Listar itens de cadárpio cadastrados\n"
                    + "[8] Buscar item de cardápio por nome\n"
                    + "[9] Atualizar dados de um item do cardápio\n"
                    + "[10] Remover um item de cardápio do sistema\n"
                    + "\n"
                    + "[11] Cadastrar uma nova reserva\n"
                    + "[12] Listar reservas cadastradas\n"
                    + "[13] Buscar reserva por nome de cliente\n"
                    + "[14] Atualizar dados de uma reserva\n"
                    + "[15] Remover uma reserva do sistema\n"
                    + "\n"
                    + "[16] Sair do sistema");

            switch (opcao) {
                // Opções para Cliente
                case 1:
                    cc.cadastrar();
                    break;
                case 2:
                    cc.listar();
                    break;
                case 3:
                    cc.buscarPorNome();
                    break;
                case 4:
                    cc.atualizar();
                    break;
                case 5:
                    cc.remover();
                    break;

                // Opções para Item de Cardápio
                case 6:
                    icc.cadastrar();
                    break;
                case 7:
                    icc.listar();
                    break;
                case 8:
                    icc.buscarPorNome();
                    break;
                case 9:
                    icc.atualizar();
                    break;
                case 10:
                    icc.remover();
                    break;

                // Opções para Reserva    
                case 11:
                    rc.cadastrar();
                    break;
                case 12:
                    rc.listar();
                    break;
                case 13:
                    rc.buscarPorNome();
                    break;
                case 14:
                    rc.atualizar();
                    break;
                case 15:
                    rc.remover();
                    break;

                // Opção para Sair do Sistema
                case 16:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida! Escolha uma opção do menu.");
            }
        } while (opcao != 16);

        System.exit(0);
    }
}
