package javaclass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class WillRespond {
    private int Id_website;
    private int Id_Proxy;
    private int triedOk;
    private int triedTotal;

    public WillRespond(int Id_website, int Id_Proxy, int triedOk, int triedTotal) {
        this.Id_website = Id_website;
        this.Id_Proxy = Id_Proxy;
        this.triedOk = triedOk;
        this.triedTotal = triedTotal;
    }

    public int getId_website() {
        return this.Id_website;
    }

    public int getId_Proxy() {
        return this.Id_Proxy;
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
                WillRespond willrespond = new WillRespond(rs.getInt("Id_Website"), rs.getInt("Id_Proxy"),
                        rs.getInt("triedOk"), rs.getInt("triedTotal"));
                String key = willrespond.getId_website() + "-" + willrespond.getId_Proxy();
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
        return ("Id_URL: " + this.Id_website + ", Id_Proxy: " + this.Id_Proxy + ", TriedOk: " + this.triedOk
                + ", TriedTotal: " + this.triedTotal);
    }

    public int addNewTry() {
        this.triedTotal += 1;
        return this.triedTotal;
    }

    public int addNewSuccess() {
        this.triedOk += 1;
        return this.triedOk;
    }

}
