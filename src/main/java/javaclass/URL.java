package javaclass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class URL {
    private int id_url;
    private int type;
    private String country_name;
    private int id_website;

    public URL(int id_url, int type, String country_name, int id_website) {
        this.id_url = id_url;
        this.type = type;
        this.country_name = country_name;
        this.id_website = id_website;
    }

    public static List<URL> load_URLs() {
        List<URL> urls = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Connexion à la base de données
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            // Création d'un objet Statement
            stmt = conn.createStatement();

            // Exécution de la requête SQL pour récupérer les données de la table URL
            rs = stmt.executeQuery("SELECT * FROM URL");

            // Parcours du ResultSet et instanciation d'un objet URL pour chaque
            // enregistrement
            while (rs.next()) {
                URL url = new URL(rs.getInt("Id_URL"), rs.getInt("type"), rs.getString("country_name"),
                        rs.getInt("Id_Website"));
                urls.add(url);
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

        // Retourne la liste des URL chargées depuis la base de données
        return urls;
    }

    public int getId_URL() {
        return this.id_url;
    }

    public int getType_URL() {
        return this.type;
    }

    public String getCountry_name_URL() {
        return this.country_name;
    }

    public int getId_website() {
        return this.id_website;
    }

    public String toString() {
        return ("Id_URL: " + this.id_url + ", Type: " + this.type + ", Country: " + this.country_name + ", Id_Website: "
                + this.id_website);
    }
}