// import javaclass.Url;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.ArrayList;
// import java.util.List;

// public class robot {

//     public static List<Url> loadUrls() {
//         List<Url> urls = new ArrayList<>();

//         try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db")) {
//             Statement stmt = conn.createStatement();
//             ResultSet resultSet = stmt.executeQuery("SELECT * FROM URL");

//             while (resultSet.next()) {
//                 int idUrl = resultSet.getInt("Id_URL");
//                 int type = resultSet.getInt("type");
//                 String countryName = resultSet.getString("country_name");
//                 int idWebsite = resultSet.getInt("Id_Website");

//                 Url url = new Url(idUrl, type, countryName, idWebsite);
//                 urls.add(url);
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }

//         return urls;
//     }

//     public static void main(String[] args) {
//         List<Url> urls = loadUrls();
//         // Do something with the urls, e.g., print them
//         for (Url url : urls) {
//             System.out.println("Id_URL: " + url.getIdUrl() + ", Type: " + url.getType() + ", Country: "
//                     + url.getCountryName() + ", Id_Website: " + url.getIdWebsite());
//         }
//     }
// }

import javaclass.Url;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class robot {

    public static List<Url> loadUrls() {
        List<Url> urls = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM URL");

            while (resultSet.next()) {
                int idUrl = resultSet.getInt("Id_URL");
                int type = resultSet.getInt("type");
                String countryName = resultSet.getString("country_name");
                int idWebsite = resultSet.getInt("Id_Website");

                Url url = new Url(idUrl, type, countryName, idWebsite);
                urls.add(url);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return urls;
    }

    public static List<Url> generateRandomUrls(int urlCount, List<Url> urlTable) {
        List<Url> randomUrls = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < urlCount; i++) {
            int randomIndex = random.nextInt(urlTable.size());
            randomUrls.add(urlTable.get(randomIndex));
        }

        return randomUrls;
    }

    public static void main(String[] args) {
        List<Url> urls = loadUrls();

        // Generate a random list of URLs from the loaded URLs
        List<Url> randomUrls = generateRandomUrls(1000, urls);

        // Print the random URLs
        for (Url url : randomUrls) {
            System.out.println("Id_URL: " + url.getIdUrl() + ", Type: " + url.getType() + ", Country: "
                    + url.getCountryName() + ", Id_Website: " + url.getIdWebsite());
        }
    }
}
