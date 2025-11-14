package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Gerencia a conexão com o BD MySQL
 * <p>
 * Utiliza o driver JDBC (Oracle) para criar conexão com o BD MySQL. Caso a
 * conexão seja bem-sucedida, o objeto {@link Connection} será retornado para
 * operações de persistência
 * </p>
 * 
 * <p><b>Exemplo de uso:</b></p>
 * <pre>{@code 
 * ConexaoDao conexaoDao = new ConexaoDao();
 * Connection conn = conexaoDao.getConexao();
 * if (conn != null) {
 *      System.out.println("Conexão estabelecida com sucesso!");
 * }
 * }</pre>
 */
public class ConexaoDao {
    
    /**
     * Obtém uma conexão com o banco de dados MySQL.
     * 
     * <p>
     * Carrega o JDBC, monta o URL de conexão e conecta com as credenciais de
     * usuário e senha definidas
     * </p>
     * 
     * @return Retorna um objeto {@link Connection} ativo, ou {@code null} caso
     * a conexão não seja bem-sucedida
     */
    public Connection getConexao() {
        Connection connection = null;
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            Class.forName(driver);

            String server = "localhost";
            String database = "db_produtos";
            String url = "jdbc:mysql://" + server + ":3306/" + database + "?useTimezone=true&serverTimezone=UTC";
            String user = "root";
            String password = "enzo020506";

            connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                System.out.println("Status: Conectado!");

            } else {
                System.out.println("Status: NÃO CONECTADO!");
            }
            return connection;
        } catch (ClassNotFoundException e) {
            System.out.println("O driver nao foi encontrado.");
            return null;
        } catch (SQLException e) {
            System.out.println("Não foi possivel conectar...");
            return null;
        }

    }
}
