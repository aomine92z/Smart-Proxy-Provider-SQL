package javaclass;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PP_Service {

    public static class ServiceRunner implements Runnable {
        private List<URL> URL_pack;
        private List<Proxy> proxies;
        private Map<String, WillRespond> willresponds;
        private Map<String, SimulationWillRespond> simuwillresponds;
        private static long startTime;
        private int nbtry;
        private Random random;
        private Map<String, WillRespond> CurrentWillRespond;

        public ServiceRunner(List<URL> URL_pack, List<Proxy> proxies, Map<String, WillRespond> willresponds,
                Map<String, SimulationWillRespond> simuwillresponds, long startTime, int nbtry, Random random, Map<String, WillRespond> CurrentWillRespond) {
            this.URL_pack = URL_pack;
            this.proxies = proxies;
            this.willresponds = willresponds;
            this.simuwillresponds = simuwillresponds;
            ServiceRunner.startTime = startTime;
            this.nbtry = nbtry;
            this.random = random;
            this.CurrentWillRespond = CurrentWillRespond;
        }

        public static long getStartTime() {
            return startTime;
        }

        public Map<String, WillRespond> getWillResponds() {
            return this.willresponds;
        }

        public void run() {
            willresponds = callProxyProvider(URL_pack, proxies, willresponds, simuwillresponds, nbtry, random, CurrentWillRespond);
        }

    }

    private static Map<String, WillRespond> callProxyProvider(List<URL> URL_pack, List<Proxy> proxies,
            Map<String, WillRespond> willresponds, Map<String, SimulationWillRespond> simuwillresponds, int nbtry, Random random, Map<String, WillRespond> CurrentWillRespondMap) {
        // call the service with the given arguments
        List<URL> URL_next_pack = new ArrayList<>();

        if (URL_pack.size() > 0 && nbtry < 6){

            for (URL url : URL_pack) {
                String success1 = "False";
                String success2 = "False";

                Proxy matchingProxy = findMatchingProxy(url, proxies, random);

                if (matchingProxy != null) {
                    
                    String key = url.getId_website() + "-" + matchingProxy.getId_Proxy();

                    SimulationWillRespond foundSimulation = simuwillresponds.get(key);
                    System.out.println(foundSimulation);
                    Map<String, Object> ProbaTimeStamp = getProba(foundSimulation);
                    double proba = (double) ProbaTimeStamp.get("proba");
                    String timestamp = (String) ProbaTimeStamp.get("timestamp");
                    String key2 = url.getId_URL() + "-" + matchingProxy.getId_Proxy();

                    WillRespond previousWR = findClosestWR(url.getId_website(), matchingProxy.getId_Proxy(), CurrentWillRespondMap);

                    if (previousWR != null){
                        success1 = previousWR.get_Success();
                        success2 = previousWR.get_Success1();
                    }

                    // Choice of proxy working
                    if (random.nextDouble() > proba) {
                        System.out.println("Proxy provider service hasn't scrapped Website : " + url.getId_website()
                                + " using Proxy : " + matchingProxy.getId_Proxy());
                        WillRespond newWillRespond = new WillRespond(url.getId_website(), matchingProxy.getId_Proxy(),
                                "False", timestamp, success1, success2);
                        String newKey = url.getId_URL() + "-" + matchingProxy.getId_Proxy() + "-" + timestamp;
                        willresponds.put(newKey, newWillRespond);

                        URL_next_pack.add(url);
                        // newWillRespond.saveToDatabase();

                    } else {
                        System.out.println("Called proxy provider service for Website : " + url.getId_website()
                                + " using Proxy : " + matchingProxy.getId_Proxy() + " successfully.");
                        WillRespond newWillRespond = new WillRespond(url.getId_website(), matchingProxy.getId_Proxy(),
                                "True", timestamp, success1, success2);
                        String newKey = url.getId_URL() + "-" + matchingProxy.getId_Proxy() + "-" + timestamp;
                        ;
                        willresponds.put(newKey, newWillRespond);
                        // newWillRespond.saveToDatabase();
                    }
                } else {
                    System.out.println("No matching proxy found for Website: " + url.getId_website());
                }
            }
            callProxyProvider(URL_next_pack, proxies, willresponds, simuwillresponds, nbtry+1, random, CurrentWillRespondMap);
        }
        return willresponds;
    }

    // private static Proxy findMatchingProxy(URL url, List<Proxy> proxies) {
    // for (Proxy proxy : proxies) {
    // if (proxy.getType_Proxy() == url.getType_URL()) {
    // List<String> proxyCountries = proxy.getCountryName_Proxy();
    // if (proxyCountries.contains(url.getCountry_name_URL())) {
    // return proxy;
    // }
    // }
    // }
    // return null;
    // }

    private static Proxy findMatchingProxy(URL url, List<Proxy> proxies, Random random) {
        List<Proxy> matchingProxies = new ArrayList<>();

        for (Proxy proxy : proxies) {
            if (proxy.getType_Proxy() == url.getType_URL()) {
                List<String> proxyCountries = proxy.getCountryName_Proxy();
                for (String element : url.getCountry_name_URL())
                    if (proxyCountries.contains(element)) {
                        matchingProxies.add(proxy);
                        break;
                    }
            }
        }

        if (matchingProxies.isEmpty()) {
            return null;
        }

        // sort la liste de proxies par probabilités historiques
        // on sélectionne le "meilleur" proxy et on le retourne
        // si y'en a plusieurs qui ont la même proba faire un random sur la sous liste
        // obtenue
        int randomIndex = random.nextInt(matchingProxies.size());
        return matchingProxies.get(randomIndex); // c'est ici que se fera le choix "intelligent" du proxy
    }

    private static String getTime() {
        // Receive the current timestamp
        long elapsedTime = System.currentTimeMillis() - ServiceRunner.getStartTime();

        // Convert the timestamp to minutes, hours, and centiseconds
        long elapsedSeconds = elapsedTime / 1000;
        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        long centiseconds = (elapsedTime % 1000) / 10; // Get two-digit centiseconds

        // Format the time units with leading zeros if necessary
        String time = String.format("%d:%02d:%02d", minutes, seconds, centiseconds);
        return time;
    }



    private static WillRespond findClosestWR(int id_website, int id_proxy, Map<String, WillRespond> C_willresponds) {
    
        String keySearched = id_website + "-" + id_proxy;
        Map<String, WillRespond> listWR = new HashMap<>();

        for (String key : C_willresponds.keySet()) {
            if (key.contains(keySearched)) {
                listWR.put(key, C_willresponds.get(key));
            }
        }

        // Create a map to associate WillRespond objects with their computation scores
        Map<WillRespond, Integer> computationScores = new HashMap<>();

        for (Map.Entry<String, WillRespond> entry : listWR.entrySet()) {
            String timestamp = entry.getValue().get_Timestamp();

            String[] parts = timestamp.split(":"); // Divisez la chaîne en parties (heure, minutes, secondes)

            int heure = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int secondes = Integer.parseInt(parts[2]);

            int computation = heure * 100 + minutes * 10 + secondes;

            // Associate the computation score with the WillRespond object in the map
            computationScores.put(entry.getValue(), computation);
        }

        // Convert the entry set of the map to a list
        List<Map.Entry<WillRespond, Integer>> sortedList = new ArrayList<>(computationScores.entrySet());

        // Sort the list based on the computation scores in descending order
        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        return sortedList.get(0).getKey();
    }


    // private boolean getAttempt() {

    // }

    // private static void countTries(URL url, Proxy proxy, boolean TriedOk){
    // Connection conn = null;
    // try {
    // // Load the SQLite JDBC driver
    // Class.forName("org.sqlite.JDBC");

    // // Connect to the database (create a new one if it doesn't exist)
    // conn = DriverManager.getConnection("jdbc:sqlite:test.db");

    // // String sql = "UPDATE INTO willRespond (Id_URL, Id_Proxy, triedOk,
    // triedTotal) VALUES (?, ?, ?, COALESCE((SELECT triedTotal FROM willRespond
    // WHERE Id_URL = ? AND Id_Proxy = ?), 0) + 1)";

    // // try (PreparedStatement statement = conn.prepareStatement(sql)) {
    // // statement.setInt(1, url.getId_URL());
    // // statement.setInt(2, proxy.getId_Proxy());
    // // statement.setInt(3, TriedOk ? 1 : 0);
    // // statement.setInt(4, 1);
    // // int rows = statement.executeUpdate();
    // // }

    // String sql = "INSERT INTO willRespond (Id_URL, Id_Proxy, triedOk, triedTotal)
    // " +
    // "VALUES (?, ?, ?, 1) " +
    // "ON CONFLICT(Id_URL, Id_Proxy) DO " +
    // "UPDATE SET triedOk = triedOk + ?, triedTotal = triedTotal + 1";

    // try (PreparedStatement statement = conn.prepareStatement(sql)) {
    // statement.setInt(1, url.getId_URL());
    // statement.setInt(2, proxy.getId_Proxy());
    // statement.setInt(3, TriedOk ? 1 : 0);
    // statement.setInt(4, TriedOk ? 1 : 0);
    // statement.executeUpdate();
    // }

    // } catch (ClassNotFoundException | SQLException e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if (conn != null) {
    // conn.close();
    // }
    // } catch (SQLException e) {
    // e.printStackTrace();
    // }
    // }

    // }

    // Recevoir la probabilité pour qu'un proxy fonctionne
    public static Map<String, Object> getProba(SimulationWillRespond foundSimulation) {
        double proba = 0;
        String timestamp = PP_Service.getTime();
        String currentHourParity;
        int currentHour = Integer.parseInt(timestamp.substring(0, 1));
        String workingHours = foundSimulation.getHoursWorking();

        if (currentHour % 2 == 0) {
            currentHourParity = "Even";
            if (workingHours.equals(currentHourParity)) {
                proba = foundSimulation.getProbabilityRejection();
            }
        } else {
            currentHourParity = "Odd";
            if (workingHours.equals(currentHourParity)) {
                proba = foundSimulation.getProbabilityRejection();
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("proba", proba);
        result.put("timestamp", timestamp);
        return result;
    }
}