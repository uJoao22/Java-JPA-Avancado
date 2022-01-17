package br.com.alura.loja.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "pedidos")
public class Pedido {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate data = LocalDate.now();
	
	@Column(name="valor_total")
	private BigDecimal valorTotal = BigDecimal.ZERO;
	
	//Definindo o carregamento como Lazy, para que só seja carregado, caso for chamado
	@ManyToOne(fetch = FetchType.LAZY)
	private Cliente cliente;

	//Informando para a JPA que este é um relcionamento bidirecional e infomando qual o outro lado
	//Adicionando o cascade, para dizer que tudo que eu fizer desse lado, é para ser feito no outro
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL) 
	private List<ItemPedido> itens = new ArrayList<>();
	
	public Pedido() {}

	public Pedido(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public void adicionarItem(ItemPedido item){
		item.setPedido(this);
		this.itens.add(item);
		//Toda vez que for adicionar um novo produto, o valor total recebe ele mesmo mais o valor do produto
		this.valorTotal = this.valorTotal.add(item.getValor());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}
	
}