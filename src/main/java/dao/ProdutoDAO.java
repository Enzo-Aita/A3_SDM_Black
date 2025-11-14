package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelo.Produto;

/**
 * Realiza operações de CRUD para os objetos {@link Produto} no BD
 * Estende {@link ConexaoDAO} para conectar
 */
public class ProdutoDAO extends ConexaoDAO {

    /** Lista para armazenar os produtos no BD */
    public static ArrayList<Produto> minhaLista = new ArrayList<>();

    /**
     * Retorna uma lista com todos os produtos no BD
     * @return Retorna lista de produtos no BD
     */
    public ArrayList<Produto> getMinhaLista() {

        minhaLista.clear();

        try {
            Statement stmt = this.getConexao().createStatement();

            ResultSet res = stmt.executeQuery("SELECT * FROM tb_produtodao");
            while (res.next()) {

                int id = res.getInt("id");
                String produto = res.getString("produto");
                double preco = res.getDouble("preco");
                String unidade = res.getString("unidade");
                String categoria = res.getString("categoria");
                int quantidade = res.getInt("quantidade");
                int quantidademax = res.getInt("quantidademax");
                int quantidademin = res.getInt("quantidademin");
                
                

                Produto objeto = new Produto(id, produto, preco, unidade, categoria, quantidade, quantidademax, quantidademin);
                minhaLista.add(objeto);
            }
            stmt.close();

        } catch (SQLException ex) {
            System.out.println("Erro:" + ex);

        }

        return minhaLista;
    }
    
    /**
     * Atuualiza a lista dos produtos
     * @param minhaLista Lista atualizada
     */
    public static void setMinhaLista(ArrayList<Produto> minhaLista) {
        ProdutoDAO.minhaLista = minhaLista;
    }

    /**
     * Retorna o maior ID na lista de produtos
     * @return Retorna o ID de maior valor (ou 0 caso haja falha)
     */
    public int maiorID() {
        int maiorID = 0;

        try {
            Statement stmt = this.getConexao().createStatement();
            ResultSet res = stmt.executeQuery("SELECT MAX(id) id FROM tb_produtodao");
            res.next();
            maiorID = res.getInt("id");
            stmt.close();
        } catch (SQLException ex) {
            System.out.println("Erro:" + ex);
        }
        return maiorID;
    }


    /**
     * Inserir um novo produto no BD
     * @param objeto Objeto a ser inserido {@link Produto}
     * @return Retorna true caso o processo funcione
     */
    public boolean insertProdutoBD(Produto objeto) {

        String sql = "INSERT INTO tb_produtodao(id,produto,preco,unidade,categoria,quantidade,quantidademax,quantidademin) VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = this.getConexao().prepareStatement(sql);

            stmt.setInt(1, objeto.getId());
            stmt.setString(2, objeto.getProduto());
            stmt.setDouble(3, (double) objeto.getPreco());
            stmt.setString(4, objeto.getUnidade());
            stmt.setString(5, objeto.getCategoria());
            stmt.setInt(6, objeto.getQuantidade());
            stmt.setInt(7, objeto.getQuantidademax());
            stmt.setInt(8, objeto.getQuantidademin());

            stmt.execute();
            stmt.close();

            return true;
        } catch (SQLException erro) {
            System.out.println("Erro:" + erro);
            throw new RuntimeException(erro);
        }
    }

    /**
     * Deleta um produto do BD
     * @param id ID do produto a ser deletado
     * @return Retorna true se a operação for terminada
     */
    public boolean deleteProdutoBD(int id) {
        try {
            Statement stmt = this.getConexao().createStatement();

            stmt.executeUpdate("DELETE FROM tb_produtodao WHERE id =" + id);

            stmt.close();
        } catch (SQLException erro) {
            System.out.println("Erro:" + erro);
        }
        return true;
    }

    /**
     * Atualiza um produto no BD
     * @param objeto Produto com os novos dados
     * @return Retorna true caso algum dados seja modificado
     */
    public boolean updateProdutoBD(Produto objeto) {
        String sql = "UPDATE tb_produtodao SET produto = ?, preco = ?, unidade = ?, categoria = ?, "
                + "quantidade = ?, quantidademax = ?, quantidademin = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.getConexao().prepareStatement(sql);

            stmt.setString(1, objeto.getProduto());
            stmt.setDouble(2, (double) objeto.getPreco());
            stmt.setString(3, objeto.getUnidade());
            stmt.setString(4, objeto.getCategoria());
            stmt.setInt(5, objeto.getQuantidade());
            stmt.setInt(6, objeto.getQuantidademax());
            stmt.setInt(7, objeto.getQuantidademin());
            stmt.setInt(8, objeto.getId());

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            
            
            return rowsAffected > 0;
        } catch (SQLException erro) {
            System.out.println("Erro:" + erro);
            return false;
        }
    }

    /**
     * Atualiza o preço de um produto
     * @param id ID do produto 
     * @param novoPreco Novo preço do produto a ser atualizado
     * @return Retorna true caso o produto seja atualizado corretamente
     */
    public boolean updatePrecoBD(int id, int novoPreco) {
        String sql = "UPDATE tb_produtodao SET preco = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.getConexao().prepareStatement(sql);
            stmt.setInt(1, novoPreco);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            stmt.close();

            return rowsAffected > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar preço:" + erro);
            return false;
        }
    }

    /**
     * Atualiza a quantidade de itens de um produto em estoque
     * @param id ID do produto
     * @param novaQuantidade Nova quantidade do produto em estoque
     * @return Retorna true caso a operação funcione
     */
    public boolean updateQuantidadeBD(int id, int novaQuantidade) {
        String sql = "UPDATE tb_produtodao SET quantidade = ? WHERE id = ?";

        try (Connection conn = this.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, novaQuantidade);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException erro) {
            System.out.println("Erro ao atualizar quantidade:" + erro);
            return false;
        }
    }

    /**
     * Carrega os dados de um produto com base no ID
     * @param id ID do produto (para busca)
     * @return Retorna o objeto {@link Produto} com os dados do BD
     */
    public Produto carregaProduto(int id) {
        Produto objeto = new Produto();
        objeto.setId(id);
        try {
            Statement stmt = this.getConexao().createStatement();

            ResultSet res = stmt.executeQuery("SELECT * FROM tb_produtodao WHERE id =" + id);
            res.next();

            objeto.setProduto(res.getString("produto"));
            objeto.setPreco(res.getDouble("preco"));
            objeto.setUnidade(res.getString("unidade"));
            objeto.setCategoria(res.getString("categoria"));
            objeto.setQuantidade(res.getInt("quantidade"));
            objeto.setQuantidademax(res.getInt("quantidademax"));
            objeto.setQuantidademin(res.getInt("quantidademin"));

            stmt.close();
        } catch (SQLException erro) {
            System.out.println("Erro:" + erro);
        }
        return objeto;
    }

}

