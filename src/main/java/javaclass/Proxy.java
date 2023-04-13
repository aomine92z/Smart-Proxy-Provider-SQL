package javaclass;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class Proxy {
    private int id_Proxy;
    private int type_Proxy;
    private List<String> countryName_Proxy;

    public Proxy(int id_Proxy, int type_Proxy, List<String> countryName_Proxy) {
        this.id_Proxy = id_Proxy;
        this.type_Proxy = type_Proxy;
        this.countryName_Proxy = countryName_Proxy;
    }

    public int getId_Proxy() {
        return this.id_Proxy;
    }

    public void setId_Proxy(int id_Proxy) {
        this.id_Proxy = id_Proxy;
    }

    public int getType_Proxy() {
        return this.type_Proxy;
    }

    public void setType_Proxy(int type_Proxy) {
        this.type_Proxy = type_Proxy;
    }

    public List<String> getCountryName_Proxy() {
        return this.countryName_Proxy;
    }

    public void setCountryName_Proxy(int idProxy) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet ls = null;

        try {
            // Connexion à la base de données
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            // Création d'un objet Statement
            stmt = conn.createStatement();

            // Requête SQL pour récupérer les country_name correspondant à l'ID de proxy
            String query = "SELECT country_name FROM covers WHERE Id_Proxy = " + idProxy;
            ResultSet rsCovers = stmt.executeQuery(query);

            List<String> countryNames = new ArrayList<>();
            while (rsCovers.next()) {
                String countryName = rsCovers.getString("country_name");
                countryNames.add(countryName);
            }
            this.countryName_Proxy = countryNames;
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

    public String toString(){
        String countryNamesStr = String.join(", ", this.countryName_Proxy);
        return "Id_Proxy: " + this.id_Proxy + " || Type: " + this.type_Proxy + " || Country_name: " + countryNamesStr;
    }

    public static List<Proxy> load_Proxies() {
        List<Proxy> proxies = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet ls = null;

        try {
            // Connexion à la base de données
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            // Création d'un objet Statement
            stmt = conn.createStatement();

            // Exécution de la requête SQL pour récupérer les données de la table URL
            ls = stmt.executeQuery("SELECT * FROM Proxy");

            // Parcours du ResultSet et instanciation d'un objet URL pour chaque enregistrement
            while (ls.next()) {
                Proxy proxy = new Proxy(ls.getInt("Id_Proxy"), ls.getInt("type"), null);
                // Ajoute les country_name à l'objet Proxy
                // proxy.setCountryName_Proxy(countryNames);
                proxy.setCountryName_Proxy(ls.getInt("Id_Proxy"));
                proxies.add(proxy);
            }
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
        // Retourne la liste des URL chargées depuis la base de données
        return proxies;
    }
}



