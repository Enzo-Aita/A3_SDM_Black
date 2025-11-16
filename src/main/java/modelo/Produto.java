package modelo;

import java.io.Serializable;

/**
 * Representa um produto do estoque
 * <p>
 * Realizar operações pelo ProdutoDao
 * </p>
 */
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Código de identificação do produto
     */
    private int id;

    /**
     * Preço do produto
     */
    private double preco;

    // Esse aqui eu não entendi muito bem como comentar
    private String unidade;

    /**
     * Categoria do produto
     */
    private String categoria;

    /**
     * Nome do produto
     */
    private String produto;

    /**
     * Quantidade em estoque do produto
     */
    private int quantidade;

    /**
     * Estoque mínimo do produto no estoque
     */
    private int estoqueminimo;

    /**
     * Estoque máximo do produto no estoque
     */
    private int estoquemaximo;

    // Construtores
    /**
     * Construtor para inicializar o produto
     */
    public Produto() {
        this(0, "", 0, "", "", 0, 0, 0);
    }

    /**
     * Construtor completo
     *
     * @param id ID do produto
     * @param produto Nome do produto
     * @param preco Preço do produto
     * @param unidade
     * @param categoria Categoria do produto
     * @param quantidade Quantidade em estoque do produto
     * @param estoqueminimo Quantidade máxima em estoque do produto
     * @param estoquemaximo Quantidade mínima em estoque do produto
     */
    public Produto(int id, String produto, double preco, String unidade,
            String categoria, int quantidade, int estoqueminimo, int estoquemaximo) {
        this.id = id;
        this.preco = preco;
        this.unidade = unidade;
        this.categoria = categoria;
        this.produto = produto;
        this.quantidade = quantidade;
        this.estoqueminimo = estoqueminimo;
        this.estoquemaximo = estoquemaximo;

    }

    // Getters e Setters
    /**
     * @return Retorna o ID do produto
     */
    public int getId() {
        return id;
    }

    /**
     * @param id Seta o ID do produto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Retorna o preço do produto
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Seta o preço do produto
     *
     * @param preco Novo preço
     * @throws RuntimeException Caso o preço seja negativo
     */
    public void setPreco(double preco) {
        if (preco < 0) {
            throw new RuntimeException("Preço não pode ser negativo");
        }
        this.preco = preco;
    }

    // Esse aqui eu não entendi muito bem como comentar
    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    /**
     * @return Retorna a categoria do produto
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * @param categoria Seta a categoria do produto
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * @return Retorna o nome do produto
     */
    public String getProduto() {
        return produto;
    }

    /**
     * @param produto Seta o nome do produto
     */
    public void setProduto(String produto) {
        this.produto = produto;
    }

    /**
     * @return Retorna a quantidade em estoque de um produto
     */
    public int getQuantidade() {
        return quantidade;
    }

    /**
     * Define a quantidade em estoque de um produto
     *
     * @param quantidade Nova quantidade
     * @throws RuntimeException Caso a quantidade seja negativa
     */
    public void setQuantidade(int quantidade) {
        if (quantidade < 0) {
            throw new RuntimeException("Quantidade não pode ser negativa");
        }
        this.quantidade = quantidade;
    }

    /**
     * @return Retorna a Estoque mínimo
     */
    public int getEstoqueminimo() {
        return estoqueminimo;
    }

    /**
     * @param estoqueMinimo Seta a estoque mínimo
     */
    public void setEstoqueminimo(int estoqueMinimo) {
        this.estoqueminimo = estoqueMinimo;
    }

    /**
     * @return Retorna a estoque máximo
     */
    public int getEstoquemaximo() {
        return estoquemaximo;
    }

    /**
     * @param estoqueMaximo Seta a quantidade mínima
     */
    public void setEstoquemaximo(int estoqueMaximo) {
        this.estoquemaximo = estoqueMaximo;
    }

}

