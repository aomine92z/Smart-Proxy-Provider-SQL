package javaclass;

import java.util.List;

public class PP_Service {

    public static class ServiceRunner implements Runnable {
        private List<URL> URL_pack;

        public ServiceRunner(List<URL> URL_pack) {
            this.URL_pack = URL_pack;
        }

        public void run() {
            callProxyProvider(URL_pack);
        }
    }

    private static void callProxyProvider(List<URL> URL_pack) {
        // call the service with the given arguments
        System.out.println("Bonjour");
    }
}