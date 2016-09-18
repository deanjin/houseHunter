package net.dean.watcher.parser;

import net.dean.common.CommonHttpURLConnection;
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
//        runDepartment("西溪河滨之城","http://www.tmsf.com/newhouse/property_33_64897079_price.htm","西湖区","330106","34000",null);
//        runDepartment("黄龙金茂悦","http://www.tmsf.com/newhouse/property_33_57411595_price.htm","拱墅","330105","34528",null);
//        runDepartment("凯德&middot;湖墅观邸","http://www.tmsf.com/newhouse/property_33_93148643_price.htm","拱墅","330105","33566",null);
//        runDepartment("天峻公寓","http://www.tmsf.com/newhouse/property_330184_81564505_price.htm","余杭","330184","17378",null);
//        runDepartment("星空公寓","http://www.tmsf.com/newhouse/property_330184_254120984_price.htm","余杭","330184","24538",null);
//        runDepartment("万科&middot;新都会1958","http://www.tmsf.com/newhouse/property_33_232615031_price.htm","下城","330103","42436",null);
//        runDepartment("东方星城","http://www.tmsf.com/newhouse/property_33_238384144_price.htm","江干","330104","26876",null);
//        runDepartment("绿城九龙仓&middot;柳岸晓风", "http://www.tmsf.com/newhouse/property_33_239218542_price.htm", "滨江", "330108", "41484", null);
//
//        runDepartment("北大资源未名府","http://www.tmsf.com/newhouse/property_330184_265972817_price.htm","余杭","330184","17081",null);
//        runDepartment("绿城西子田园牧歌","http://www.tmsf.com/newhouse/property_33_2582_price.htm","拱墅","330105","18442",null);
//
//        runDepartment("云杉郡景中心","http://www.tmsf.com/newhouse/property_33_264942875_price.htm","滨江","330108","16677",null);

//        runDepartment("九龙仓&middot;珑玺", "http://www.tmsf.com/newhouse/property_33_231599256_price.htm", "拱墅", "330105", "29552", null);
//        runDepartment("水色宜居", "http://www.tmsf.com/newhouse/property_33_163840737_price.htm", "拱墅", "330105", "33672", null);
//        runDepartment("孔雀蓝轩", "http://www.tmsf.com/newhouse/property_33_177105285_price.htm", "拱墅", "330105", "26355", null);
//        runDepartment("学院华庭", "http://www.tmsf.com/newhouse/property_33_101409102_price.htm", "西湖", "330106", "50365", null);
//        runDepartment("白马湖和院", "http://www.tmsf.com/newhouse/property_33_115301085_price.htm", "滨江", "330108", "17244", null);
//        runDepartment("滨江&middot;铂金海岸", "http://www.tmsf.com/newhouse/property_33_272625341_price.htm", "江干", "330104", "25407", null);
//        runDepartment("西溪银泰商业中心", "http://www.tmsf.com/newhouse/property_33_122036672_price.htm", "西湖", "330106", "0", null);
//        runDepartment("湖漫雅筑", "http://www.tmsf.com/newhouse/property_33_25749011_price.htm", "滨江", "330108", "0", null);
//        runDepartment("御溪花苑", "http://www.tmsf.com/newhouse/property_33_49559595_price.htm", "西湖", "330106", "0", null);
//        runDepartment("玉泉二期&middot;香樟洋房", "http://www.tmsf.com/newhouse/property_33_302207316_price.htm", "西湖", "330106", "0", null);

//        runDepartment("金都艺墅","http://www.tmsf.com/newhouse/property_33_2554_price.htm","之江","330110","21496",null);
//        runDepartment("运河金麟府","http://www.tmsf.com/newhouse/property_33_240333664_price.htm","拱墅","330105","0",null);

//        runDepartment("卓蓝华庭", "http://www.tmsf.com/newhouse/property_33_164427716_price.htm", "江干", "330104", "12491", null);
//        runDepartment("运河宸园", "http://www.tmsf.com/newhouse/property_33_49436919_price.htm", "拱墅", "330105", "27433", null);

//        runDepartment("广厦天都城", "http://www.tmsf.com/newhouse/property_330184_20262405_price.htm", "余杭", "330184", "12491", null);

//          runDepartment("云荷廷", "http://www.tmsf.com/newhouse/property_33_61780345_price.htm", "之江", "330110", "28374", null);

//        runDepartment("阳光郡公寓", "http://www.tmsf.com/newhouse/property_33_8911_price.htm", "拱墅", "330105", "19877", null);
//        runDepartment("紫蝶苑", "http://www.tmsf.com/newhouse/property_33_59170394_price.htm", "西湖", "330106", "23189", null);
//        runDepartment("万科郡西澜山", "http://www.tmsf.com/newhouse/property_330184_186063736_price.htm", "余杭", "330184", "18219", null);

//        runDepartment("西溪蓝海", "http://www.tmsf.com/newhouse/property_330184_177675733_price.htm", "余杭", "330184", "14792", null);
//        runDepartment("雍荣华庭", "http://www.tmsf.com/newhouse/property_33_226845036_price.htm", "拱墅", "330105", "27560", null);

//        runDepartment("都会翡翠花苑", "http://www.tmsf.com/newhouse/property_33_235799135_price.htm", "江干", "330104", "33441", null);
//        runDepartment("运河金麟府", "http://www.tmsf.com/newhouse/property_33_240333664_price.htm", "拱墅", "330105", "37695", null);
//        runDepartment("映月台公寓", "http://www.tmsf.com/newhouse/property_330184_290596929_price.htm", "余杭", "330184", "22902", null);
//        runDepartment("溪岸悦府", "http://www.tmsf.com/newhouse/property_330184_166388413_price.htm", "余杭", "330184", "25625", null);

//        runDepartment("萍实公寓", "http://pay.tmsf.com/newhouse/property_33_78158625_price.htm", "拱墅", "330105", "30976", null);
//        runDepartment("滨江&middot;锦绣之城", "http://www.tmsf.com/newhouse/property_33_290876212_price.htm", "拱墅", "330105", "44585", null);
//        runDepartment("阳光城&middot;文澜府", "http://www.tmsf.com/newhouse/property_33_238789032_price.htm", "拱墅", "330105", "32057", null);
//        runDepartment("海域晶华公寓", "http://www.tmsf.com/newhouse/property_33_92241652_price.htm", "江干", "330104", "47232",null);
//        runDepartment("玉观邸", "http://www.tmsf.com/newhouse/property_33_16444178_price.htm", "下沙", "330186", "15948",null);
//        runDepartment("百翘星辉名阁", "http://www.tmsf.com/newhouse/property_33_140153398_price.htm", "下沙", "330186", "12534",null);
//        runDepartment("碧月华庭", "http://www.tmsf.com/newhouse/property_33_231571583_price.htm", "拱墅", "330105", "36524",null);

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
        Parser parser = new Parser(CommonHttpURLConnection.getURLConnection(url));

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

        Parser parser = new Parser(CommonHttpURLConnection.getURLConnection(url));


        NodeFilter nodeFilter1 = new HasAttributeFilter("class", "build_des dingwei");
        NodeList nodeList1 = parser.extractAllNodesThatMatch(nodeFilter1);

        for (Node node1 : nodeList1.toNodeArray()) {
            NodeList nodeList2 = new NodeList();
            NodeFilter nodeFilter = new HasAttributeFilter("class", "build_txt line26");
            node1.collectInto(nodeList2, nodeFilter);
            DepartmentInfo departmentInfo = null;
            DepartmentInfo.Builder builder = new DepartmentInfo.Builder();

            NodeList tmpNodeList = new NodeList();

            //小区名
            nodeFilter = new HasAttributeFilter("class", "build_word01");
            nodeList2.elementAt(0).collectInto(tmpNodeList, nodeFilter);
            if (tmpNodeList.size() != 0) {
                String departmentName = CharMatcher.WHITESPACE.removeFrom(tmpNodeList.elementAt(0).getChildren().elementAt(1).toPlainTextString());
                builder.name(departmentName);
            }

            //小区URL
            tmpNodeList = new NodeList();

            nodeFilter = new HasAttributeFilter("class", "build_txt2");
            nodeList2.elementAt(0).collectInto(tmpNodeList, nodeFilter);
            if (tmpNodeList.size() != 0) {
                String departmentUrl = ((LinkTag) tmpNodeList.elementAt(0).getChildren().elementAt(3)).getAttribute("href");
                builder.url(URLConfig.URL_PREFIX + departmentUrl);
            }

            //小区均价
            tmpNodeList = new NodeList();

            nodeFilter = new HasAttributeFilter("class", "word1");
            node1.collectInto(tmpNodeList, nodeFilter);
            if (tmpNodeList.size() != 0) {
                String price = parseSpan(tmpNodeList.elementAt(0));
                builder.averPrice(price);
            }

            departmentInfo = builder.build();
            departmentInfo.setDistrictCode(districtCode);
            departmentInfo.setDistrictName(districtName);


            log.info("parse department :{} successfully", departmentInfo);

            departmentInfoList.add(departmentInfo);
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
        node.collectInto(priceNodeList, nodeFilter);
        for (Node spanNode : priceNodeList.toNodeArray()) {
            if (spanNode instanceof Span) {
                String attribute = ((Span) spanNode).getAttribute("class");
                sb.append(MappingSet.NUMBER_MAPPING.get(attribute));
            }
        }
        return CharMatcher.WHITESPACE.removeFrom(sb.toString());
    }
}
