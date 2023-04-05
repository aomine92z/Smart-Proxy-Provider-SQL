import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
        return Arrays.asList("Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Argentina", "Armenia",
                "Australia",
                "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
                "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia", "Botswana", "Brazil", "Brunei", "Bulgaria",
                "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Chad", "Chile", "China",
                "Colombia", "Comoros", "Costa Rica", "Cote d\'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic",
                "Denmark", "Djibouti", "Dominica", "Ecuador", "Egypt", "El Salvador", "Eritrea", "Estonia", "Ethiopia",
                "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada",
                "Guatemala",
                "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia",
                "Iran",
                "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati",
                "Kosovo",
                "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",
                "Lithuania",
                "Luxembourg", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
                "Mauritania", "Mauritius",
                "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique",
                "Myanmar", "Namibia",
                "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea",
                "Norway",
                "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Paraguay", "Peru", "Philippines", "Poland",
                "Portugal",
                "Qatar", "Romania", "Russia", "Rwanda", "Saint Lucia", "Samoa", "Saudi Arabia", "Senegal", "Serbia",
                "Seychelles", "Sierra Leone", "Singapore", "Suriname");
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
