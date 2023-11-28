package repository;

import javax.swing.*;

public class ClienteRepository {

    public static void crud() {
        boolean sair = false;

        while (!sair) {
            String[] options = { "Adicionar", "Editar", "Remover", "Sair" };

            int opcao = JOptionPane.showOptionDialog(null, "Escolha uma opção para cliente: ",
                    "Menu Cliente", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            switch (opcao) {
                case 0:
                    adicionar();
                    break;
                case 1:
                    editar();
                    break;
                case 2:
                    remover();
                    break;
                case 3:
                    sair = true;
                    break;
            }
        }
    }

    private static void adicionar() {

    }

    private static void editar() {
    }

    private static void remover() {
    }

    private static void busca() {

    }
}

