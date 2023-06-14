import java.util.List;
import java.util.Map;
import java.util.Random;

import javaclass.URL;
import javaclass.Website;
import javaclass.Proxy;
import javaclass.SimulationWillRespond;
import javaclass.WillRespond;
import javaclass.PP_Service;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;

public class robot {
    public static void main(String[] args) throws Exception {
        // Create a new instance of the Random class
        Random random = new Random();

        // Set the seed value
        long seed = 52;

        random.setSeed(seed);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (6 * 60 * 1000);
        AtomicInteger globaltried = new AtomicInteger(0);

        while (System.currentTimeMillis() < endTime) {

            // Recevoir le timestamp actuel
            long elapsedTime = System.currentTimeMillis() - startTime;

            // Transformer le timestamp en seconds et minutes (qui represente d'heures en
            // realité)
            long elapsedSeconds = elapsedTime / 1000;
            long hour = elapsedSeconds / 60;
            long seconds = elapsedSeconds % 60;

            // Fusionner les unités de temps
            String time = hour + ":" + seconds + ":" + elapsedTime;

            // First we load the data
            Map<Integer, Website> websites = Website.load_Websites();
            List<URL> urls = URL.load_URLs(websites);
            List<Proxy> proxies = Proxy.load_Proxies();
            Map<String, WillRespond> willresponds = WillRespond.load_willRespond();
            Map<String, SimulationWillRespond> simuwillresponds = SimulationWillRespond.load_simulationWillRespond();
            Evaluator evaluator = new LoadingModelEvaluatorBuilder().load(new File("model/trained_model.pmml")).build();

            // for (int i = 0; i < willresponds.size(); i++) {
            // System.out.println(willresponds.get(i).toString());
            // }
            // for (int i = 0; i < 10; i++) {
            // System.out.println(proxies.get(i).toString());
            // }
            Collections.shuffle(urls, random);
            // for (int i = 0; i < 10; i++) {
            // System.out.println(urls.get(i).toString());
            // }
            int fullsize = urls.size() / 5;

            List<URL> URL_pack1 = urls.subList(0, fullsize);
            List<URL> URL_pack2 = urls.subList(fullsize, fullsize * 2);
            List<URL> URL_pack3 = urls.subList(fullsize * 2, fullsize * 3);
            List<URL> URL_pack4 = urls.subList(fullsize * 3, fullsize * 4);
            List<URL> URL_pack5 = urls.subList(fullsize * 4, fullsize * 5);

            // List<List<URL>> URL_Big_Pack = new ArrayList<List<URL>>();
            // URL_Big_Pack.add(URL_pack1);
            // URL_Big_Pack.add(URL_pack2);
            // URL_Big_Pack.add(URL_pack3);
            // URL_Big_Pack.add(URL_pack4);
            // URL_Big_Pack.add(URL_pack5);

            List<PP_Service.ServiceRunner> serviceRunners = new ArrayList<>();
            serviceRunners
                    .add(new PP_Service.ServiceRunner(URL_pack1, proxies, willresponds, simuwillresponds, startTime, 1, random, 0, evaluator));
            serviceRunners
                    .add(new PP_Service.ServiceRunner(URL_pack2, proxies, willresponds, simuwillresponds, startTime, 1, random, 0, evaluator));
            serviceRunners
                    .add(new PP_Service.ServiceRunner(URL_pack3, proxies, willresponds, simuwillresponds, startTime, 1, random, 0, evaluator));
            serviceRunners
                    .add(new PP_Service.ServiceRunner(URL_pack4, proxies, willresponds, simuwillresponds, startTime, 1, random, 0, evaluator));
            serviceRunners
                    .add(new PP_Service.ServiceRunner(URL_pack5, proxies, willresponds, simuwillresponds, startTime, 1, random, 0, evaluator));

            List<Thread> threads = new ArrayList<>();

            // for (int i = 0; i < URL_Big_Pack.size(); i++) {
            // PP_Service.ServiceRunner serviceRunner = new
            // PP_Service.ServiceRunner(URL_Big_Pack.get(i), proxies);
            // List<URL> URL_pack = URL_Big_Pack.get(i);
            // String countryName = URL_pack.get(0).getCountryName_URL();
            // int type = URL_pack.get(0).getType_URL();

            for (PP_Service.ServiceRunner serviceRunner : serviceRunners) {
                Thread thread = new Thread(serviceRunner);
                threads.add(thread);
                thread.start();
            }
            
            // for (Thread thread : threads) {
            //     thread.join();
            // }
            
            for (Thread thread : threads) {
                thread.join();
                }
            
            // for (PP_Service.ServiceRunner serviceRunner : serviceRunners) {
            //     // System.out.println("localtried: " + serviceRunner.getLocalTriedResult());
            //     System.out.println("localtried: " + serviceRunner.getLocalTried());
            //     globaltried.addAndGet(serviceRunner.getLocalTried().get()); 
            // }

            // System.out.println("localtried: " + localtried);
            // globaltried += localtried;
        
            // System.out.println("globaltried: " + globaltried);

            Map<String, WillRespond> willRespondFinal = new HashMap<>();
            for (PP_Service.ServiceRunner serviceRunner : serviceRunners) {
                willRespondFinal.putAll(serviceRunner.getWillResponds());
            }
            // for (List<URL> URL_pack : URL_Big_Pack) {
            // for (URL url : URL_pack) {
            // String searched_country = url.getCountryName_URL();
            // int searched_type = url.getType_URL();
            // //callProxyProvider(URL_Big_Pack[URL_pack].index) // First argument
            // correspond to the service called (1,2,3,4 or 5)
            // // Second Argument country name
            // // Second Argument type
            // }
            // }
            saveData(willRespondFinal);

            Thread.sleep(60000);
        }
        

    }
    

    public static void saveData(Map<String, WillRespond> willRespondsMap) {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Connexion à la base de données
            // System.out.println(willRespondsMap.size());
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");

            for (Map.Entry<String, WillRespond> willRespond : willRespondsMap.entrySet()) {
                String sql = "INSERT OR REPLACE INTO willRespond (Id_Website, Id_Proxy, success, timestamp) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setInt(1, willRespond.getValue().getId_Website());
                    statement.setInt(2, willRespond.getValue().getId_Proxy());
                    statement.setString(3, willRespond.getValue().get_Success());
                    statement.setString(4, willRespond.getValue().get_Timestamp());
                    statement.executeUpdate();
                }
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
    }
}