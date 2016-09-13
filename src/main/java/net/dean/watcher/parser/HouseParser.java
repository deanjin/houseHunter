package net.dean.watcher.parser;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import net.dean.common.CommonHttpURLConnection;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dean.common.MappingSet;
import net.dean.dal.DataOP;
import net.dean.object.DepartmentInfo;
import net.dean.object.HouseInfo;
import net.dean.object.HouseStateInfo;
import net.dean.setting.URLConfig;

/**
 * Created by dean on 16/6/27.
 */
public class HouseParser {

    private static final Logger log = LoggerFactory.getLogger(HouseParser.class);

    private static final DataOP dataOP = new DataOP();

    public List<HouseInfo> run(List<DepartmentInfo> departmentInfoList,
                               boolean writeDB,
                               Map<String, HouseInfo> houseInfoMap,
                               List<HouseInfo> currentSellHouseInfoList,
                               HouseStateInfo state) {

        List<HouseInfo> houseInfoList = Lists.newArrayList();

        for (DepartmentInfo departmentInfo : departmentInfoList) {
            try {
                log.info("begin to parse house info for department:{}", departmentInfo);

                //爬取所有的预售证
                Map<String, String> sellCreditMap = parseAllSellCredit(departmentInfo);
                ExecutorService executorService = Executors.newFixedThreadPool(sellCreditMap.size());
                final CountDownLatch countDownLatch = new CountDownLatch(sellCreditMap.size());
                for (Map.Entry<String, String> sellCredit : sellCreditMap.entrySet()) {

                    executorService.execute(() -> {
                        try {
                            parseSellCredit(
                                    houseInfoList,
                                    departmentInfo,
                                    sellCredit.getKey(),
                                    sellCredit.getValue(),
                                    writeDB,
                                    houseInfoMap,
                                    currentSellHouseInfoList,
                                    state);
                        } catch (Exception e) {
                            log.error("run parseSellCredit catch exception:", e);
                        } finally {
                            countDownLatch.countDown();
                        }
                    });
                }
                countDownLatch.await();
            } catch (Exception e) {
                log.error("parse department:[{}] catch exception:", departmentInfo, e);
            }
            log.info("successfully to parse department info:{}", departmentInfo.toString());
        }
        return houseInfoList;
    }

    /***
     * 爬取预售证
     *
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception
     */
    public Map<String, String> parseAllSellCredit(final DepartmentInfo departmentInfo) throws InterruptedException, IOException, ParserException {
        Parser parser = new Parser(CommonHttpURLConnection.getURLConnection(departmentInfo.getUrl()));

        //解析预售证
        NodeFilter nodeFilter = new HasAttributeFilter("id", "presell_dd");
        NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);
        if (nodeList.size() == 0) {
            return Maps.newHashMap();
        }

        NodeList tmpNodeList = new NodeList();
        nodeFilter = new HasAttributeFilter("class", "lptypebarin");
        nodeList.elementAt(0).collectInto(tmpNodeList, nodeFilter);

        Map<String, String> sellCreditMap = parseDetailInfo(tmpNodeList);

        log.info("get sell credits [{}] for department:[{}]", sellCreditMap.toString(), departmentInfo.toString());

        departmentInfo.setSellCreditMap(sellCreditMap);
        return sellCreditMap;

    }

    /***
     * 爬取楼幢信息
     *
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception
     */
    public Map<String, String> parseBuilding(String url, DepartmentInfo departmentInfo)
            throws InterruptedException, IOException, ParserException {

        Map<String, String> buildingMap = Maps.newHashMap();

        Parser parser = new Parser(CommonHttpURLConnection.getURLConnection(url));

        //解析预售证
        NodeFilter nodeFilter = new HasAttributeFilter("id", "building_dd");
        NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);
        if (nodeList.size() == 0) {
            return Maps.newHashMap();
        }

        NodeList tmpNodeList = new NodeList();
        nodeFilter = new HasAttributeFilter("class", "lptypebarin");
        nodeList.elementAt(0).collectInto(tmpNodeList, nodeFilter);

        buildingMap.putAll(parseDetailInfo(tmpNodeList));

        log.info("get building info [{}] for department:[{}]", buildingMap.toString(), departmentInfo.toString());

        departmentInfo.getBuildingMap().putAll(buildingMap);
        return buildingMap;

    }

    private Map<String, String> parseDetailInfo(NodeList nodeList) {
        Map<String, String> InfoMap = Maps.newHashMap();
        if (nodeList.size() == 0) {
            return InfoMap;
        }
        for (Node pageNode : nodeList.elementAt(0).getChildren().toNodeArray()) {
            try {
                if (pageNode instanceof LinkTag) {
                    String rawId = ((LinkTag) pageNode).getAttribute("id");
                    if (StringUtils.isBlank(rawId)) {
                        continue;
                    }
                    if (rawId.contains("all")) {
                        continue;
                    }
                    String id = rawId.substring(rawId.indexOf("_") + 1);

                    InfoMap.put(id, pageNode.toPlainTextString());
                }
            } catch (Exception e) {
                log.error("parse parseDetailInfo catch Exception:", e);
            }
        }
        return InfoMap;
    }


    /**
     * 爬取当前楼幢的页数
     *
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws Exception
     */
    public int parsePageInfo(String url, DepartmentInfo departmentInfo) throws ParserException, IOException {

        Parser parser = new Parser(CommonHttpURLConnection.getURLConnection(url));

        int page = 0;
        //解析页数
        NodeFilter nodeFilter = new HasAttributeFilter("class", "spagenext");
        NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);
        if (nodeList.size() == 0) {
            return page;
        }

        for (Node pageNode : nodeList.elementAt(0).getChildren().toNodeArray()) {
            if (pageNode instanceof Span) {
                try {
                    String tmp = pageNode.toPlainTextString();
                    page = Integer.parseInt(tmp.substring(tmp.indexOf("/") + 1, tmp.indexOf("总数") - 1).trim());
                    break;
                } catch (Exception e) {
                }
            }
        }

        log.info("get total page [{}] for department:[{}]", page, departmentInfo.toString());

        return page;
    }

    public void parse(final DepartmentInfo departmentInfo,
                      final List<HouseInfo> houseInfoList,
                      final String url, String sellCredit,
                      boolean writeDB,
                      Map<String, HouseInfo> unSellHouseInfoMap,
                      List<HouseInfo> currentSellHouseInfoList)
            throws InterruptedException, IOException, ParserException {
        Parser parser = new Parser(CommonHttpURLConnection.getURLConnection(url));

        NodeFilter nodeFilter = new HasAttributeFilter("style", "background-color:#f6f6f6");
        NodeList rootNodeList = parser.extractAllNodesThatMatch(nodeFilter);
        if (rootNodeList.size() == 0) {
            return;
        }
        rootNodeList = rootNodeList.elementAt(0).getParent().getChildren();

        String tmp;
        HouseInfo houseInfo;
        for (Node node : rootNodeList.toNodeArray()) {

            if (!(node instanceof TableRow)) {
                continue;
            }

            HouseInfo.Builder builder = new HouseInfo.Builder();
            NodeList nodeList = node.getChildren();


            //小区名
            builder.departmentName(departmentInfo.getName());

            //所属区
            builder.setDistrictName(departmentInfo.getDistrictName());
            builder.setDistrictCode(departmentInfo.getDistrictCode());

            //预售证
            builder.sellCredit(sellCredit);

            //解析楼幢数
            builder.buildingNumber(parseLinkTag(nodeList.elementAt(1).getChildren()));

            //解析房号
            builder.doorNumber(parseDiv(nodeList.elementAt(3).getChildren()));

            //解析建筑面积
            builder.originArea(parseSpan(nodeList.elementAt(5).getChildren()));

            //解析套内实际面积
            builder.finalArea(parseSpan(nodeList.elementAt(7).getChildren()));

            //解析得房率
            builder.areaPercent(parseSpan(nodeList.elementAt(9).getChildren()));

            //解析申请毛坯单价
            String originPriceStr = parseSpan(nodeList.elementAt(11).getChildren());
            if (StringUtils.equalsIgnoreCase("-", originPriceStr)) {
                continue;
            } else {
                builder.originPrice(originPriceStr);
            }

            //解析装修价
            builder.decorationPrice(parseSpan(nodeList.elementAt(13).getChildren()));

            //解析总价
            builder.totalPrice(parseSpan(nodeList.elementAt(15).getChildren()));

            //解析状态
            builder.status(parseDiv(nodeList.elementAt(17).getChildren()));

            //url
            builder.setUrl(url);

            houseInfo = builder.build();
            updateHouseInfo(houseInfo);
            houseInfoList.add(houseInfo);

            if (unSellHouseInfoMap != null && !unSellHouseInfoMap.isEmpty()) {
                HouseInfo matchHouseInfo = unSellHouseInfoMap.get(houseInfo.getHashCode());
                if (null == matchHouseInfo) {
                    continue;
                } else {
                    houseInfo.setId(matchHouseInfo.getId());
                    currentSellHouseInfoList.add(matchHouseInfo);
                }
            }

            if (writeDB) {
                //插入到数据库
                dataOP.insertHouseInfo(houseInfo);
            }

            log.info("get house info [{}] for department:[{}]", houseInfo.toString(), departmentInfo.toString());

        }

        Thread.sleep(500);

    }

    /**
     * 解析楼幢数
     *
     * @param nodeList
     * @return
     */
    private String parseLinkTag(NodeList nodeList) {
        for (Node node : nodeList.toNodeArray()) {
            if (node instanceof LinkTag) {
                return node.toPlainTextString();
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * 解析房号、状态
     *
     * @param nodeList
     * @return
     */
    private String parseDiv(NodeList nodeList) {
        for (Node node : nodeList.toNodeArray()) {
            if (node instanceof Div) {
                return node.toPlainTextString();
            }
        }
        return StringUtils.EMPTY;
    }

    /***
     * 解析其他
     *
     * @param nodeList
     * @return
     */
    private String parseSpan(NodeList nodeList) {
        StringBuilder sb = new StringBuilder();
        for (Node node : nodeList.toNodeArray()) {
            if (node instanceof Div) {
                if (StringUtils.equalsIgnoreCase("-", node.toPlainTextString())) {
                    return "0";
                }
                NodeList spanNodeList = node.getChildren();
                for (Node spanNode : spanNodeList.toNodeArray()) {
                    if (spanNode instanceof Span) {
                        String attribute = ((Span) spanNode).getAttribute("class");
                        sb.append(MappingSet.NUMBER_MAPPING.get(attribute));
                    }
                }
            }
        }
        return sb.toString();
    }

    private void updateHouseInfo(HouseInfo houseInfo) {
        if (houseInfo == null) {
            return;
        }

        //房屋状态
        if (StringUtils.equalsIgnoreCase(houseInfo.getStatus(), "团购报名")) {
            houseInfo.setStatus("可售");
        } else if (StringUtils.equalsIgnoreCase(houseInfo.getStatus(), "不可售")) {
            houseInfo.setStatus("已售");
        }

        //楼层
        String doorNumberStr = houseInfo.getDoorNumber();
        if (StringUtils.isNotBlank(doorNumberStr)) {
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(doorNumberStr);
            try {
                int doorNumber = Integer.parseInt(m.replaceAll("").trim());
                if (doorNumber >= 100) {
                    houseInfo.setFloor(doorNumber / 100);
                }
            } catch (Exception e) {

            }
        }

        //hashCode
        houseInfo.setHashCode(
                Hashing.md5().hashString(
                        houseInfo.getSellCredit()
                                + houseInfo.getDepartmentName()
                                + houseInfo.getBuildingNumber()
                                + houseInfo.getDoorNumber()
                                + houseInfo.getOriginArea(), Charsets.UTF_8).toString());
    }

    public void parseSellCredit(List<HouseInfo> houseInfoList,
                                DepartmentInfo departmentInfo,
                                String sellCreditKey,
                                String sellCreditValue,
                                boolean writeDB,
                                Map<String, HouseInfo> houseInfoMap,
                                List<HouseInfo> currentSellHouseInfoList,
                                HouseStateInfo state)
            throws InterruptedException, IOException, ParserException {
        String sellCreditUrl = String.format(URLConfig.HOUSE_ENTRANCE_URL, departmentInfo.getUrl(), sellCreditKey);
        //爬取该预售证下面的所有楼幢
        Map<String, String> buildingMap = parseBuilding(sellCreditUrl, departmentInfo);

//        ExecutorService executorService = Executors.newFixedThreadPool(buildingMap.size());
//        final CountDownLatch countDownLatch = new CountDownLatch(buildingMap.size());

        for (Map.Entry<String, String> building : buildingMap.entrySet()) {
//            executorService.execute(
//                    () -> {
                        try {
                            String url = String.format(URLConfig.HOUSE_ENTRANCE_URL + "&buildingid=%s", departmentInfo.getUrl(), sellCreditKey, building.getKey());
                            if (state != null) {
                                url += state.getName();
                            }
                            int page = parsePageInfo(url, departmentInfo);

                            for (int i = 1; i <= page; ++i) {
                                parse(departmentInfo, houseInfoList, url + "&page=" + i, sellCreditValue, writeDB, houseInfoMap, currentSellHouseInfoList);
                            }

//                            countDownLatch.countDown();
                        } catch (Exception e) {
                            log.error("run parseSellCredit catch exception:", e);
                        }
//                    }
//            );
        }
//        countDownLatch.await();
    }
}

