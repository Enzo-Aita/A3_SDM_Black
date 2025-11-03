package modelo;

import dao.ProdutoDao;
import java.util.ArrayList;

public class Produto {
    
    private int id;
    private double preco;
    private String unidade;
    private String categoria;
    private String produto;
    private int quantidade;
    private int quantidademax;
    private int quantidademin;
    private ProdutoDao dao;
    

    public Produto() {
        this(0, "", 0, "", "", 0, 0, 0);
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidademax() {
        return quantidademax;
    }

    public void setQuantidademax(int quantidademax) {
        this.quantidademax = quantidademax;
    }

    public int getQuantidademin() {
        return quantidademin;
    }

    public void setQuantidademin(int quantidademin) {
        this.quantidademin = quantidademin;
    }

    public ProdutoDao getDao() {
        return dao;
    }

    public void setDao(ProdutoDao dao) {
        this.dao = dao;
    }
    
    

    public ArrayList<Produto> getMinhaLista() {
        return dao.getMinhaLista();
    }

    public boolean insertProdutoBD(String produto, double preco, String unidade, String categoria, int quantidade, int quantidademax, int quantidademin) {
        int id = this.maiorID() + 1;
        Produto objeto = new Produto(id, produto, preco, unidade, categoria, quantidade, quantidademax, quantidademin);
        dao.insertProdutoBD(objeto);
        return true;
    }

    public boolean deleteProdutoBD(int id) {

        dao.deleteProdutoBD(id);
        return true;
    }

    public boolean updatePrecoBD(int id, int novoPreco) {
        return dao.updatePrecoBD(id, novoPreco);
    }

    public boolean updateProdutoBD(int id, String produto,
            double preco, String unidade, String categoria,
            int quantidade, int quantidademax, int quantidademin) {

        Produto objeto = new Produto(id, produto, preco, unidade,
                categoria, quantidade, quantidademax, quantidademin);

       
        return dao.updateProdutoBD(objeto);
    }

    public boolean updateQuantidadeBD(int id, int novaQuantidade) {
        return dao.updateQuantidadeBD(id, novaQuantidade);
    }

    public Produto carregaProduto(int id) {
        return dao.carregaProduto(id);
    }

    public int maiorID() {
        return dao.maiorID();
    }

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


