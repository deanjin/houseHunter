package net.dean.watcher.parser;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CharMatcher;
import net.dean.common.ESOP;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dean.dal.DataOP;
import net.dean.object.DepartmentInfo;
import net.dean.object.HouseInfo;
import net.dean.object.SellCreditInfo;
import net.dean.setting.URLConfig;

/**
 * Created by dean on 16/7/9.
 * 用于解析预售证,获取楼盘的增量数据
 */
public class SellCreditParser {

    private final static Logger log = LoggerFactory.getLogger(SellCreditParser.class);

    private final static DataOP dataOP = new DataOP();

    private final static HouseParser houseParser = new HouseParser();

    public static void main(String[] args) throws InterruptedException, IOException, ParserException  {
        SellCreditParser sellCreditParser = new SellCreditParser();
        sellCreditParser.run("http://www.tmsf.com/index.jsp");
//        sellCreditParser.parseOneSellCredit();
    }

    public void run(String url) throws InterruptedException, IOException, ParserException {

//        "GET /index.jsp HTTP/1.1
//        Host: www.tmsf.com
//        Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//Cookie: Hm_lpvt_d7682ab43891c68a00de46e9ce5b76aa=1471043278; Hm_lvt_d7682ab43891c68a00de46e9ce5b76aa=1470990241; pgv_pvid=5516016926; CNZZDATA1253675216=712919559-1470787474-http%253A%252F%252Fwww.tmsf.com%252F%7C1471041400; IESESSION=alive; JSESSIONID=6B4D231FBB77C5DB6C420E969DE1079A.lb3; ROUTEID=.lb3; __jsluid=a3eb302a0320f8f11b836ac0c7b08941; __qc_wId=831; _qddab=3-9a62e6.irsb2ydr; _qddamta_800055708=3-0; Hm_lpvt_bbb8b9db5fbc7576fd868d7931c80ee1=1471043316; Hm_lvt_bbb8b9db5fbc7576fd868d7931c80ee1=1470790610,1471039722,1471041894; pgv_pvi=5287058432; pgv_si=s153255936
//User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/601.4.4 (KHTML, like Gecko) Version/9.0.3 Safari/601.4.4
//Accept-Language: zh-cn
//Accept-Encoding: gzip, deflate
//Connection: keep-alive"

//        Parser parser = new Parser(new URL(url).openConnection());
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_3) AppleWebKit/601.4.4");
//        urlConnection.setRequestProperty("X-Forward-For","115.205.144.23");
//        urlConnection.setRequestProperty("Client-IP","115.205.144.23");
        urlConnection.setRequestProperty("Cookie","Hm_lpvt_d7682ab43891c68a00de46e9ce5b76aa=1471043278; Hm_lvt_d7682ab43891c68a00de46e9ce5b76aa=1470990241; pgv_pvid=5516016926; CNZZDATA1253675216=712919559-1470787474-http%253A%252F%252Fwww.tmsf.com%252F%7C1471041400; IESESSION=alive; JSESSIONID=6B4D231FBB77C5DB6C420E969DE1079A.lb3; ROUTEID=.lb3; __jsluid=a3eb302a0320f8f11b836ac0c7b08941; __qc_wId=831; _qddab=3-9a62e6.irsb2ydr; _qddamta_800055708=3-0; Hm_lpvt_bbb8b9db5fbc7576fd868d7931c80ee1=1471043316; Hm_lvt_bbb8b9db5fbc7576fd868d7931c80ee1=1470790610,1471039722,1471041894; pgv_pvi=5287058432; pgv_si=s153255936");
        Parser parser = new Parser(urlConnection);
        NodeFilter nodeFilter = new HasAttributeFilter("class", "sale1");
        NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);

        if (nodeList.toNodeArray().length > 0) {
            Node[] sellCreditNodeArray = nodeList.elementAt(0).getChildren().toNodeArray();
            for (int i = 2; i < sellCreditNodeArray.length; i++) {
                if (sellCreditNodeArray[i] instanceof TableRow) {
                    SellCreditInfo sellCreditInfo = parseSellParser(sellCreditNodeArray[i]);
                    log.info("get sell credit info:{}", sellCreditInfo);
                    //该预售证是否已经爬过
                    HouseInfo houseInfo = dataOP.getHouseInfoByDepartmentNameAndSellCredit(sellCreditInfo);
                    if(houseInfo != null){
                        log.info("already parsing sell credit:{}",sellCreditInfo);
                        break;
                    }
                    dataOP.insertSellCreditInfo(sellCreditInfo);
                    if(i==2) continue;
                    parseHouseInfo(sellCreditInfo);
                }
            }
        }
    }

    private void parseOneSellCredit(){
        SellCreditInfo sellCreditInfo = new SellCreditInfo();
        sellCreditInfo.setUrl("/newhouse/property_33_232615031_price.htm");
        sellCreditInfo.setName("万科&middot;新都会1958");
        sellCreditInfo.setSellCredit("2016000104");

        try {
            parseHouseInfo(sellCreditInfo);
        }catch(Exception e){

        }
    }

    private SellCreditInfo parseSellParser(Node tableRow) {
        SellCreditInfo sellCreditInfo = new SellCreditInfo();
        LinkTag linkTag;

        sellCreditInfo.setName(CharMatcher.WHITESPACE.removeFrom(tableRow.getChildren().elementAt(1).toPlainTextString()));
        linkTag = (LinkTag) tableRow.getChildren().elementAt(3).getChildren().elementAt(1);
        sellCreditInfo.setSellCredit(linkTag.toPlainTextString());
        sellCreditInfo.setUrl(linkTag.getAttribute("href"));
        sellCreditInfo.setCreditDate(CharMatcher.WHITESPACE.removeFrom(tableRow.getChildren().elementAt(5).toPlainTextString()));

        return sellCreditInfo;
    }

    private void parseHouseInfo(SellCreditInfo sellCreditInfo) throws InterruptedException, IOException, ParserException{

        //该预售证是否已经爬过
//        HouseInfo houseInfo = dataOP.getHouseInfoByDepartmentNameAndSellCredit(sellCreditInfo);
//        if(houseInfo != null){
//            log.info("already parsing sell credit:{}",sellCreditInfo);
//            return;
//        }

        HouseInfo houseInfo = dataOP.getHouseInfoByDepartmentName(sellCreditInfo.getName());
        DepartmentInfo departmentInfo = new DepartmentInfo();

        Map<String, String> sellCreditMap = new HashMap<>();

        //新楼盘
        if (houseInfo == null) {
            departmentInfo.setUrl(URLConfig.URL_PREFIX + sellCreditInfo.getUrl());
            log.info("it's new sell credit:{}",sellCreditInfo);
            ESOP.writeToES("log/daily_sell_credit_es", JSONObject.toJSONString(sellCreditInfo));
            return;
        }

        //老楼盘新预售
        departmentInfo.setName(houseInfo.getDepartmentName());
        departmentInfo.setDistrictName(houseInfo.getDistrictName());
        departmentInfo.setDistrictCode(houseInfo.getDistrictCode());
        departmentInfo.setUrl(URLConfig.URL_PREFIX + sellCreditInfo.getUrl());
        String sellCreditId = null;
        //爬取所有的预售证
        sellCreditMap = houseParser.parseAllSellCredit(departmentInfo);
        for (Map.Entry<String, String> entry : sellCreditMap.entrySet()) {
            if (StringUtils.equalsIgnoreCase(entry.getValue(), sellCreditInfo.getSellCredit())) {
                sellCreditId = entry.getKey();
            }
        }
        if (StringUtils.isNotBlank(sellCreditId)) {
            List<HouseInfo> houseInfoList = new ArrayList<>();
            houseParser.parseSellCredit(houseInfoList, departmentInfo, sellCreditId, sellCreditInfo.getSellCredit(),true,null,null,null);
        }
    }
}
