package bd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoricoDAO {

    public void registrarAcao(double temperatura, String acaoTomada) {
        String sql = "INSERT INTO historico (temperatura, acao_tomada) VALUES (?, ?)";
        try (Connection conn = ConexaoDB.conectar()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, temperatura);
                stmt.setString(2, acaoTomada);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Erro ao registrar histórico: " + e.getMessage());
        }
    }

    public String buscarUltimaAcao() {
        String sql = "SELECT acao_tomada FROM historico ORDER BY id DESC LIMIT 1";
        try (Connection conn = ConexaoDB.conectar()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("acao_tomada");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar histórico: " + e.getMessage());
        }
        return "Nenhuma Acao";
    }
}