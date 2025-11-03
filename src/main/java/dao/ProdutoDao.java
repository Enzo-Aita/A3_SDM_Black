package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelo.Produto;

public class ProdutoDao extends ConexaoDao {

    public static ArrayList<Produto> minhaLista = new ArrayList<>();

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

    public static void setMinhaLista(ArrayList<Produto> minhaLista) {
        ProdutoDao.minhaLista = minhaLista;
    }

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

