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

//        61.155.222.162

//        GET /daily.htm HTTP/1.1
//        Host: www.tmsf.com
//        Accept-Encoding: gzip, deflate
//        Cookie: CNZZDATA1253675216=348674432-1471762978-http%253A%252F%252Fwww.tmsf.com%252F%7C1471865998; JSESSIONID=454EAF8B1B533F90B12B81489322F114.lb1; ROUTEID=.lb2; __jsl_clearance=1471867618.735|0|YvIouxtcO618%2FU%2FaGdTB9cHf30k%3D; __jsluid=884bc1a619f3dc8efda068b15b80596a; Hm_lpvt_bbb8b9db5fbc7576fd868d7931c80ee1=1471869232; Hm_lvt_bbb8b9db5fbc7576fd868d7931c80ee1=1471767551,1471869112; pgv_pvi=440478720
//        Connection: keep-alive
//        Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/601.4.4 (KHTML, like Gecko) Version/9.0.3 Safari/601.4.4
//Accept-Language: zh-cn
//Referer: http://www.tmsf.com/index.jsp
//Cache-Control: max-age=0

        URLConnection urlConnection = null;
        try{urlConnection = new URL(url).openConnection();
        urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/601.4.4 (KHTML, like Gecko) Version/9.0.3 Safari/601.4.4");
        urlConnection.setRequestProperty("Referer","http://www.tmsf.com/daily.htm");
            urlConnection.setRequestProperty("Connection","keep-alive");
            urlConnection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//        urlConnection.setRequestProperty("X-Forward-For","115.205.144.23");
//        urlConnection.setRequestProperty("Client-IP","115.205.144.23");
//        urlConnection.setRequestProperty("Cookie","Hm_lpvt_d7682ab43891c68a00de46e9ce5b76aa=1471043278; Hm_lvt_d7682ab43891c68a00de46e9ce5b76aa=1470990241; pgv_pvid=5516016926; CNZZDATA1253675216=712919559-1470787474-http%253A%252F%252Fwww.tmsf.com%252F%7C1471041400; IESESSION=alive; JSESSIONID=6B4D231FBB77C5DB6C420E969DE1079A.lb3; ROUTEID=.lb3; __jsluid=a3eb302a0320f8f11b836ac0c7b08941; __qc_wId=831; _qddab=3-9a62e6.irsb2ydr; _qddamta_800055708=3-0; Hm_lpvt_bbb8b9db5fbc7576fd868d7931c80ee1=1471043316; Hm_lvt_bbb8b9db5fbc7576fd868d7931c80ee1=1470790610,1471039722,1471041894; pgv_pvi=5287058432; pgv_si=s153255936");
        urlConnection.setRequestProperty("Cookie"," Hm_lvt_18f10d8299bec7ffdf82d66d10c93213=1474376866; Hm_lvt_4c7bd8dcbe98ab6faabff5eaf127a97a=1474376861; Hm_lvt_d7682ab43891c68a00de46e9ce5b76aa=1472726762,1472727985,1472739575,1472750093; pgv_pvid=6221838220; tencentSig=1837654016; CNZZDATA1253029781=2068559359-1472306765-http%253A%252F%252Fwww.tmsf.com%252F%7C1474979594; CNZZDATA1253675216=348674432-1471762978-http%253A%252F%252Fwww.tmsf.com%252F%7C1474982070; IESESSION=alive; JSESSIONID=E22B22AD291E731412E42949C47BD273.lb1; ROUTEID=.lb2; __jsluid=884bc1a619f3dc8efda068b15b80596a; __qc_wId=128; _qdda=3-1.2iwbfk; _qddab=3-lfnkke.itlj97dd; _qddamta_800055708=3-0; bdshare_firstime=1472396874233; istiped=; Hm_lpvt_bbb8b9db5fbc7576fd868d7931c80ee1=1474983826; Hm_lvt_bbb8b9db5fbc7576fd868d7931c80ee1=1473905766,1474376877,1474376881,1474683321; _qddaz=QD.stn65d.gbeq6t.itgkdixi; pgv_pvi=440478720; pgv_si=s9105485824");

        }catch (Exception e){
            log.error("failed to generate URL Connection:{}, exception:{}",url,e);
            FileOP.writeFile("log/daily_error_"+ LocalDate.now().toString(),String.format("failed to generate URL Connection:%s, exception:%s",url,e));
        }
        return urlConnection;
    }
}
