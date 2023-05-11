import java.util.List;
import java.util.Map;

import javaclass.URL;
import javaclass.Proxy;
import javaclass.SimulationWillRespond;
import javaclass.WillRespond;
import javaclass.PP_Service;
import java.util.ArrayList;
import java.util.Collections;

public class robot {
    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (6 * 60 * 1000);

        while (System.currentTimeMillis() < endTime) {

            List<URL> urls = URL.load_URLs();
            List<Proxy> proxies = Proxy.load_Proxies();
            Map<String, WillRespond> willresponds = WillRespond.load_willRespond();
            Map<String, SimulationWillRespond> simuwillresponds = SimulationWillRespond.load_simulationWillRespond();

            // for (int i = 0; i < willresponds.size(); i++) {
            // System.out.println(willresponds.get(i).toString());
            // }
            // for (int i = 0; i < 10; i++) {
            // System.out.println(proxies.get(i).toString());
            // }
            Collections.shuffle(urls);
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
            serviceRunners.add(new PP_Service.ServiceRunner(URL_pack1, proxies, willresponds, simuwillresponds));
            serviceRunners.add(new PP_Service.ServiceRunner(URL_pack2, proxies, willresponds, simuwillresponds));
            serviceRunners.add(new PP_Service.ServiceRunner(URL_pack3, proxies, willresponds, simuwillresponds));
            serviceRunners.add(new PP_Service.ServiceRunner(URL_pack4, proxies, willresponds, simuwillresponds));
            serviceRunners.add(new PP_Service.ServiceRunner(URL_pack5, proxies, willresponds, simuwillresponds));

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

            Thread.sleep(1000);
        }

    }
}