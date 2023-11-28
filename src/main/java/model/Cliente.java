package model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cliente {

	@Id
	@GeneratedValue(generator = "uuid2")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	private String id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "endereco")
	private Endereco endereco;

	@Column(name = "telefone")
	private String telefone;

	@Column(name = "email")
	private String email;

}