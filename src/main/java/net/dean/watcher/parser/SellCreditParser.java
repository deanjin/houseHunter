package net.dean.watcher.parser;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CharMatcher;
import net.dean.common.CommonHttpURLConnection;
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

    }

    /***
     * 爬取透明网最近的预售证信息
     * @param url
     * @throws InterruptedException
     * @throws IOException
     * @throws ParserException
     */
    public void run(String url) throws InterruptedException, IOException, ParserException {

        URLConnection urlConnection = CommonHttpURLConnection.getURLConnection(url);
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

    /***
     * 爬取某一个预售证楼盘信息
     */
    public void parseOneSellCredit(SellCreditInfo sellCreditInfo){
        try {
            parseHouseInfo(sellCreditInfo);
        }catch(Exception e){

        }
    }

    public SellCreditInfo parseSellParser(Node tableRow) {
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
