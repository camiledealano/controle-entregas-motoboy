package model.enuns;

import lombok.Getter;

@Getter
public enum TipoCadastro {
    CLIENTE("Cliente"),
    MOTOBOY("Motoboy"),
    PEDIDO("Pedido"),
    SAIR("Sair");

    private final String nome;

    TipoCadastro(String nome) {
        this.nome = nome;
    }

}