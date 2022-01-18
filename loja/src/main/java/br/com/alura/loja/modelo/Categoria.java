package br.com.alura.loja.modelo;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "categorias")
public class Categoria {
//Tratando o id dessa entidade como se fosse um id composto
	
	//Usando essa anotação para dizer que a linha abaixo é a chave primaria da entidade
	@EmbeddedId
	private CategoriaId id;
	
	public Categoria() {
	}
	
	public Categoria(String nome) {
		this.id = new CategoriaId(nome, "xpto");
	}
	
	public String getNome() {
		return this.id.getNome();
	}
}