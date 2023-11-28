package model;

import lombok.Data;
import org.bson.types.ObjectId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Motoboy {

	@Id
	@GeneratedValue(generator = "uuid2")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	private ObjectId id;

	@Column(name = "nome")
	private String nome;

	@Column(name = "placaMoto")
	private String placaMoto;

	@Column(name = "disponivel")
	private boolean disponivel;
}