package enuns;

import lombok.Getter;

@Getter
public enum StatusEntrega {
    AGUARDANDO("Cliente"),
    ENTREGUE("Motoboy");

    private final String nome;

    StatusEntrega(String nome) {
        this.nome = nome;
    }

}