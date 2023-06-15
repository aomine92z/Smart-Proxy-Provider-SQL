package javaclass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class WillRespond {
    private int Id_Website;
    private int Id_Proxy;
    private String success;
    private String timestamp;

    public WillRespond(int Id_Website, int Id_Proxy, String success, String timestamp) {
        this.Id_Website = Id_Website;
        this.Id_Proxy = Id_Proxy;
        this.success = success;
        this.timestamp = timestamp;
    }

    public int getId_Website(){
        return this.Id_Website;
    }

    public int getId_Proxy() {
        return this.Id_Proxy;
    }

    public String get_Success(){
        return this.success;
    }

    public String get_Timestamp(){
        return this.timestamp;
    }

    public static Map<String, WillRespond> load_willRespond() {
        Map<String, WillRespond> willrespondsMap = new HashMap<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Connexion à la base de données
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            // Création d'un objet Statement
            stmt = conn.createStatement();

            // Exécution de la requête SQL pour récupérer les données de la table URL
            rs = stmt.executeQuery("SELECT * FROM willRespond");

            // Parcours du ResultSet et instanciation d'un objet URL pour chaque
            // enregistrement
            while (rs.next()) {
                WillRespond willrespond = new WillRespond(rs.getInt("Id_Website"), rs.getInt("Id_Proxy"), rs.getString("success"), rs.getString("timestamp"));
                String key = willrespond.getId_Website() + "-" + willrespond.getId_Proxy() + "-" + willrespond.get_Timestamp();
                willrespondsMap.put(key, willrespond);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des objets ResultSet, Statement et Connection
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return willrespondsMap;
    }

    public String toString() {
        return ("Id_Website: " + this.Id_Website + ", Id_Proxy: " + this.Id_Proxy + ", Success: " + this.success + ", Timestamp: " + this.timestamp);
    }

    // public void saveToDatabase() {
    //     Connection conn = null;
    //     PreparedStatement pstmt = null;
    
    //     try {
    //         conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            
    //         String sql = "INSERT INTO willRespond (Id_Website, Id_Proxy, success, timestamp) VALUES (?, ?, ?, ?)";
    //         pstmt = conn.prepareStatement(sql);
            
    //         pstmt.setInt(1, this.Id_Website);
    //         pstmt.setInt(2, this.Id_Proxy);
    //         pstmt.setString(3, this.success);
    //         pstmt.setString(4, this.timestamp);
    
    //         pstmt.executeUpdate();
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     } finally {
    //         try {
    //             if (pstmt != null) {
    //                 pstmt.close();
    //             }
    //             if (conn != null) {
    //                 conn.close();
    //             }
    //         } catch (SQLException e) {
    //             e.printStackTrace();
    //         }
    //     }
    // }
    
}
