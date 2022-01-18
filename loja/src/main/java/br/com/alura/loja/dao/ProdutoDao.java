package br.com.alura.loja.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.alura.loja.modelo.Produto;

public class ProdutoDao {

	private EntityManager em;

	public ProdutoDao(EntityManager em) {
		this.em = em;
	}

	public void cadastrar(Produto produto) {
		this.em.persist(produto);
	}

	public void atualizar(Produto produto) {
		this.em.merge(produto);
	}

	public void remover(Produto produto) {
		produto = em.merge(produto);
		this.em.remove(produto);
	}
	
	public Produto buscarPorId(Long id) {
		return em.find(Produto.class, id);
	}
	
	public List<Produto> buscarTodos() {
		String jpql = "SELECT p FROM Produto p";
		return em.createQuery(jpql, Produto.class).getResultList();
	}
	
	public List<Produto> buscarPorNome(String nome) {
		String jpql = "SELECT p FROM Produto p WHERE p.nome = :nome";
		return em.createQuery(jpql, Produto.class)
				.setParameter("nome", nome)
				.getResultList();
	}
	
	public List<Produto> buscarPorNomeDaCategoria(String nome) {
		//Utilizando o named Query, ao invés do método createQuery, deve ser usado o método 
		//createNamedQuery(apelido da query dado na class, o que será retornado)
		return em.createNamedQuery("Produto.produtosPorCategoria", Produto.class)
				.setParameter("nome", nome)
				.getResultList();
	}
	
	public BigDecimal buscarPrecoDoProdutoComNome(String nome) {
		String jpql = "SELECT p.preco FROM Produto p WHERE p.nome = :nome";
		return em.createQuery(jpql, BigDecimal.class)
				.setParameter("nome", nome)
				.getSingleResult();
	}

	//Usando o método tradicional, com o jpql e a "gambiarra" do where 1=1
	public List<Produto> buscarPorParametros(String nome, BigDecimal preco, LocalDate dataCadastro){
		//Fazendo a "gambiarra" com o WHERE para deixar os parametros dinamicos
		String jpql = "SELECT p FROM Produto p WHERE 1=1";

		//Se a string não for null e nem for uma string vazia, faça
		//.trim() remove todos os espaços da string
		//.isEmpty() verifica se a string está vazia
		if(nome != null && !nome.trim().isEmpty())
			jpql += " AND p.nome = :nome ";
		if(preco != null)
			jpql += " AND p.preco = :preco ";
		if(dataCadastro != null)
			jpql += " AND p.dataCadastro = : dataCadastro ";
		
		TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);
		if(nome != null && !nome.trim().isEmpty())
			query.setParameter("nome", nome);
		if(preco != null)
			query.setParameter("preco", preco);
		if(dataCadastro != null)
			query.setParameter("dataCadastro", dataCadastro);
		
		return query.getResultList();
	}
	
	//Usando o método adicionado na JPA 2.0, o Criteria, sem usar o jpql
	public List<Produto> buscarPorParametrosComCriteria(String nome, BigDecimal preco, LocalDate dataCadastro){
		
		//A partir do Entity Manager chamando o método getCriteriaBuilder, que vai criar o objeto criteria e passar para um 
		//objeto do tipo CriteriaBuilder
		CriteriaBuilder builder = em.getCriteriaBuilder();
		
		//builder, crie uma query pra mim, uma criteria query que devolve um objeto do tipo Produto para um objeto do tipo
		//CriteriaQuery<Produto>
		CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
		
		//Informando para a query que ela irá fazer a consulta na entidade Produto, definindo o som FROM da query
		//Se o select for igual ao from, for selecionar todos atributos da entidade definida no from, não é preciso informar
		//o select, ele entende que é o mesmo do from, caso precisase, seria assim:
		//query.select("query do select");
		//O from devolve um objeto do tipo Root<Produto>
		Root<Produto> from = query.from(Produto.class);

		//Criando um builder.and() que cria os and's na condição do where e devolve um objeto do tipo Predicate
		Predicate filtros = builder.and();
		
		if(nome != null && !nome.trim().isEmpty())
			//Builder, crie um novo and, usando o and atual, e executando uma condição apartir do builder, que seria a condição
			//de igualdade, se o atributo nome for igual ao nome recebido por parametro e passe esse resultado para a variavel
			//filtros, reatribuindo ela
			filtros = builder.and(filtros, builder.equal(from.get("nome"), nome));
		if(preco != null)
			filtros = builder.and(filtros, builder.equal(from.get("preco"), preco));
		if(dataCadastro != null)
			filtros = builder.and(filtros, builder.equal(from.get("dataCadastro"), dataCadastro));
		
		//Pegando a query e dizendo para ele que os filtros do and fazem parte do Where
		query.where(filtros);
		
		//Criando uma query baseada no objeto Criteria Query e execute retornando uma lista de resultados
		return em.createQuery(query).getResultList();
	}
}