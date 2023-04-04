import java.sql.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class InsertDATA {


                                // DO NOT RUN THIS FILE
    public static void main(String[] args) {
        
        String url = "jdbc:mysql://127.0.0.1:3307/proxyproviderdb?serverTimezone=UTC";
        String username = "root";
        String password = "amineoiu";
        
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            
            // Insert 800 websites
            for (int i = 1; i <= 800; i++) {
                String websiteId = "W" + i;
                String websiteName = "Website " + i;
                
                String sql = "INSERT INTO website (website_id, website_name) VALUES (?, ?)";
                
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, websiteId);
                    statement.setString(2, websiteName);
                    statement.executeUpdate();
                }
            }
            
            // Insert 100000 URLs
            for (int i = 1; i <= 100000; i++) {
                String urlId = "URL" + i;
                String websiteId = "Website" + (int)(Math.random() * 800) + 1;
                
                String urlCountry = getCountry();

                Random rand = new Random();
                int randomNum = rand.nextInt(4) + 1;
                String urlType = "Type " + randomNum;
                
                String sql = "INSERT INTO url (url_id, website_id, url_country, url_type) VALUES (?, ?, ?, ?)";
                
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, urlId);
                    statement.setString(2, websiteId);
                    statement.setString(3, urlCountry);
                    statement.setString(4, urlType);
                    statement.executeUpdate();
                }
            }
            
            // Insert 50 proxies
            for (int i = 1; i <= 60; i++) {
                String proxyId = "P" + i;

                ArrayList<String> countriesProxie = getCountries();
                String countriesConcat = String.join(",", countriesProxie);
                String proxyType = "Type " + i;
                // int proxyProbability = (int)(Math.random() * 100) + 1;
                // statement.setInt(4, proxyProbability);

                String sql = "INSERT INTO proxie (proxie_id, proxie_country, proxie_type) VALUES (?, ?, ?)";
                
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, proxyId);
                    statement.setString(2, countriesConcat);
                    statement.setString(3, proxyType);
                    statement.executeUpdate();
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    public static String getCountry(){
        String[] countries = {"Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Chad", "Chile", "China", "Colombia", "Comoros", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Ecuador", "Egypt", "El Salvador", "Eritrea", "Estonia", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea", "Norway", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Saint Lucia", "Samoa", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Suriname"};
        Random randi = new Random();
        int randomNum = randi.nextInt(countries.length);
        String choosedCountry = countries[randomNum];
        return choosedCountry;
    }

    public static ArrayList<String> getCountries(){
        ArrayList<String> countries = new ArrayList<String>(Arrays.asList("Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Chad", "Chile", "China", "Colombia", "Comoros", "Costa Rica", "Cote d'Ivoire", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Ecuador", "Egypt", "El Salvador", "Eritrea", "Estonia", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Korea", "Norway", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Saint Lucia", "Samoa", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Suriname"));
        Collections.shuffle((countries));
        Random randi = new Random();
        int randomNum = randi.nextInt(8) + 2;
        ArrayList<String> choosedCountry = new ArrayList<String>(countries.subList(0, randomNum));
        return choosedCountry;
 }
}