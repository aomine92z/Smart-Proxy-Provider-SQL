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

// PRE-TACHES : DEFINIR UN RANDOM SEED POUR LA CREATION DE LA BDD  -------- DONE
// PREMIERE TACHE : REMODELER LA BDD
// DEUXIEME TACHE : RELANCER LE ROBOT.JAVA POUR TESTER LE NOUVEAU MODELE DE DONNEES


// NUMBER OF USE I A SHORT TIME IS THE PRIORITY
// principal rejection : website think that the proxy is not a real human
// More you advance in the night more its suspicious if the proxy used doesn't come from the good country
// if a proxy never works on a website : may had been banned from it (the exploration still needs to be done at a frequency we will determine)

public class createDatasqlite {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // Create a new instance of the Random class
            Random random = new Random();

            // Set the seed value
            long seed = 42;
            random.setSeed(seed);

            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Connect to the database (create a new one if it doesn't exist)
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            // Create the Website table
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE Website(Id_Website INTEGER PRIMARY KEY, type INTEGER)");

            // Create the Proxy table
            stmt.executeUpdate("CREATE TABLE Proxy(Id_Proxy INTEGER, type INTEGER, PRIMARY KEY(Id_Proxy))");

            // Create the Country table
            stmt.executeUpdate("CREATE TABLE Country(country_name VARCHAR(50) PRIMARY KEY)");

            // Create the URL table
            stmt.executeUpdate(
                    "CREATE TABLE URL(Id_URL INTEGER, Id_Website INTEGER NOT NULL, PRIMARY KEY(Id_URL), FOREIGN KEY(Id_Website) REFERENCES Website(Id_Website))");

            // Create the proxy_covers table
            stmt.executeUpdate(
                    "CREATE TABLE proxy_covers(Id_Proxy INTEGER, country_name VARCHAR(50), PRIMARY KEY(Id_Proxy, country_name), FOREIGN KEY(Id_Proxy) REFERENCES Proxy(Id_Proxy), FOREIGN KEY(country_name) REFERENCES Country(country_name))");

            // Create the website_covers table
            stmt.executeUpdate(
                    "CREATE TABLE website_covers(Id_Website INTEGER, country_name VARCHAR(50), PRIMARY KEY(Id_Website, country_name), FOREIGN KEY(Id_Website) REFERENCES Proxy(Id_Website), FOREIGN KEY(country_name) REFERENCES Country(country_name))");
            
            // Create the willRespond table
            stmt.executeUpdate(
                    "CREATE TABLE willRespond(Id_Website INTEGER, Id_Proxy INTEGER, success VARCHAR(50), timestamp VARCHAR(50), success1 VARCHAR(50), success2 VARCHAR(50), success3 VARCHAR(50), PRIMARY KEY(Id_Website, Id_Proxy, timestamp), FOREIGN KEY(Id_Website) REFERENCES URL(Id_Website), FOREIGN KEY(Id_Proxy) REFERENCES Proxy(Id_Proxy))");

            // Create the simulation_willRespond table
            stmt.executeUpdate(
                    "CREATE TABLE simulation_willRespond(Id_Website INTEGER, Id_Proxy INTEGER, simulation_probability DOUBLE, parity_feature VARCHAR(50), max_try INTEGER, nbr_try INTEGER, PRIMARY KEY(Id_Website, Id_Proxy), FOREIGN KEY(Id_Website) REFERENCES URL(Id_Website), FOREIGN KEY(Id_Proxy) REFERENCES Proxy(Id_Proxy))");

            System.out.println("Database schema created successfully.");
            // Insert data into the Website table
            for (int i = 1; i <= 800; i++) {
                String sql = "INSERT INTO Website (Id_Website, type) VALUES (?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, i);
                    int type = (int) (random.nextInt(4) + 1); // Generate a random integer between 1 and 4
                    statement.setInt(2, type);
                    statement.executeUpdate();
                }
            }

            // Insert data into the Proxy table
            for (int i = 1; i <= 60; i++) {
                String sql = "INSERT INTO Proxy (Id_Proxy, type) VALUES (?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, i);
                    int type = (int) (random.nextInt(4) + 1); // Generate a random integer between 1 and 4
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
                String sql = "INSERT INTO URL (Id_URL, Id_Website) VALUES (?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, i);
                    statement.setInt(2, (int) (random.nextInt(800) + 1));
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
                    .prepareStatement("INSERT INTO proxy_covers (Id_Proxy, country_name) VALUES (?, ?)");

            // For each proxyid
            while (proxyIdResultSet.next()) {
                int proxyId = proxyIdResultSet.getInt("Id_Proxy");
                int randomCountryCount = (int) (random.nextInt(50) + 1); // Random number between 1 and 6
                Collections.shuffle(countryNames); // Shuffle the list of country names

                // Insert unique random country names
                for (int i = 0; i < randomCountryCount; i++) {
                    insertStatement.setInt(1, proxyId);
                    insertStatement.setString(2, countryNames.get(i));
                    insertStatement.executeUpdate();
                }
            }


            // Get the list of all websiteids
            PreparedStatement websiteIdStatement = conn.prepareStatement("SELECT Id_Website FROM Website");
            ResultSet websiteIdResultSet = websiteIdStatement.executeQuery();

            // Prepare the INSERT statement
            PreparedStatement insertStatement2 = conn
                    .prepareStatement("INSERT INTO website_covers (Id_Website, country_name) VALUES (?, ?)");

            // For each proxyid
            while (websiteIdResultSet.next()) {
                int websiteId = websiteIdResultSet.getInt("Id_Website");
                int randomCountryCount = (int) (random.nextInt(50) + 1); // Random number between 1 and 50
                Collections.shuffle(countryNames); // Shuffle the list of country names

                // Insert unique random country names
                for (int i = 0; i < randomCountryCount; i++) {
                    insertStatement2.setInt(1, websiteId);
                    insertStatement2.setString(2, countryNames.get(i));
                    insertStatement2.executeUpdate();
                }
            }

            // Insert data into the simulation_willRespond table
            for (int i = 1; i <= 800; i++) {
                // Fetch the Website type and country_name
                int WebsiteType;
                String fetchWebsiteDataSql = "SELECT type FROM Website WHERE Id_Website = ?";

                PreparedStatement WebsiteCountryNameStatement = conn.prepareStatement("SELECT country_name FROM website_covers WHERE Id_Website = ?");
                WebsiteCountryNameStatement.setInt(1, i);
                ResultSet WebsiteCountryNameResultSet = WebsiteCountryNameStatement.executeQuery();
                ArrayList<String> WebsiteCountryNames = new ArrayList<>();
                while (WebsiteCountryNameResultSet.next()) {
                    WebsiteCountryNames.add(WebsiteCountryNameResultSet.getString("country_name"));
                }

                try (PreparedStatement fetchWebsiteDataStatement = conn.prepareStatement(fetchWebsiteDataSql)) {
                    fetchWebsiteDataStatement.setInt(1, i);
                    ResultSet resultSet = fetchWebsiteDataStatement.executeQuery();
                    resultSet.next();
                    WebsiteType = resultSet.getInt("type");
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
                    // Website table
                    boolean countryNameMatches = false;
                    String fetchCountryNameSql = "SELECT country_name FROM proxy_covers WHERE Id_Proxy = ?";
                    try (PreparedStatement fetchCountryNameStatement = conn.prepareStatement(fetchCountryNameSql)) {
                        fetchCountryNameStatement.setInt(1, j);
                        ResultSet resultSet = fetchCountryNameStatement.executeQuery();
                        while (resultSet.next()) {
                            String countryName = resultSet.getString("country_name");
                            if (WebsiteCountryNames.contains(countryName)) {
                                countryNameMatches = true;
                                break;
                            }
                        }
                    }

                    // Only insert a record if the URL and proxy types match, and the country_name
                    // matches
                    if (WebsiteType == proxyType && countryNameMatches) {
                        String sql = "INSERT INTO simulation_willRespond (Id_Website, Id_Proxy, simulation_probability, parity_feature, max_try, nbr_try) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement statement = conn.prepareStatement(sql)) {
                            double randomValue = random.nextDouble();
                            double truncatedValue = Math.floor(randomValue * 10000.0) / 10000.0;
                            String parityString = random.nextBoolean() ? "Odd" : "Even";
                            statement.setInt(1, i);
                            statement.setInt(2, j);
                            statement.setDouble(3, truncatedValue);
                            statement.setString(4, parityString);
                            int max_try = (int) (random.nextInt(20000) + 1); // Generate a random integer between 1 and 4
                            statement.setInt(5, max_try);
                            statement.setInt(6, 0);
                            statement.executeUpdate();
                        }
                    }
                }
            }

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

    // private static String getCountry() {
    //     List<String> countries = getCountries();
    //     Random random = new Random();
    //     int index = random.nextInt(countries.size());
    //     return countries.get(index);
    // }

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