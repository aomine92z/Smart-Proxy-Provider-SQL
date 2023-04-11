package javaclass;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class URL {
    private int id_URL;
    private int type_URL;
    private String countryName_URL;
    private int idWebsite_URL;

    public URL(int id_URL, int type_URL, String countryName_URL, int idWebsite_URL) {
        this.id_URL = id_URL;
        this.type_URL = type_URL;
        this.countryName_URL = countryName_URL;
        this.idWebsite_URL = idWebsite_URL;
    }

    public int getId_URL() {
        return this.id_URL;
    }

    public void setId_URL(int id_URL) {
        this.id_URL = id_URL;
    }

    public int getType_URL() {
        return this.type_URL;
    }

    public void setType_URL(int type_URL) {
        this.type_URL = type_URL;
    }

    public String getCountryName_URL() {
        return this.countryName_URL;
    }

    public void setCountryName_URL(String countryName_URL) {
        this.countryName_URL = countryName_URL;
    }

    public int getIdWebsite_URL() {
        return this.idWebsite_URL;
    }

    public void setIdWebsite_URL(int idWebsite_URL) {
        this.idWebsite_URL = idWebsite_URL;
    }

    public String toString(){
        return "Id_URL: " + this.getId_URL() + " || Type: " + this.getType_URL() + " || Country_name: " + this.getCountryName_URL() + " || Id_Website: " + this.getIdWebsite_URL();
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