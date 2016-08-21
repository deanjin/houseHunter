package net.dean.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;

/**
 * Created by dean on 16/8/13.
 * 伪造http报文以绕过网站安全策略
 */
public class CommonHttpURLConnection {

    private final static Logger log  = LoggerFactory.getLogger(CommonHttpURLConnection.class);

    public static URLConnection getURLConnection(String url){
        URLConnection urlConnection = null;
        try{urlConnection = new URL(url).openConnection();
        urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/601.4.4");
//        urlConnection.setRequestProperty("X-Forward-For","115.205.144.23");
//        urlConnection.setRequestProperty("Client-IP","115.205.144.23");
        urlConnection.setRequestProperty("Cookie","Hm_lpvt_d7682ab43891c68a00de46e9ce5b76aa=1471043278; Hm_lvt_d7682ab43891c68a00de46e9ce5b76aa=1470990241; pgv_pvid=5516016926; CNZZDATA1253675216=712919559-1470787474-http%253A%252F%252Fwww.tmsf.com%252F%7C1471041400; IESESSION=alive; JSESSIONID=6B4D231FBB77C5DB6C420E969DE1079A.lb3; ROUTEID=.lb3; __jsluid=a3eb302a0320f8f11b836ac0c7b08941; __qc_wId=831; _qddab=3-9a62e6.irsb2ydr; _qddamta_800055708=3-0; Hm_lpvt_bbb8b9db5fbc7576fd868d7931c80ee1=1471043316; Hm_lvt_bbb8b9db5fbc7576fd868d7931c80ee1=1470790610,1471039722,1471041894; pgv_pvi=5287058432; pgv_si=s153255936");
        }catch (Exception e){
            log.error("failed to generate URL Connection:{}, exception:{}",url,e);
            FileOP.writeFile("log/daily_error_"+ LocalDate.now().toString(),String.format("failed to generate URL Connection:%s, exception:%s",url,e));
        }
        return urlConnection;
    }
}
