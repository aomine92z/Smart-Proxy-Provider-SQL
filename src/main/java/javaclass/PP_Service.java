package javaclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.jpmml.evaluator.*;
import java.io.File;

public class PP_Service {

    public static class ServiceRunner implements Runnable {
        private List<URL> URL_pack;
        private List<Proxy> proxies;
        private Map<String, WillRespond> willresponds;
        private Map<String, SimulationWillRespond> simuwillresponds;
        private static long startTime;
        private int nbtry;
        private Random random;
        private int localtried;
        private Evaluator model_Evaluator;

        public ServiceRunner(List<URL> URL_pack, List<Proxy> proxies, Map<String, WillRespond> willresponds,
                Map<String, SimulationWillRespond> simuwillresponds, long startTime, int nbtry, Random random,int localtried, Evaluator model_Evaluator) {
            this.URL_pack = URL_pack;
            this.proxies = proxies;
            this.willresponds = willresponds;
            this.simuwillresponds = simuwillresponds;
            ServiceRunner.startTime = startTime;
            this.nbtry = nbtry;
            this.random = random;
            this.localtried = localtried;
            this.model_Evaluator = model_Evaluator;
        }

        public static long getStartTime() {
            return startTime;
        }

        public int getLocalTried() {
            return this.localtried;
        }

        public Map<String, WillRespond> getWillResponds() {
            return this.willresponds;
        }

        public void run() {
            willresponds = callProxyProvider(URL_pack, proxies, willresponds, simuwillresponds, nbtry, random, localtried, model_Evaluator);
        }

    }

    private static Map<String, WillRespond> callProxyProvider(List<URL> URL_pack, List<Proxy> proxies,
            Map<String, WillRespond> willresponds, Map<String, SimulationWillRespond> simuwillresponds, int nbtry, Random random,int localtried, Evaluator model_Evaluator) {
        // call the service with the given arguments
        
        while (URL_pack.size() > 0 && nbtry < 6){

            List<URL> URL_next_pack = new ArrayList<>();

            for (URL url : URL_pack) {

                // Proxy matchingProxy = findMatchingProxy(url, proxies, random);
                String timestamp_proxy = getTime();
                Proxy matchingProxy = findMatchingProxy(url, proxies, random, timestamp_proxy, model_Evaluator);

                if (matchingProxy != null) {

                    String key = url.getId_website() + "-" + matchingProxy.getId_Proxy();

                    SimulationWillRespond foundSimulation = simuwillresponds.get(key);
                    // System.out.println(foundSimulation);
                    Map<String, Object> ProbaTimeStamp = getProba(foundSimulation);
                    double proba = (double) ProbaTimeStamp.get("proba");
                    String timestamp = (String) ProbaTimeStamp.get("timestamp");
                    String key2 = url.getId_URL() + "-" + matchingProxy.getId_Proxy();

                    // Choice of proxy working
                    if (random.nextDouble() > proba) {
                        // System.out.println("Proxy provider service hasn't scrapped Website : " + url.getId_website()
                        //        + " using Proxy : " + matchingProxy.getId_Proxy());
                        WillRespond newWillRespond = new WillRespond(url.getId_website(), matchingProxy.getId_Proxy(),
                                "False", timestamp);
                        String newKey = url.getId_URL() + "-" + matchingProxy.getId_Proxy() + "-" + timestamp;
                        willresponds.put(newKey, newWillRespond);

                        URL_next_pack.add(url);
                        // newWillRespond.saveToDatabase();

                    } else {
                        // System.out.println("Called proxy provider service for Website : " + url.getId_website()
                        //        + " using Proxy : " + matchingProxy.getId_Proxy() + " successfully.");
                        WillRespond newWillRespond = new WillRespond(url.getId_website(), matchingProxy.getId_Proxy(),
                                "True", timestamp);
                        String newKey = url.getId_URL() + "-" + matchingProxy.getId_Proxy() + "-" + timestamp;
                        ;
                        willresponds.put(newKey, newWillRespond);
                        // newWillRespond.saveToDatabase();
                    }
                } else {
                    // System.out.println("No matching proxy found for Website: " + url.getId_website());
                }
            }

            URL_pack = URL_next_pack;
            nbtry+=1;
            localtried = localtried + URL_next_pack.size();
            System.out.println("Amount of loops made :" + nbtry);
        } 
        System.out.println("Here is the number of retries for this thread : " + localtried);
        return willresponds;
    }

    // private static Proxy findMatchingProxy(URL url, List<Proxy> proxies, Random random) {
    //     List<Proxy> matchingProxies = new ArrayList<>();

    //     for (Proxy proxy : proxies) {
    //         if (proxy.getType_Proxy() == url.getType_URL()) {
    //             List<String> proxyCountries = proxy.getCountryName_Proxy();
    //             for (String element : url.getCountry_name_URL())
    //                 if (proxyCountries.contains(element)) {
    //                     matchingProxies.add(proxy);
    //                     break;
    //                 }
    //         }
    //     }

    //     if (matchingProxies.isEmpty()) {
    //         return null;
    //     }

    //     // sort la liste de proxies par probabilités historiques
    //     // on sélectionne le "meilleur" proxy et on le retourne
    //     // si y'en a plusieurs qui ont la même proba faire un random sur la sous liste
    //     // obtenue
    //     int randomIndex = random.nextInt(matchingProxies.size());
    //     return matchingProxies.get(randomIndex); // c'est ici que se fera le choix "intelligent" du proxy
    // }

    private static Proxy findMatchingProxy(URL url, List<Proxy> proxies, Random random, String timestamp, Evaluator model_Evaluator) {
        try {
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

            

            List<Proxy> selectedProxies = new ArrayList<>();
            Proxy selectedProxy = null;
            int webid = url.getId_website();


            for (Proxy proxy : matchingProxies) {

                Map<String, Object> inputData = new HashMap<>();
            
                String[] timeComponents = timestamp.split(":");
                int hours = Integer.parseInt(timeComponents[0]);
                int minutes = Integer.parseInt(timeComponents[1]);
                int seconds = Integer.parseInt(timeComponents[2]);
                int totalSeconds = hours*3600 + minutes*60 + seconds;
 
                inputData.put("Id_proxy", proxy.getId_Proxy());
                inputData.put("Id_Website", webid);
                inputData.put("time_elapsed", totalSeconds);

                Map<String, ?> results = model_Evaluator.evaluate(inputData);
                results = EvaluatorUtil.decodeAll(results);

                Object resultObject = results.get("Success");


                if (resultObject instanceof Integer) {
                    Integer success = (Integer) resultObject;
                    if (success == 1) {
                        selectedProxies.add(proxy);
                    }
                }

            }

            if(selectedProxies.size() > 0){
                int randomIndex = random.nextInt(selectedProxies.size());
                selectedProxy = selectedProxies.get(randomIndex);
            } else{
                return null;
            }
            

            // Random random = new Random();
            // int randomIndex = random.nextInt(matchingProxies.size());
            // return matchingProxies.get(randomIndex); // c'est ici que se fera le choix
            // "intelligent" du proxy
            return selectedProxy;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

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