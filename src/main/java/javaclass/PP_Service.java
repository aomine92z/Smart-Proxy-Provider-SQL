package javaclass;

import java.util.ArrayList;
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

        public ServiceRunner(List<URL> URL_pack, List<Proxy> proxies, Map<String, WillRespond> willresponds,
                Map<String, SimulationWillRespond> simuwillresponds, long startTime) {
            this.URL_pack = URL_pack;
            this.proxies = proxies;
            this.willresponds = willresponds;
            this.simuwillresponds = simuwillresponds;
            ServiceRunner.startTime = startTime;
        }

        public static long getStartTime() {
            return startTime;
        }

        public void run() {
            callProxyProvider(URL_pack, proxies, willresponds, simuwillresponds);
        }

    }

    private static Map<String, WillRespond> callProxyProvider(List<URL> URL_pack, List<Proxy> proxies,
            Map<String, WillRespond> willresponds, Map<String, SimulationWillRespond> simuwillresponds) {
        // call the service with the given arguments

        for (URL url : URL_pack) {
            Proxy matchingProxy = findMatchingProxy(url, proxies);

            if (matchingProxy != null) {
                String key = url.getId_URL() + "-" + matchingProxy.getId_Proxy();
                WillRespond foundWillRespond = willresponds.get(key);
                SimulationWillRespond foundSimulation = simuwillresponds.get(key);

                double proba = getProba(foundSimulation);

                // Choice of proxy working
                if (Math.random() > foundSimulation.getProbabilityRejection()) {
                    if (foundWillRespond != null) {
                        System.out.println("Called proxy provider service for URL : " +
                                url.getId_URL()
                                + " using Proxy : " + matchingProxy.getId_Proxy() + " successfully.");
                        foundWillRespond.addNewSuccess();
                        foundWillRespond.addNewTry();
                        willresponds.put(key, foundWillRespond);
                    } else {
                        WillRespond newWillRespond = new WillRespond(url.getId_URL(),
                                matchingProxy.getId_Proxy(), 1,
                                1);
                        String newKey = url.getId_URL() + "-" + matchingProxy.getId_Proxy();
                        newWillRespond.addNewSuccess();
                        newWillRespond.addNewTry();
                        willresponds.put(newKey, newWillRespond);
                    }
                } else {
                    System.out.println("Proxy provider service hasn't scrapped URL : " +
                            url.getId_URL()
                            + " using Proxy : " + matchingProxy.getId_Proxy());
                    if (foundWillRespond != null) {
                        foundWillRespond.addNewTry();
                        willresponds.put(key, foundWillRespond);
                    } else {
                        WillRespond newWillRespond = new WillRespond(url.getId_URL(),
                                matchingProxy.getId_Proxy(), 1,
                                1);
                        String newKey = url.getId_URL() + "-" + matchingProxy.getId_Proxy();
                        newWillRespond.addNewTry();
                        willresponds.put(newKey, newWillRespond);
                    }
                }
            } else {
                System.out.println("No matching proxy found for URL: " + url.toString());
            }

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

    private static Proxy findMatchingProxy(URL url, List<Proxy> proxies) {
        List<Proxy> matchingProxies = new ArrayList<>();

        for (Proxy proxy : proxies) {
            if (proxy.getType_Proxy() == url.getType_URL()) {
                List<String> proxyCountries = proxy.getCountryName_Proxy();
                if (proxyCountries.contains(url.getCountry_name_URL())) {
                    matchingProxies.add(proxy);
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

        Random random = new Random();
        int randomIndex = random.nextInt(matchingProxies.size());
        return matchingProxies.get(randomIndex); // c'est ici que se fera le choix "intelligent" du proxy
    }

    private static String getTime() {
        // Recevoir le timestamp actuel
        long elapsedTime = System.currentTimeMillis() - ServiceRunner.getStartTime();

        // Transformer le timestamp en seconds et minutes (qui represente d'heures en
        // realité)
        long elapsedSeconds = elapsedTime / 1000;
        long hour = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;

        // Fusionner les unités de temps
        String time = hour + ":" + seconds + ":" + elapsedTime;

        return time;
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
    public static double getProba(SimulationWillRespond foundSimulation) {
        double proba = 0;

        // Récupérer l'heure actuelle et le convertir en int
        String timestamp = PP_Service.getTime();
        String currentHourParity;
        int currentHour = Integer.parseInt(timestamp.substring(0, 0));
        String workingHours = foundSimulation.getHoursWorking();

        if (currentHour % 2 != 0) {
            currentHourParity = "Even";
            if (workingHours == currentHourParity) {
                proba = foundSimulation.getProbabilityRejection();
            }
        } else {
            currentHourParity = "Odd";
            if (workingHours == currentHourParity) {
                proba = foundSimulation.getProbabilityRejection();
            }
        }
        return proba;
    }
}