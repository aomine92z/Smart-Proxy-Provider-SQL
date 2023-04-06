import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.sql.ResultSet;
import java.util.Collections;

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
            stmt.executeUpdate(
                    "CREATE TABLE URL(Id_URL INTEGER, type INTEGER, country_name VARCHAR(50) NOT NULL, Id_Website INTEGER NOT NULL, PRIMARY KEY(Id_URL), FOREIGN KEY(country_name) REFERENCES Country(country_name), FOREIGN KEY(Id_Website) REFERENCES Website(Id_Website))");

            // Create the covers table
            stmt.executeUpdate(
                    "CREATE TABLE covers(Id_Proxy INTEGER, country_name VARCHAR(50), PRIMARY KEY(Id_Proxy, country_name), FOREIGN KEY(Id_Proxy) REFERENCES Proxy(Id_Proxy), FOREIGN KEY(country_name) REFERENCES Country(country_name))");

            // Create the willRespond table
            stmt.executeUpdate(
                    "CREATE TABLE willRespond(Id_URL INTEGER, Id_Proxy INTEGER, triedOk INTEGER, triedTotal INTEGER, PRIMARY KEY(Id_URL, Id_Proxy), FOREIGN KEY(Id_URL) REFERENCES URL(Id_URL), FOREIGN KEY(Id_Proxy) REFERENCES Proxy(Id_Proxy))");

            // Create the simulation_willRespond table
            stmt.executeUpdate(
                    "CREATE TABLE simulation_willRespond(Id_URL INTEGER, Id_Proxy INTEGER, simulation_probability DOUBLE, PRIMARY KEY(Id_URL, Id_Proxy), FOREIGN KEY(Id_URL) REFERENCES URL(Id_URL), FOREIGN KEY(Id_Proxy) REFERENCES Proxy(Id_Proxy))");

            System.out.println("Database schema created successfully.");
            // Insert data into the Website table
            for (int i = 1; i <= 800; i++) {
                String sql = "INSERT INTO Website (Id_Website) VALUES (?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, i);
                    statement.executeUpdate();
                }
            }

            // Insert data into the Proxy table
            for (int i = 1; i <= 60; i++) {
                String sql = "INSERT INTO Proxy (Id_Proxy, type) VALUES (?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, i);
                    int type = (i - 1) % 4 + 1; // This will cycle through the values 1, 2, 3, 4
                    statement.setInt(2, type);
                    statement.executeUpdate();
                }
            }

            // Insert data into the Country table
            for (String country : getCountries()) {
                String sql = "INSERT INTO Country (country_name) VALUES (?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, country);
                    statement.executeUpdate();
                }
            }

            // Insert data into the URL table
            for (int i = 1; i <= 100000; i++) {
                String sql = "INSERT INTO URL (Id_URL, type, country_name, Id_Website) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, i);
                    statement.setInt(2, (int) (Math.random() * 4) + 1);
                    statement.setString(3, getCountry());
                    statement.setInt(4, (int) (Math.random() * 800) + 1);
                    statement.executeUpdate();
                }
            }

            // Get the list of all proxyids
            PreparedStatement proxyIdStatement = conn.prepareStatement("SELECT Id_Proxy FROM Proxy");
            ResultSet proxyIdResultSet = proxyIdStatement.executeQuery();

            // Get the list of all country names
            PreparedStatement countryNameStatement = conn.prepareStatement("SELECT country_name FROM Country");
            ResultSet countryNameResultSet = countryNameStatement.executeQuery();
            ArrayList<String> countryNames = new ArrayList<>();
            while (countryNameResultSet.next()) {
                countryNames.add(countryNameResultSet.getString("country_name"));
            }

            // Prepare the INSERT statement
            PreparedStatement insertStatement = conn
                    .prepareStatement("INSERT INTO covers (Id_Proxy, country_name) VALUES (?, ?)");

            // For each proxyid
            while (proxyIdResultSet.next()) {
                int proxyId = proxyIdResultSet.getInt("Id_Proxy");
                int randomCountryCount = (int) (Math.random() * 6) + 1; // Random number between 1 and 6
                Collections.shuffle(countryNames); // Shuffle the list of country names

                // Insert unique random country names
                for (int i = 0; i < randomCountryCount; i++) {
                    insertStatement.setInt(1, proxyId);
                    insertStatement.setString(2, countryNames.get(i));
                    insertStatement.executeUpdate();
                }
            }
            // Insert data into the simulation_willRespond table
            for (int i = 1; i <= 100000; i++) {
                // Fetch the URL type and country_name
                int urlType;
                String urlCountryName;
                String fetchUrlDataSql = "SELECT type, country_name FROM URL WHERE Id_URL = ?";
                try (PreparedStatement fetchUrlDataStatement = conn.prepareStatement(fetchUrlDataSql)) {
                    fetchUrlDataStatement.setInt(1, i);
                    ResultSet resultSet = fetchUrlDataStatement.executeQuery();
                    resultSet.next();
                    urlType = resultSet.getInt("type");
                    urlCountryName = resultSet.getString("country_name");
                }

                for (int j = 1; j <= 60; j++) {
                    // Fetch the proxy type
                    int proxyType;
                    String fetchProxyTypeSql = "SELECT type FROM Proxy WHERE Id_Proxy = ?";
                    try (PreparedStatement fetchProxyTypeStatement = conn.prepareStatement(fetchProxyTypeSql)) {
                        fetchProxyTypeStatement.setInt(1, j);
                        ResultSet resultSet = fetchProxyTypeStatement.executeQuery();
                        resultSet.next();
                        proxyType = resultSet.getInt("type");
                    }

                    // Check if the country_name in the covers table matches the country_name in the
                    // URL table
                    boolean countryNameMatches = false;
                    String fetchCountryNameSql = "SELECT country_name FROM covers WHERE Id_Proxy = ?";
                    try (PreparedStatement fetchCountryNameStatement = conn.prepareStatement(fetchCountryNameSql)) {
                        fetchCountryNameStatement.setInt(1, j);
                        ResultSet resultSet = fetchCountryNameStatement.executeQuery();
                        while (resultSet.next()) {
                            String countryName = resultSet.getString("country_name");
                            if (countryName.equals(urlCountryName)) {
                                countryNameMatches = true;
                                break;
                            }
                        }
                    }

                    // Only insert a record if the URL and proxy types match, and the country_name
                    // matches
                    if (urlType == proxyType && countryNameMatches) {
                        String sql = "INSERT INTO simulation_willRespond (Id_URL, Id_Proxy, simulation_probability) VALUES (?, ?, ?)";
                        try (PreparedStatement statement = conn.prepareStatement(sql)) {
                            double randomValue = Math.random();
                            double truncatedValue = Math.floor(randomValue * 10000.0) / 10000.0;
                            statement.setInt(1, i);
                            statement.setInt(2, j);
                            statement.setDouble(3, truncatedValue);
                            statement.executeUpdate();
                        }
                    }
                }
            }

            // // Insert data into the simulation_willRespond table
            // for (int i = 1; i <= 100000; i++) {
            // // Fetch the URL type
            // int urlType;
            // String fetchUrlTypeSql = "SELECT type FROM URL WHERE Id_URL = ?";
            // try (PreparedStatement fetchUrlTypeStatement =
            // conn.prepareStatement(fetchUrlTypeSql)) {
            // fetchUrlTypeStatement.setInt(1, i);
            // ResultSet resultSet = fetchUrlTypeStatement.executeQuery();
            // resultSet.next();
            // urlType = resultSet.getInt("type");
            // }

            // for (int j = 1; j <= 60; j++) {
            // // Fetch the proxy type
            // int proxyType;
            // String fetchProxyTypeSql = "SELECT type FROM Proxy WHERE Id_Proxy = ?";
            // try (PreparedStatement fetchProxyTypeStatement =
            // conn.prepareStatement(fetchProxyTypeSql)) {
            // fetchProxyTypeStatement.setInt(1, j);
            // ResultSet resultSet = fetchProxyTypeStatement.executeQuery();
            // resultSet.next();
            // proxyType = resultSet.getInt("type");
            // }

            // // Only insert a record if the URL and proxy types match
            // if (urlType == proxyType) {
            // String sql = "INSERT INTO simulation_willRespond (Id_URL, Id_Proxy,
            // simulation_probability) VALUES (?, ?, ?)";
            // try (PreparedStatement statement = conn.prepareStatement(sql)) {
            // double randomValue = Math.random();
            // double truncatedValue = Math.floor(randomValue * 10000.0) / 10000.0;
            // statement.setInt(1, i);
            // statement.setInt(2, j);
            // statement.setDouble(3, truncatedValue);
            // statement.executeUpdate();
            // }
            // }
            // }
            // }

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

    private static List<String> getCountries() {
        return Arrays.asList("Norway", "Switzerland", "Iceland", "Netherlands", "Denmark", "Finland",
                "Sweden", "Germany", "Luxembourg", "Ireland", "Australia", "Austria", "New Zealand", "Canada",
                "United Kingdom", "Belgium", "Liechtenstein", "Japan", "Singapore", "France", "Slovenia", "Italy",
                "Spain", "Czech Republic", "Malta", "Estonia", "Greece", "Cyprus", "Poland", "Andorra", "Lithuania",
                "Latvia", "Slovakia", "Portugal", "Hungary", "Croatia", "Qatar", "Brunei", "Romania", "Bulgaria",
                "China", "Belarus", "Montenegro", "Russia", "Argentina", "Oman", "Kazakhstan", "Chile", "Bahrain",
                "Saudi Arabia");
    }

    private static String getCountry() {
        List<String> countries = getCountries();
        Random random = new Random();
        int index = random.nextInt(countries.size());
        return countries.get(index);
    }

}

// 'Afghanistan', 'Albania', 'Algeria', 'Andorra', 'Angola', 'Argentina',
// 'Armenia', 'Australia',
// 'Austria', 'Azerbaijan', 'Bahamas', 'Bahrain', 'Bangladesh', 'Barbados',
// 'Belarus', 'Belgium',
// 'Belize', 'Benin', 'Bhutan', 'Bolivia', 'Bosnia', 'Botswana', 'Brazil',
// 'Brunei', 'Bulgaria',
// 'Burkina Faso', 'Burundi', 'Cabo Verde', 'Cambodia', 'Cameroon', 'Canada',
// 'Chad', 'Chile',
// 'China', 'Colombia', 'Comoros', 'Costa Rica', 'Cote d\'Ivoire', 'Croatia',
// 'Cuba', 'Cyprus',
// 'Czech Republic', 'Denmark', 'Djibouti', 'Dominica', 'Ecuador', 'Egypt', 'El
// Salvador',
// 'Eritrea', 'Estonia', 'Ethiopia', 'Fiji', 'Finland', 'France', 'Gabon',
// 'Gambia', 'Georgia',
// 'Germany', 'Ghana', 'Greece', 'Grenada', 'Guatemala', 'Guinea',
// 'Guinea-Bissau', 'Guyana',
// 'Haiti', 'Honduras', 'Hungary', 'Iceland', 'India', 'Indonesia', 'Iran',
// 'Iraq', 'Ireland',
// 'Israel', 'Italy', 'Jamaica', 'Japan', 'Jordan', 'Kazakhstan', 'Kenya',
// 'Kiribati', 'Kosovo',
// 'Kuwait', 'Kyrgyzstan', 'Laos', 'Latvia', 'Lebanon', 'Lesotho', 'Liberia',
// 'Libya',
// 'Liechtenstein', 'Lithuania', 'Luxembourg', 'Macedonia', 'Madagascar',
// 'Malawi', 'Malaysia',
// 'Maldives', 'Mali', 'Malta', 'Mauritania', 'Mauritius', 'Mexico',
// 'Micronesia', 'Moldova',
// 'Monaco', 'Mongolia', 'Montenegro', 'Morocco', 'Mozambique', 'Myanmar',
// 'Namibia', 'Nauru',
// 'Nepal', 'Netherlands', 'New Zealand', 'Nicaragua', 'Niger', 'Nigeria',
// 'North Korea',
// 'Norway', 'Oman', 'Pakistan', 'Palau', 'Palestine', 'Panama', 'Paraguay',
// 'Peru',
// 'Philippines', 'Poland', 'Portugal', 'Qatar', 'Romania', 'Russia', 'Rwanda',
// 'Saint Lucia',
// 'Samoa', 'Saudi Arabia', 'Senegal', 'Serbia', 'Seychelles', 'Sierra Leone',
// 'Singapore','Suriname'
