import java.util.List;
import javaclass.URL;

import java.util.ArrayList;
import java.util.Collections;

public class robot {
    public static void main(String[] args) {
        List<URL> urls = URL.loadURL();
        Collections.shuffle(urls);
        // for (int i=0; i<10; i++) {
        //     System.out.println(urls.get(i).toString());
        // }
        int fullsize = urls.size()/5;

        List<URL> URL_pack1 = urls.subList(0, fullsize);
        List<URL> URL_pack2 = urls.subList(fullsize, fullsize*2);
        List<URL> URL_pack3 = urls.subList(fullsize*2, fullsize*3);
        List<URL> URL_pack4 = urls.subList(fullsize*3, fullsize*4);
        List<URL> URL_pack5 = urls.subList(fullsize*4, fullsize*5);

        List<List<URL>> URL_Big_Pack = new ArrayList<List<URL>>();
        URL_Big_Pack.add(URL_pack1);
        URL_Big_Pack.add(URL_pack2);
        URL_Big_Pack.add(URL_pack3);
        URL_Big_Pack.add(URL_pack4);
        URL_Big_Pack.add(URL_pack5);

        for (List<URL> URL_pack : URL_Big_Pack) {
            for (URL url : URL_pack) {
                String searched_country = url.getCountry_name_URL();
                String searched_type = url.getType_URL();
                //callProxyProvider(URL_Big_Pack[URL_pack].index) // First argument correspond to the service called (1,2,3,4 or 5)
                                                                // Second Argument country name
                                                                // Second Argument type
            }
        }
    }
}
