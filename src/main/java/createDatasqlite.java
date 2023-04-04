import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class createDatasqlite {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Connect to the database (create a new one if it doesn't exist)
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            // Create the Website table
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE Website(Id_Website INTEGER PRIMARY KEY)");

            // Create the Proxy table
            stmt.executeUpdate("CREATE TABLE Proxy(Id_Proxy INTEGER, type INTEGER, PRIMARY KEY(Id_Proxy))");

            // Create the Country table
            stmt.executeUpdate("CREATE TABLE Country(country_name VARCHAR(50) PRIMARY KEY)");

            // Create the URL table
            stmt.executeUpdate("CREATE TABLE URL(Id_URL INTEGER, type INTEGER, country_name VARCHAR(50) NOT NULL, Id_Website INTEGER NOT NULL, PRIMARY KEY(Id_URL), FOREIGN KEY(country_name) REFERENCES Country(country_name), FOREIGN KEY(Id_Website) REFERENCES Website(Id_Website))");

            // Create the covers table
            stmt.executeUpdate("CREATE TABLE covers(Id_Proxy INTEGER, country_name VARCHAR(50), PRIMARY KEY(Id_Proxy, country_name), FOREIGN KEY(Id_Proxy) REFERENCES Proxy(Id_Proxy), FOREIGN KEY(country_name) REFERENCES Country(country_name))");

            // Create the willRespond table
            stmt.executeUpdate("CREATE TABLE willRespond(Id_URL INTEGER, Id_Proxy INTEGER, triedOk INTEGER, triedTotal INTEGER, PRIMARY KEY(Id_URL, Id_Proxy), FOREIGN KEY(Id_URL) REFERENCES URL(Id_URL), FOREIGN KEY(Id_Proxy) REFERENCES Proxy(Id_Proxy))");

            // Create the simulation_willRespond table
            stmt.executeUpdate("CREATE TABLE simulation_willRespond(Id_URL INTEGER, Id_Proxy INTEGER, simulation_probability DOUBLE, PRIMARY KEY(Id_URL, Id_Proxy), FOREIGN KEY(Id_URL) REFERENCES URL(Id_URL), FOREIGN KEY(Id_Proxy) REFERENCES Proxy(Id_Proxy))");

            System.out.println("Database schema created successfully.");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
