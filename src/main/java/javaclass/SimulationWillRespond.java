package javaclass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SimulationWillRespond {
    private int Id_URL;
    private int Id_Proxy;
    private double probabilityRejection;

    public SimulationWillRespond(int Id_URL, int Id_Proxy, double probabilityRejection) {
        this.Id_URL = Id_URL;
        this.Id_Proxy = Id_Proxy;
        this.probabilityRejection = probabilityRejection;
    }

    public int getId_URL(){
        return this.Id_URL;
    }

    public int getId_Proxy(){
        return this.Id_Proxy;
    }

    public double getProbabilityRejection(){
        return this.probabilityRejection;
    }

    public static Map<String, SimulationWillRespond> load_simulationWillRespond() {
        Map<String, SimulationWillRespond> simulationWillrespondsMap = new HashMap<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Connexion à la base de données
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            // Création d'un objet Statement
            stmt = conn.createStatement();

            // Exécution de la requête SQL pour récupérer les données de la table URL
            rs = stmt.executeQuery("SELECT * FROM simulation_willRespond");

            // Parcours du ResultSet et instanciation d'un objet URL pour chaque
            // enregistrement
            while (rs.next()) {
                SimulationWillRespond simulationwillrespond = new SimulationWillRespond(rs.getInt("Id_URL"), rs.getInt("Id_Proxy"), rs.getDouble("simulation_probability"));
                String key = simulationwillrespond.getId_URL() + "-" + simulationwillrespond.getId_Proxy();
                simulationWillrespondsMap.put(key, simulationwillrespond);
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

        return simulationWillrespondsMap;
    }

    public String toString() {
        return ("Id_URL: " + this.Id_URL + ", Id_Proxy: " + this.Id_Proxy + ", TriedOk: " + this.probabilityRejection);
    }
}