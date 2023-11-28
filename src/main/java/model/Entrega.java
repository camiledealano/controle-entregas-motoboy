package model;

import lombok.Data;
import org.bson.types.ObjectId;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Entrega {

	@Id
	@GeneratedValue(generator = "uuid2")
	private String id;

	@Column(name = "endereco")
	private Endereco endereco;

	@Column(name = "pedido")
	private Pedido pedido;

	@Column(name = "entregador")
	private Motoboy entregador;

	@Column(name = "dataEntrega")
	private Date dataEntrega;

	@Column(name = "status_entrega")
	private String statusEntrega;

	@Column(name = "avalicao_cliente")
	private double avaliacaoCliente;

	@Column(name = "comentario_cliente")
	private String comentarioCliente;
}