package javaclass;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


import java.sql.Connection;
import java.util.List;

import java.util.List;

public class URL {
    private int idURL;
    private int type;
    private String countryName;
    private int idWebsite;

    public URL(int idURL, int type, String countryName, int idWebsite) {
        this.idURL = idURL;
        this.type = type;
        this.countryName = countryName;
        this.idWebsite = idWebsite;
    }

    public int getId_URL() {
        return idURL;
    }

    public void setId_URL(int idURL) {
        this.idURL = idURL;
    }

    public int getType_URL() {
        return type;
    }

    public void setType_URL(int type) {
        this.type = type;
    }

    public String getCountryName_URL() {
        return countryName;
    }

    public void setCountryName_URL(String countryName) {
        this.countryName = countryName;
    }

    public int getIdWebsite_URL() {
        return idWebsite;
    }

    public void setIdWebsite_URL(int idWebsite) {
        this.idWebsite = idWebsite;
    }

    public static List<URL> loadURL() {
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

            // Parcours du ResultSet et instanciation d'un objet URL pour chaque enregistrement
            while (rs.next()) {
                URL url = new URL(rs.getInt("Id_URL"), rs.getInt("type"), rs.getString("country_name"), rs.getInt("Id_Website"));
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
}
