package net.dean.watcher.parser;

import net.dean.common.MappingSet;
import net.dean.object.DepartmentInfo;
import net.dean.object.HouseStateInfo;
import net.dean.setting.URLConfig;

import com.google.common.base.CharMatcher;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by dean on 16/6/29.
 * 爬取杭州所有的小区信息
 */
public class DepartmentParser {

    private final static Logger log = LoggerFactory.getLogger(DepartmentParser.class);

    private static final Table<String, String, String> districtTable = HashBasedTable.create();

    static {
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330103&keytype=1&propertystate=2", "330103", "下城");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330106&keytype=1&propertystate=2", "330106", "西湖");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330105&keytype=1&propertystate=2", "330105", "拱墅");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330104&keytype=1&propertystate=2", "330104", "江干");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330108&keytype=1&propertystate=2", "330108", "滨江");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330110&keytype=1&propertystate=2", "330110", "之江");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330102&keytype=1&propertystate=2", "330102", "上城");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330186&keytype=1&propertystate=2", "330186", "下沙");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330231&keytype=1&propertystate=2", "330231", "大江东");
        districtTable.put("/newhouse/property_searchall.htm?sid=330181&keytype=1&propertystate=2", "330181", "萧山");
        districtTable.put("/newhouse/property_searchall.htm?sid=330184&keytype=1&propertystate=2", "330184", "余杭");
        //这几个地方的网站结构不太一样,暂时不爬
//        distinctTable.put("/newhouse/property_searchall.htm?sid=330187&keytype=1&propertystate=2" ,"330187","富阳");
//        distinctTable.put("/newhouse/property_searchall.htm?sid=330188&keytype=1&propertystate=2" ,"330188","桐庐");
//        distinctTable.put("/newhouse/property_searchall.htm?sid=330189&keytype=1&propertystate=2" ,"330189","建德");
//        distinctTable.put("/newhouse/property_searchall.htm?sid=330191&keytype=1&propertystate=2" ,"330191","临安");
//        distinctTable.put("/newhouse/property_searchall.htm?sid=330190&keytype=1&propertystate=2" ,"330190","淳安");

    }

    public void run() throws IOException, ParserException {
        /*for(Table.Cell<String, String, String> cell : distinctTable.cellSet()){
            runDistinct(cell);
        }*/
//        runDepartment("西溪河滨之城","http://www.tmsf.com/newhouse/property_33_64897079_price.htm","西湖区","330108","29137",null);
//        runDepartment("黄龙金茂悦","http://www.tmsf.com/newhouse/property_33_57411595_price.htm","拱墅","330105","34528",null);
//        runDepartment("凯德&middot;湖墅观邸","http://www.tmsf.com/newhouse/property_33_93148643_price.htm","拱墅","330105","33566",null);
//        runDepartment("天峻公寓","http://www.tmsf.com/newhouse/property_330184_81564505_price.htm","余杭","330184","17378",null);
//        runDepartment("星空公寓","http://www.tmsf.com/newhouse/property_330184_254120984_price.htm","余杭","330184","24538",null);
//        runDepartment("万科&middot;新都会1958","http://www.tmsf.com/newhouse/property_33_232615031_price.htm","下城","330103","42436",null);
//        runDepartment("东方星城","http://www.tmsf.com/newhouse/property_33_238384144_price.htm","江干","330104","26876",null);

//        runDepartment("北大资源未名府","http://www.tmsf.com/newhouse/property_330184_265972817_price.htm","余杭","330184","17081",null);
//        runDepartment("绿城西子田园牧歌","http://www.tmsf.com/newhouse/property_33_2582_price.htm","拱墅","330105","18442",null);

//        runDepartment("云杉郡景中心","http://www.tmsf.com/newhouse/property_33_264942875_price.htm","滨江","330108","16677",null);

        runDepartment("九龙仓&middot;珑玺", "http://www.tmsf.com/newhouse/property_33_231599256_price.htm", "拱墅", "330105", "29552", null);

    }

    public List<DepartmentInfo> getDepartmentByDistrict(final Table<String, String, String> districtTable) throws IOException, ParserException {
        List<DepartmentInfo> departmentInfoList = new ArrayList<>();
        for (Table.Cell<String, String, String> cell : districtTable.cellSet()) {
            departmentInfoList.addAll(runDistrict(cell, true));
            try {
                Thread.sleep(1000);
            } catch (Exception e) {

            }
        }
        return departmentInfoList;
    }

    public List<DepartmentInfo> runDistrict(final Table.Cell<String, String, String> district, boolean jsutReturnDepartmentList) throws IOException, ParserException {

        List<DepartmentInfo> ReturnDepartmentInfoList = new ArrayList<>();
        //获取区的页数
        DepartmentParser departmentParser = new DepartmentParser();
        String url = URLConfig.URL_PREFIX + district.getRowKey();
        int page = departmentParser.parsePageInfo(url);
        log.info("get department page:{}", page);

        //获取每一个区的信息
        String currentDepartmentUrl;
        for (int i = 1; i <= page; i++) {
            currentDepartmentUrl = String.format("%s&page=%d", url, i);
            try {
                List<DepartmentInfo> departmentInfoList = departmentParser.parseDepartment(currentDepartmentUrl, district.getColumnKey(), district.getValue());
                if (jsutReturnDepartmentList) {
                    ReturnDepartmentInfoList.addAll(departmentInfoList);
                } else {
                    HouseParser houseParser = new HouseParser();
                    houseParser.run(departmentInfoList, true, null, null, null);
                }
            } catch (Exception e) {
                log.info("failed to parse {} page departments, url is:{}, exception:{}", i, currentDepartmentUrl, e);
            }

            log.info("finish to parse {} page departments, url is:{}", i, currentDepartmentUrl);
        }

        log.info("finish to parse distinct:{}", district);

        return ReturnDepartmentInfoList;
    }

    public void runDepartment(final String name, final String url, final String districtName, final String districtCode, final String avgPrice, HouseStateInfo state) {
        DepartmentInfo departmentInfo = new DepartmentInfo();
        departmentInfo.setName(name);
        departmentInfo.setDistrictCode(districtCode);
        departmentInfo.setDistrictName(districtName);
        departmentInfo.setUrl(url);
        departmentInfo.setAverPrice(avgPrice);

        List<DepartmentInfo> departmentInfoList = Arrays.asList(departmentInfo);

        long beginTime = Clock.systemUTC().millis();

        HouseParser houseParser = new HouseParser();
        houseParser.run(departmentInfoList, true, null, null, state);

        log.info("cost {}ms to parse department:{}", Clock.systemUTC().millis() - beginTime, departmentInfo);

    }

    /***
     * 解析小区的页数
     *
     * @param url
     * @return
     * @throws IOException
     * @throws ParserException
     */
    private int parsePageInfo(final String url) throws IOException, ParserException {
        Parser parser = new Parser(new URL(url).openConnection());

        NodeFilter nodeFilter = new HasAttributeFilter("class", "pagenumber");
        NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);
        for (Node node : nodeList.toNodeArray()) {
            if (!(node instanceof Div)) {
                continue;
            }
            for (Node innerNode : node.getChildren().elementAt(1).getChildren().toNodeArray()) {
                if (!(innerNode instanceof TextNode)) {
                    continue;
                }
                String pageStr = innerNode.toPlainTextString();
                if (!pageStr.contains("/")) {
                    continue;
                }
                pageStr = pageStr.substring(pageStr.indexOf("/") + 1);
                try {
                    return Integer.parseInt(pageStr);
                } catch (Exception e) {

                }
            }
        }
        return 0;
    }

    /***
     * 解析每一个小区的信息
     *
     * @param url
     * @param districtCode
     * @param districtName
     * @return
     * @throws IOException
     * @throws ParserException
     */
    private List<DepartmentInfo> parseDepartment(final String url, final String districtCode, final String districtName) throws IOException, ParserException {

        List<DepartmentInfo> departmentInfoList = Lists.newArrayList();

        Parser parser = new Parser(new URL(url).openConnection());
        NodeFilter nodeFilter = new HasAttributeFilter("class", "build_txt");
        NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);
        DepartmentInfo departmentInfo = null;
        DepartmentInfo.Builder builder = new DepartmentInfo.Builder();
        for (Node node : nodeList.toNodeArray()) {
            NodeList tmpNodeList = new NodeList();

            //小区名
            nodeFilter = new HasAttributeFilter("style", "padding-top: 10px;line-height:20px;");
            node.collectInto(tmpNodeList, nodeFilter);
            if (tmpNodeList.size() != 0) {
                String departmentName = CharMatcher.WHITESPACE.removeFrom(tmpNodeList.elementAt(0).toPlainTextString());
                builder.name(departmentName);
            }

            //小区均价
            tmpNodeList = new NodeList();

            nodeFilter = new HasAttributeFilter("class", "build_txt06");
            node.collectInto(tmpNodeList, nodeFilter);
            if (tmpNodeList.size() != 0) {
                String price = parseSpan(tmpNodeList.elementAt(0));
                builder.averPrice(price);
            }

            //小区URL
            tmpNodeList = new NodeList();

            nodeFilter = new HasAttributeFilter("class", "build_txt2");
            node.collectInto(tmpNodeList, nodeFilter);
            if (tmpNodeList.size() != 0) {
                String departmentUrl = ((LinkTag) tmpNodeList.elementAt(0).getChildren().elementAt(3)).getAttribute("href");
                builder.url(URLConfig.URL_PREFIX + departmentUrl);
            }

            departmentInfo = builder.build();
            departmentInfo.setDistrictCode(districtCode);
            departmentInfo.setDistrictName(districtName);
            departmentInfoList.add(departmentInfo);
            log.info("parse department :{} successfully", departmentInfo);
        }
        return departmentInfoList;
    }

    /***
     * 解析价格
     *
     * @param node
     * @return
     */
    private String parseSpan(Node node) {
        StringBuilder sb = new StringBuilder();
        NodeList priceNodeList = new NodeList();
        NodeFilter nodeFilter = new TagNameFilter("span");
        node.getChildren().elementAt(1).collectInto(priceNodeList, nodeFilter);
        for (Node spanNode : priceNodeList.toNodeArray()) {
            if (spanNode instanceof Span) {
                String attribute = ((Span) spanNode).getAttribute("class");
                sb.append(MappingSet.NUMBER_MAPPING.get(attribute));
            }
        }
        return CharMatcher.WHITESPACE.removeFrom(sb.toString());
    }
}
