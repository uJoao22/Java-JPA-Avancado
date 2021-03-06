package br.com.alura.loja.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import br.com.alura.loja.modelo.Pedido;
import br.com.alura.loja.vo.RelatorioDeVendasVo;

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

	//Este método devolve uma lista da class RelatorioDeVendasVo
	public List<RelatorioDeVendasVo> relatiorioDeVendas() {
		//Usando uma funcionalidade da JPA, o SELECT new, para dar new em uma class e passar para ele por parametro os dados que
		//é desejado fazer a consulta, é necessario informar todo o caminho da class e assim será selecionado o nome da entidade 
		//produto, a soma de quantidade da entidade item e a maior data da entidade pedido, e então fazendo o relacionamento dessas 
		//entidades a partir da entidade Pedido e agrupando pelo nome e ordenando pela quantidade
		String jpql = "SELECT new br.com.alura.loja.vo.RelatorioDeVendasVo(produto.nome, SUM(item.quantidade), MAX(pedido.data)) FROM Pedido pedido "
				+ " JOIN pedido.itens item JOIN item.produto produto "
				+ " GROUP BY produto.nome ORDER BY item.quantidade DESC";
		return em.createQuery(jpql, RelatorioDeVendasVo.class).getResultList();
	}

	public Pedido buscarPedidoComCliente(Long id) {
		//Criando a query planejada, fazendo a consulta já carregando o atributo lazy que vou precisar em seguida
		//Usando o JOIN FECTH atributo a ser carregado
		return em.createQuery("SELECT p FROM Pedido p JOIN FETCH p.cliente WHERE p.id = :id", Pedido.class)
				.setParameter("id", id).getSingleResult();
	}
	
	
	
}