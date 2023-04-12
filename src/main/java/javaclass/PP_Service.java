package javaclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PP_Service {

    public static class ServiceRunner implements Runnable {
        private List<URL> URL_pack;
        private List<Proxy> proxies;

        public ServiceRunner(List<URL> URL_pack, List<Proxy> proxies) {
            this.URL_pack = URL_pack;
            this.proxies = proxies;
        }

        public void run() {
            callProxyProvider(URL_pack, proxies);
        }
    }

    private static void callProxyProvider(List<URL> URL_pack, List<Proxy> proxies) {
        // call the service with the given arguments
        for (URL url : URL_pack) {
            Proxy matchingProxy = findMatchingProxy(url, proxies);

            if (matchingProxy != null) {
                System.out.println("Calling proxy provider service with URL: " + url.toString() + " using Proxy ID: "
                        + matchingProxy.getId_Proxy());
                // Call the proxy provider service with the matching proxy
            } else {
                System.out.println("No matching proxy found for URL: " + url.toString());
            }
        }
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

        Random random = new Random();
        int randomIndex = random.nextInt(matchingProxies.size());
        return matchingProxies.get(randomIndex);
    }

}