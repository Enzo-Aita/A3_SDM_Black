package dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Produto;

/**
 * Data Access Object para operações com produtos no banco de dados
 *
 * @author SeuNome
 * @version 1.0
 */
public class ProdutoDAO extends ConexaoDAO {

    /**
     * Obtém todos os produtos do banco de dados
     *
     * @return Lista de produtos
     */
    public List<Produto> getMinhaLista() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM db_produtos.tb_produtodao";

        try (Connection conn = this.getConexao(); Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {

            while (res.next()) {
                Produto objeto = new Produto(
                        res.getInt("id"),
                        res.getString("produto"),
                        res.getDouble("preco"),
                        res.getString("unidade"),
                        res.getString("categoria"),
                        res.getInt("quantidade"),
                        res.getInt("estoqueminimo"),
                        res.getInt("estoquemaximo")
                );
                produtos.add(objeto);
            }

        } catch (SQLException ex) {
            System.err.println("Erro ao listar produtos: " + ex.getMessage());
            throw new RuntimeException("Erro ao buscar produtos", ex);
        }

        return produtos;
    }

    /**
     * Obtém o maior ID da tabela de produtos
     *
     * @return Maior ID encontrado
     */
    public int maiorID() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConexao();

            String sql = "SELECT MAX(id) as maior_id FROM db_produtos.tb_produtodao";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int maior = rs.getInt("maior_id");
                return Math.max(maior, 1);
            } else {
                return 1;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar maior ID: " + e.getMessage());
            return 1;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    /**
     * Insere um novo produto no banco de dados com valores fixos para estoque
     *
     * @param produto Produto a ser inserido
     * @return true se inserido com sucesso, false caso contrário
     */
    public boolean insertProdutoBD(Produto produto) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = getConexao();
            if (conn == null) {
                System.err.println("ERRO: Conexão NULL");
                return false;
            }

           
            String sql = "INSERT INTO tb_produtodao (produto, preco, unidade, categoria, quantidade, estoqueminimo, estoquemaximo) VALUES (?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, produto.getProduto());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setString(3, produto.getUnidade());
            pstmt.setString(4, produto.getCategoria());
            pstmt.setInt(5, produto.getQuantidade());
            pstmt.setInt(6, 25); 
            pstmt.setInt(7, 100); 

            int rows = pstmt.executeUpdate();

            
            if (rows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int novoId = generatedKeys.getInt(1);
                    produto.setId(novoId); 
                    System.out.println("Novo ID gerado para produto: " + novoId);
                }
                generatedKeys.close();
            }

            return rows > 0;

        } catch (SQLException e) {
            System.err.println("ERRO SQL: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    /**
     * Exclui um produto do banco de dados
     *
     * @param id ID do produto a ser excluído
     * @return true se excluído com sucesso, false caso contrário
     */
    public boolean deleteProdutoBD(int id) {
        String sql = "DELETE FROM db_produtos.tb_produtodao WHERE id=?";

        try (Connection conn = this.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            System.err.println("Erro ao deletar produto: " + ex.getMessage());
            throw new RuntimeException("Erro ao deletar produto", ex);
        }
    }

    /**
     * Atualiza um produto no banco de dados com valores fixos para estoque
     * mínimo e máximo
     *
     * @param produto Produto com dados atualizados
     * @return true se atualizado com sucesso, false caso contrário
     */
    public boolean updateProdutoBD(Produto produto) {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            int estoqueMinimoFixo = 25;
            int estoqueMaximoFixo = 100;

            conn = this.getConexao();
            String sql = "UPDATE tb_produtodao SET produto = ?, preco = ?, unidade = ?, "
                    + "categoria = ?, quantidade = ?, estoqueminimo = ?, estoquemaximo = ? WHERE id = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, produto.getProduto());
            stmt.setDouble(2, produto.getPreco());
            stmt.setString(3, produto.getUnidade());
            stmt.setString(4, produto.getCategoria());
            stmt.setInt(5, produto.getQuantidade());
            stmt.setInt(6, estoqueMinimoFixo);
            stmt.setInt(7, estoqueMaximoFixo);
            stmt.setInt(8, produto.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }

    /**
     * Busca um produto pelo ID
     *
     * @param id ID do produto
     * @return Produto encontrado ou null
     */
    public Produto getProdutoById(int id) {
        String sql = "SELECT * FROM tb_produtodao WHERE id = ?";

        try (Connection conn = this.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    return new Produto(
                            res.getInt("id"),
                            res.getString("produto"),
                            res.getDouble("preco"),
                            res.getString("unidade"),
                            res.getString("categoria"),
                            res.getInt("quantidade"),
                            res.getInt("estoqueminimo"),
                            res.getInt("estoquemaximo")
                    );
                }
            }

        } catch (SQLException ex) {
            System.err.println("Erro ao buscar produto por ID: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Atualiza apenas o preço de um produto
     *
     * @param id ID do produto
     * @param novoPreco Novo preço
     * @return true se atualizado com sucesso, false caso contrário
     */
    public boolean updatePrecoBD(int id, double novoPreco) {
        String sql = "UPDATE tb_produtodao SET preco = ? WHERE id = ?";

        try (Connection conn = this.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, novoPreco);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            System.err.println("Erro ao atualizar preço: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Atualiza apenas a quantidade de um produto
     *
     * @param id ID do produto
     * @param novaQuantidade Nova quantidade
     * @return true se atualizado com sucesso, false caso contrário
     */
    public boolean updateQuantidadeBD(int id, int novaQuantidade) {
        String sql = "UPDATE tb_produtodao SET quantidade = ? WHERE id = ?";

        try (Connection conn = this.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, novaQuantidade);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            System.err.println("Erro ao atualizar quantidade: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Carrega um produto pelo ID
     *
     * @param id ID do produto
     * @return Produto carregado
     */
    public Produto carregaProduto(int id) {
        String sql = "SELECT * FROM tb_produtodao WHERE id = ?";
        Produto objeto = new Produto();
        objeto.setId(id);

        try (Connection conn = this.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    objeto.setProduto(res.getString("produto"));
                    objeto.setPreco(res.getDouble("preco"));
                    objeto.setUnidade(res.getString("unidade"));
                    objeto.setCategoria(res.getString("categoria"));
                    objeto.setQuantidade(res.getInt("quantidade"));
                    objeto.setEstoqueminimo(res.getInt("estoqueminimo"));
                    objeto.setEstoquemaximo(res.getInt("estoquemaximo"));
                }
            }

        } catch (SQLException ex) {
            System.err.println("Erro ao carregar produto: " + ex.getMessage());
            throw new RuntimeException("Erro ao carregar produto", ex);
        }
        return objeto;
    }

    /**
     * Busca um produto pelo nome
     *
     * @param nomeProduto Nome do produto
     * @return Produto encontrado ou null
     */
    public Produto buscarProdutoPorNome(String nomeProduto) {
        String sql = "SELECT * FROM tb_produtodao WHERE produto = ?";

        try (Connection conn = this.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeProduto);

            try (ResultSet res = stmt.executeQuery()) {
                if (res.next()) {
                    return new Produto(
                            res.getInt("id"),
                            res.getString("produto"),
                            res.getDouble("preco"),
                            res.getString("unidade"),
                            res.getString("categoria"),
                            res.getInt("quantidade"),
                            res.getInt("estoqueminimo"),
                            res.getInt("estoquemaximo")
                    );
                }
            }

        } catch (SQLException ex) {
            System.err.println("Erro ao buscar produto por nome: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Busca produtos por categoria
     *
     * @param categoria Categoria dos produtos
     * @return Lista de produtos da categoria
     */
    public List<Produto> buscarProdutosPorCategoria(String categoria) {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM tb_produtodao WHERE categoria = ?";

        try (Connection conn = this.getConexao(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, categoria);

            try (ResultSet res = stmt.executeQuery()) {
                while (res.next()) {
                    Produto objeto = new Produto(
                            res.getInt("id"),
                            res.getString("produto"),
                            res.getDouble("preco"),
                            res.getString("unidade"),
                            res.getString("categoria"),
                            res.getInt("quantidade"),
                            res.getInt("estoqueminimo"),
                            res.getInt("estoquemaximo")
                    );
                    produtos.add(objeto);
                }
            }

        } catch (SQLException ex) {
            System.err.println("Erro ao buscar produtos por categoria: " + ex.getMessage());
        }
        return produtos;
    }

    /**
     * Busca produtos abaixo do estoque mínimo
     *
     * @return Lista de produtos abaixo do mínimo
     */
    public List<Produto> getProdutosAbaixoMinimo() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM tb_produtodao WHERE quantidade < estoqueminimo";

        try (Connection conn = this.getConexao(); Statement stmt = conn.createStatement(); ResultSet res = stmt.executeQuery(sql)) {

            while (res.next()) {
                Produto objeto = new Produto(
                        res.getInt("id"),
                        res.getString("produto"),
                        res.getDouble("preco"),
                        res.getString("unidade"),
                        res.getString("categoria"),
                        res.getInt("quantidade"),
                        res.getInt("estoqueminimo"),
                        res.getInt("estoquemaximo")
                );
                produtos.add(objeto);
            }

        } catch (SQLException ex) {
            System.err.println("Erro ao buscar produtos abaixo do mínimo: " + ex.getMessage());
        }
        return produtos;
    }

    /**
     * Corrige os valores de estoque mínimo e máximo para todos os produtos
     * Define estoque mínimo = 25 e estoque máximo = 100 para todos os produtos
     *
     * @return Número de produtos corrigidos
     */
    public int corrigirEstoqueMinMax() {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = this.getConexao();

            String sql = "UPDATE tb_produtodao SET estoqueminimo = 25, estoquemaximo = 100";

            stmt = conn.prepareStatement(sql);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected;

        } catch (SQLException e) {
            System.err.println("Erro ao corrigir estoque min/max: " + e.getMessage());
            return 0;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }
}
