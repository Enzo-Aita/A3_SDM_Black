package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovimentaDao extends ConexaoDao {

   
    public boolean registrarMovimentacao(int idProduto, int quantidade, String tipo, String observacao) {
        String sql = "INSERT INTO tb_movimentacao(id_produto, quantidade, tipo, data_hora, observacao) VALUES(?,?,?,?,?)";
        
        try (Connection conn = this.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idProduto);
            stmt.setInt(2, quantidade);
            stmt.setString(3, tipo.toUpperCase());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(5, observacao != null ? observacao : "");
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException erro) {
            System.err.println("Erro ao registrar movimentação: " + erro.getMessage());
            return false;
        }
    }
    
    public List<Map<String, Object>> getHistoricoPorProduto(int idProduto) {
        List<Map<String, Object>> historico = new ArrayList<>();
    String sql = "SELECT * FROM tb_movimentacao WHERE id_produto = ? ORDER BY data_hora DESC";
    
    try (Connection conn = this.getConexao();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, idProduto);
        ResultSet res = stmt.executeQuery();
        
        while (res.next()) {
            Map<String, Object> registro = new HashMap<>();
            registro.put("data_hora", res.getTimestamp("data_hora").toLocalDateTime());
            registro.put("tipo", res.getString("tipo"));
            registro.put("quantidade", res.getInt("quantidade"));
            registro.put("observacao", res.getString("observacao"));
            
            historico.add(registro);
        }
        
    } catch (SQLException ex) {
        System.err.println("Erro ao consultar histórico: " + ex.getMessage());
        ex.printStackTrace();
    }
    
    return historico;
}
    
   
    public List<Map<String, Object>> getTodasMovimentacoes() {
        List<Map<String, Object>> movimentacoes = new ArrayList<>();
        String sql = "SELECT m.*, p.produto as nome_produto FROM tb_movimentacao m "
                   + "JOIN tb_produtodao p ON m.id_produto = p.id "
                   + "ORDER BY m.data_hora DESC";
        
        try (Connection conn = this.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            
            while (res.next()) {
                Map<String, Object> registro = new HashMap<>();
                registro.put("id", res.getInt("id"));
                registro.put("id_produto", res.getInt("id_produto"));
                registro.put("nome_produto", res.getString("nome_produto"));
                registro.put("quantidade", res.getInt("quantidade"));
                registro.put("tipo", res.getString("tipo"));
                registro.put("data_hora", res.getTimestamp("data_hora").toLocalDateTime());
                registro.put("observacao", res.getString("observacao"));
                
                movimentacoes.add(registro);
            }
            
        } catch (SQLException ex) {
            System.err.println("Erro ao consultar movimentações: " + ex.getMessage());
        }
        
        return movimentacoes;
    }
    
    
    public int maiorID() {
        int maiorID = 0;
        String sql = "SELECT MAX(id) as max_id FROM tb_movimentacao";
        
        try (Connection conn = this.getConexao();
             Statement stmt = conn.createStatement();
             ResultSet res = stmt.executeQuery(sql)) {
            
            if (res.next()) {
                maiorID = res.getInt("max_id");
            }
            
        } catch (SQLException ex) {
            System.err.println("Erro ao obter maior ID: " + ex.getMessage());
        }
        
        return maiorID;
    }
}
