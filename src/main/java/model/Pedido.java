package model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Entity
@Data
public class Pedido {

	@Id
	@GeneratedValue(generator = "uuid2")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	private String id;

	@Column(name = "cliente")
	private Cliente cliente;

	@Column(name = "itens_pedido")
	private List<ItemPedido> itens;

	@Column(name = "status")
	private String status;

	@Column(name = "observacoes")
	private String observacoes;
}
