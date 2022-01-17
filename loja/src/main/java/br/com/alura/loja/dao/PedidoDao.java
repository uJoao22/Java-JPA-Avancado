package br.com.alura.loja.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import br.com.alura.loja.modelo.Pedido;

public class PedidoDao {

	private EntityManager em;

	public PedidoDao(EntityManager em) {
		this.em = em;
	}

	public void cadastrar(Pedido pedido) {
		this.em.persist(pedido);
	}
	
	public BigDecimal valorTotalVendido() {
		String jpql = "SELECT SUM(p.valorTotal) FROM Pedido p";
		return em.createQuery(jpql, BigDecimal.class).getSingleResult();
	}

	//Este método devolve uma lista com um array de objetos, pois ele irá retornar diferentes tipos de objetos
	public List<Object[]> relatiorioDeVendas() {
		//Selecionando o nome da entidade produto, a soma de quantidade da entidade item e a maior data da entidade pedido, e então
		//Fazendo o relacionamento dessas entidades a partir da entidade Pedido e agrupando pelo nome e ordenando pela quantidade
		String jpql = "SELECT produto.nome, SUM(item.quantidade), MAX(pedido.data) FROM Pedido pedido "
				+ " JOIN pedido.itens item JOIN item.produto produto "
				+ " GROUP BY produto.nome ORDER BY item.quantidade DESC";
		return em.createQuery(jpql, Object[].class).getResultList();
	}
	
}