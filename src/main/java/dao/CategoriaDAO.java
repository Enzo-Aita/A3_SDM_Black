package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import modelo.Categoria;

/**
 * Operações CRUD da entidade {@link Categoria} Estende {@link ConexaoDAO} para
 * acesso ao BD
 *
 * Essa função gera métodos para inserir, atualizar, excluir e consultar
 * categorias dos produtos Tabela: <code>tb_categoriadao</code>
 */
public class CategoriaDAO extends ConexaoDAO {

    /**
     * Lista auxiliar de armazenamento para os objetos 'Categoria' no BD
     */
    public static ArrayList<Categoria> minhaLista = new ArrayList<>();

    /**
     * Retorna todas as categorias no BD
     *
     * @return Retorna uma lista com os objetos de {@link Categoria}
     */
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

    /**
     * Retorna o maior ID registrado em <code>tb_categoriadao</code>
     *
     * @return Retorna o maior ID encontrado (ou 1, em caso de tabela vazia)
     */
    public int maiorID() {
        int maiorID = 0;

        try {
            Statement stmt = this.getConexao().createStatement();
            ResultSet res = stmt.executeQuery("SELECT COALESCE(MAX(id), 0) as id FROM tb_categoriadao");
            res.next();
            maiorID = res.getInt("id");
            stmt.close();

            // Se não há registros, começa com 1
            return maiorID == 0 ? 1 : maiorID + 1;

        } catch (SQLException ex) {
            System.err.println("Erro ao buscar maior ID: " + ex.getMessage());
            return 1;
        }
    }

    /**
     * Insere um objeto no BD
     *
     * @param objeto Categoria do objeto a ser incluída
     * @return Retorna true caso a operação funcione
     * @throws RuntimeException caso ocorra um erro durante a operação
     */

    public boolean insertCategoriaBD(Categoria objeto) {

        String sql = "INSERT INTO tb_categoriadao (nome, embalagem, tamanho) VALUES (?, ?, ?)";

        try {
            PreparedStatement stmt = this.getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, objeto.getNome());
            stmt.setString(2, objeto.getEmbalagem());
            stmt.setString(3, objeto.getTamanho());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int novoId = generatedKeys.getInt(1);
                    objeto.setId(novoId); // Atualiza o objeto com o ID real
                    System.out.println("Novo ID gerado para categoria: " + novoId);
                }
                generatedKeys.close();
            }

            stmt.close();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            System.err.println("Erro ao inserir categoria: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    /**
     * Remove uma categoria do BD com base no seu ID.
     *
     * @param id ID da categoria que será excluída
     * @return true se a exclusão ocorrer com sucesso, ou false caso contrário.
     */
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

    /**
     * Atualiza os dados de uma categoria no BD
     *
     * @param objeto Uma instância de {@link Categoria} com dados atualizados
     * @return Retorna true caso alguma atualização ocorra de fato
     */
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

    /**
     * Carrega uma categoria com base no ID do produto
     *
     * @param id ID do produto
     * @return Retorna um objeto {@link Categoria} com os dados encontrados, ou
     * a categoria apenas com o ID, caso não ache registro
     */
    public Categoria listarCategoria(int id) {
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
