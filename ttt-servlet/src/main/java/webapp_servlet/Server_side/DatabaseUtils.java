package vgu.pe2026.ttt.webapp_servlet.Server_side;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DatabaseUtils {
    private static final String URL = "jdbc:postgresql://localhost:5432/[ProgEx]-ttt_network_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";    

    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean isTokenUsed(String token){
        String sql = "SELECT 1 FROM used_tokens WHERE token = ?";

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setString(1, token);
                try (ResultSet rs = pstmt.executeQuery()){
                    return rs.next(); // returns true if the token exists in the DB
                } 
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
    }

    public static void saveUsedToken(String token, long deadline){
        String sql = "INSERT INTO used_tokens (token, deadline) VALUES (?, ?)";

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
            {
                pstmt.setString(1, token);
                pstmt.setLong(2, deadline);
                pstmt.executeUpdate();

            } catch (Exception e){
                e.printStackTrace();
            }
    }

    public static void cleanExpiredTokens(){
        String sql = "DELETE FROM used_tokens WHERE deadline < ?";

        long currentTime = System.currentTimeMillis();

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setLong(1, currentTime);
            pstmt.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
