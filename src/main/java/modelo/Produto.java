// Documentado para javadoc
// Fiz pelo onlinegdb, então pode dar algum bug na documentação!!

package modelo;

import dao.ProdutoDao;
import java.util.ArrayList;

/**
 * Representa um (1) Produto
 * <p>
 * Cada produto possui preço, unidade, categoria, quantidade e limite de
 * estoque.
 * Encapsula operações pelo {@link ProdutoDao}
 * </p>
 */
public class Produto {
    
    /** Código de identificação do produto */
    private int id;
    
    /** Preço do produto */
    private double preco;
    
    /** Unidade de medida(?) */
    private String unidade;
    
    /** Categoria do produto */
    private String categoria;
    
    /** Nome do produto */
    private String produto;
    
    /** Quantidade do produto em estoque */
    private int quantidade;
    
    /** Quantidade máxima do produto em estoque */
    private int quantidademax;
    
    /** Quantidade mínima do produto em estoque */
    private int quantidademin;
    
    /** Objeto para acesso do BD */
    private ProdutoDao dao;
    

    // Construtores
    
    /** Construtor padrão de inicialização */
    public Produto() {
        this(0, "", 0, "", "", 0, 0, 0);
    }

    /**
     * Construtor completo
     * 
     * @param id ID do produto
     * @param produto nome do produto
     * @param preco preço do produto
     * @param unidade unidade de medida(?)
     * @param categoria categoria do produto
     * @param quantidade quantidade em estoque
     * @param quantidademax quantidade máxima em estoque
     * @param quantidademin quantidade mínima em estoque
     * 
     */
    public Produto(int id, String produto, double preco, String unidade,
            String categoria, int quantidade, int quantidademax, int quantidademin) {
        this.id = id;
        this.preco = preco;
        this.unidade = unidade;
        this.categoria = categoria;
        this.produto = produto;
        this.quantidade = quantidade;
        this.quantidademax = quantidademax;
        this.quantidademin = quantidademin;
        this.dao = new ProdutoDao();
    }

    
    // Getters e Setters
    
    /** @return Retorna o ID do produto */
    public int getId() {
        return id;
    }
    
    /** @param id seta um ID novo para o produto */
    public void setId(int id) {
        this.id = id;
    }

    /** @return Retorna o preço do produto */
    public double getPreco() {
        return preco;
    }

    /** Seta o preço do produto
     *
     * @param preco Novo preço do produto
     * @throws IllegalArgumentException Se o preço for negativo
    */
    public void setPreco(double preco) {
        if (preco < 0) throw new IllegalArgumentException("Erro, o preço não pode ser negativo!");
        this.preco = preco;
    }

    /** @return Retorna a unidade do produto */
    public String getUnidade() {
        return unidade;
    }

    /** @param unidade Seta a nova unidade do produto */
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    /** @return Retorna a categoria do produto */
    public String getCategoria() {
        return categoria;
    }

    /** @param categoria Seta a nova categoria do produto */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /** @return Retorna o nome do produto */
    public String getProduto() {
        return produto;
    }

    /** @param produto seta o novo nome do produto */
    public void setProduto(String produto) {
        this.produto = produto;
    }

    /** @return Retorna a quantidade em estoque do produto */
    public int getQuantidade() {
        return quantidade;
    }

    /** @param quantidade Seta uma nova quantidade em estoque para o produto */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    /** @return Retorna a quantidade máxima permitida do produto */
    public int getQuantidademax() {
        return quantidademax;
    }

    /** @param quantidademax Seta uma nova quantidade máxima permitida */
    public void setQuantidademax(int quantidademax) {
        this.quantidademax = quantidademax;
    }

    /** @return Retorna a quantidade mínima permitida do produto */
    public int getQuantidademin() {
        return quantidademin;
    }

    /** @param quantidademax Seta uma nova quantidade mínima permitida */
    public void setQuantidademin(int quantidademin) {
        this.quantidademin = quantidademin;
    }

    /** @return Retorna o objeto de acesso ao BD */
    public ProdutoDao getDao() {
        return dao;
    }

    /** @param dao Novo dao para acesso ao BD */
    public void setDao(ProdutoDao dao) {
        this.dao = dao;
    }
    
    
    /**
     * Obtém a lista de produtos no sistema
     * @return Retorna a lista de produtos
     */
    public ArrayList<Produto> getMinhaLista() {
        return dao.getMinhaLista();
    }

    /**
     * Insere um novo produto no BD
     *
     * @param produto Nome do produto
     * @param preco Preço do produto
     * @param unidade Unidade de medida
     * @param categoria Categoria
     * @param quantidade Quantidade em estoque
     * @param quantidademax Limite máximo em estoque
     * @param quantidademin Limite mínimo em estoque
     * @return Retorna true se o processo funcionar
     */
    public boolean insertProdutoBD(String produto, double preco, String unidade, String categoria, int quantidade, int quantidademax, int quantidademin) {
        int id = this.maiorID() + 1;
        Produto objeto = new Produto(id, produto, preco, unidade, categoria, quantidade, quantidademax, quantidademin);
        dao.insertProdutoBD(objeto);
        return true;
    }


    /**
     * Remove um produto do BD
     *
     * @param id ID do produto a ser removido
     * @return Retorna true se a exclusão ocorrer com sucesso
     */
    public boolean deleteProdutoBD(int id) {

        dao.deleteProdutoBD(id);
        return true;
    }

    /**
     * Atualiza o preço de um produto no BD
     *
     * @param id ID do produto
     * @param novoPreco Novo valor do preço
     * @return Retorna true se a atualização for bem-sucedida
     */
    public boolean updatePrecoBD(int id, int novoPreco) {
        return dao.updatePrecoBD(id, novoPreco);
    }

    /**
     * Atualiza todas as informações de um produto
     *
     * @param id ID do produto
     * @param produto Nome do produto
     * @param preco Preço do produto
     * @param unidade Unidade
     * @param categoria Categoria do produto
     * @param quantidade Estoque atual
     * @param quantidademax Limite máximo em estoque
     * @param quantidademin Limite mínimo em estoque
     * @return Retorna true Se for atualizado com sucesso
     */
    public boolean updateProdutoBD(int id, String produto,
            double preco, String unidade, String categoria,
            int quantidade, int quantidademax, int quantidademin) {

        Produto objeto = new Produto(id, produto, preco, unidade,
                categoria, quantidade, quantidademax, quantidademin);

       
        return dao.updateProdutoBD(objeto);
    }

    /**
     * Atualiza a quantidade em estoque
     *
     * @param id ID do produto
     * @param novaQuantidade Nova quantidade em estoque
     * @return Retorna true se for atualizado com sucesso
     */
    public boolean updateQuantidadeBD(int id, int novaQuantidade) {
        return dao.updateQuantidadeBD(id, novaQuantidade);
    }

    /**
     * Carrega um produto com base no ID
     *
     * @param id ID do produto
     * @return Retorna o obbjeto {@code Produto} correspondente
     */
    public Produto carregaProduto(int id) {
        return dao.carregaProduto(id);
    }

    /**
     * Retorna o maior ID no BD
     *
     * @return Retorna o maior ID encontrado
     */
    public int maiorID() {
        return dao.maiorID();
    }

    
    
    /**
     * Altera o preço de todos os produtos com base no percentual informado
     *
     * @param percentual Percentual de reajuste dos preços (postv. ou negtv.)
     * @return Retornatrue caso o reajuste ocorra sem erros
     */
    public boolean reajustarPrecos(double percentual) {
        try {
            ArrayList<Produto> produtos = getMinhaLista();

            for (Produto produto : produtos) {
                double precoAtual = produto.getPreco();
                int novoPreco = (int) Math.round(precoAtual * (1 + percentual / 100.0));
                produto.setPreco(novoPreco);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }
}


// Era uma boa transferir parte da lógica de acesso pro produtodao