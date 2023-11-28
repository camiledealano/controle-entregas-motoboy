package apresentation;

import model.enuns.TipoCadastro;
import repository.ClienteRepository;
import repository.MotoboyRepository;
import repository.PedidoRepository;

import javax.swing.*;

public class Menu {

    public void exibir() {
        boolean sair = false;

        while (!sair) {
            TipoCadastro tipoCadastro = exibirMenuCadastros();

            switch (tipoCadastro) {
                case CLIENTE:
                    ClienteRepository.crud();
                case MOTOBOY:
                    MotoboyRepository.crud();
                case PEDIDO:
                    PedidoRepository.crud();
                    break;
                case SAIR:
                    sair = true;
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opção inválida!");
                    break;
            }
        }
    }


    private TipoCadastro exibirMenuCadastros() {
        TipoCadastro[] values = TipoCadastro.values();

        String[] options = new String[values.length];
        int i = 0;

        for (TipoCadastro tipo : values) {
            options[i++] = tipo.getNome();
        }

        int escolha = JOptionPane.showOptionDialog(
                null, "Escolha uma opção:", "Menu", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, options[0]
        );

        return values[escolha];
    }
}
