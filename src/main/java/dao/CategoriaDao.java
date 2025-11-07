package dao;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelo.Categoria;

// Operações de persistência CRUD
public class CategoriaDao extends ConexaoDao {

    public static ArrayList<Categoria> minhaLista = new ArrayList<>();

    // Serve para retornar todas as categorias no bd
    public ArrayList<Categoria> getMinhaLista() {
        minhaLista.clear();

        try {
            Statement stmt = this.getConexao().createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM tb_categoriadao");

            while (res.next()) {
                int id = res.getInt("id");
                String nome = res.getString("nome");
                String embalagem = res.getString("embalagem");
                String tamanho = res.getString("tamanho");

                Categoria objeto = new Categoria(id, nome, embalagem, tamanho);
                minhaLista.add(objeto);
            }
            stmt.close();

        } catch (SQLException ex) {
            System.err.println("Erro ao listar categorias: " + ex.getMessage());
        }

        return minhaLista;
    }
    
public int maiorID() {
        int maiorID = 0;

        try {
            Statement stmt = this.getConexao().createStatement();
            ResultSet res = stmt.executeQuery("SELECT MAX(id) id FROM tb_categoriadao");
            res.next();
            maiorID = res.getInt("id");
            stmt.close();
        } catch (SQLException ex) {
            System.err.println("Erro ao buscar maior ID: " + ex.getMessage());
        }
        return maiorID;
    }

    public boolean insertCategoriaBD(Categoria objeto) {
        String sql = "INSERT INTO tb_categoriadao(id,nome,embalagem,tamanho) VALUES(?,?,?,?)";

        try {
            PreparedStatement stmt = this.getConexao().prepareStatement(sql);
            stmt.setInt(1, objeto.getId());
            stmt.setString(2, objeto.getNome());
            stmt.setString(3, objeto.getEmbalagem());
            stmt.setString(4, objeto.getTamanho());

            stmt.execute();
            stmt.close();
            return true;

        } catch (SQLException ex) {
            System.err.println("Erro ao inserir categoria: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
    
public boolean deleteCategoriaBD(int id) {
        try {
            Statement stmt = this.getConexao().createStatement();
            stmt.executeUpdate("DELETE FROM tb_categoriadao WHERE id =" + id);
            stmt.close();
            return true;
        } catch (SQLException ex) {
            System.err.println("Erro ao excluir categoria: " + ex.getMessage());
            return false;
        }
    }

    public boolean updateCategoriaBD(Categoria objeto) {
        String sql = "UPDATE tb_categoriadao SET nome = ?, embalagem = ?, tamanho = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.getConexao().prepareStatement(sql);
            stmt.setString(1, objeto.getNome());
            stmt.setString(2, objeto.getEmbalagem());
            stmt.setString(3, objeto.getTamanho());
            stmt.setInt(4, objeto.getId());

            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            System.err.println("Erro ao atualizar categoria: " + ex.getMessage());
            return false;
        }
    }
    
public Categoria carregaCategoria(int id) {
        Categoria objeto = new Categoria();
        objeto.setId(id);

        try {
            Statement stmt = this.getConexao().createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM tb_categoriadao WHERE id =" + id);

            if (res.next()) {
                objeto.setNome(res.getString("nome"));
                objeto.setEmbalagem(res.getString("embalagem"));
                objeto.setTamanho(res.getString("tamanho"));
            }
            stmt.close();

        } catch (SQLException ex) {
            System.err.println("Erro ao carregar categoria: " + ex.getMessage());
        }
        return objeto;
    }
}