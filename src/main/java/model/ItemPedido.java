package model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ItemPedido {

	@Id
	@GeneratedValue(generator = "uuid2")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	private String id;

	@Column(name = "produto")
	private String produto;

	@Column(name = "quantidade")
	private int quantidade;

	@Column(name = "precoUnitario")
	private double precoUnitario;
}
