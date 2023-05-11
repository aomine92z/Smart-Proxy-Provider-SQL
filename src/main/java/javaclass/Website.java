package javaclass;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Website {
    private int id_website;
    private int type_website;
    private List<String> countryName_Website;

    public Website(int id_website, int type_website, List<String> countryName_Website) {
        this.id_website = id_website;
        this.type_website = type_website;
        this.countryName_Website = countryName_Website;
    }

    public void setCountryName_Website(int idWebsite) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet ls = null;

        try {
            // Connexion à la base de données
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            // Création d'un objet Statement
            stmt = conn.createStatement();

            // Requête SQL pour récupérer les country_name correspondant à l'ID de proxy
            String query = "SELECT country_name FROM website_covers WHERE Id_Website = " + idWebsite;
            ResultSet rsCovers = stmt.executeQuery(query);

            List<String> countryNames = new ArrayList<>();
            while (rsCovers.next()) {
                String countryName = rsCovers.getString("country_name");
                countryNames.add(countryName);
            }
            this.countryName_Website = countryNames;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des objets ResultSet, Statement et Connection
            try {
                if (ls != null) {
                    ls.close();
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
    }

    public static Map<Integer, Website> load_Websites() {
        Map<Integer, Website> websitesMap = new HashMap<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Connexion à la base de données
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            // Création d'un objet Statement
            stmt = conn.createStatement();

            // Exécution de la requête SQL pour récupérer les données de la table URL
            rs = stmt.executeQuery("SELECT * FROM Website");

            // Parcours du ResultSet et instanciation d'un objet URL pour chaque
            // enregistrement
            while (rs.next()) {
                Website website = new Website(rs.getInt("Id_Website"), rs.getInt("type"), null);
                website.setCountryName_Website(rs.getInt("Id_Website"));
                Integer key = website.id_website;
                websitesMap.put(key, website);
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
        return websitesMap;
    }

    public int getId_website() {
        return this.id_website;
    }

    public int getType_website() {
        return this.type_website;
    }

    public List<String> getCountry_name_website() {
        return this.countryName_Website;
    }

    public String toString() {
        String countryNamesStr = String.join(", ", this.countryName_Website);
        return "Id_Website: " + this.id_website + " || Type: " + this.type_website + " || Country_name: "
                + countryNamesStr;
    }
}
