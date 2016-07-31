package net.dean.watcher.parser;

import net.dean.common.ESOP;
import net.dean.common.FileOP;
import net.dean.common.MappingSet;
import net.dean.dal.DataOP;
import net.dean.object.*;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by dean on 16/6/24.
 * 解析每天的成交情况
 */
public class DailyDealParser {

    private final static Logger log = LoggerFactory.getLogger(DailyDealParser.class);

    private final DataOP dataOP = new DataOP();
    private final HouseParser houseParser = new HouseParser();
    private final static Set<String> needParseDepartmentSet = ImmutableSet.of(
            "凯德&middot;湖墅观邸",
            "黄龙金茂悦",
            "西溪河滨之城",
            "天峻公寓",
            "星空公寓",
            "万科&middot;新都会1958",
            "东方星城",
//        "北大资源未名府",
//        "绿城西子田园牧歌",
            "云杉郡景中心",
            "国风美域公寓",
            "九龙仓&middot;珑玺");

    public static void main(String[] args) {
        DailyDealParser dailyDealParser = new DailyDealParser();
        try {
            dailyDealParser.parseDailyBriefInfo();
        } catch (Exception e) {

        }
    }

    /***
     * 解析每天的成交情况
     *
     * @param url
     * @return
     * @throws IOException
     * @throws ParserException
     */
    public List<DailyDealInfo> run(final String url) throws IOException, ParserException {

        List<DailyDealInfo> dailyDealInfoList = Lists.newArrayList();
        Parser parser = new Parser(new URL(url).openConnection());
        NodeFilter nodeFilter = new HasAttributeFilter("class", "datacenternow");
        NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);
        if (nodeList.elementAt(0).getChildren().size() < 2) {
            return Lists.newArrayList();
        }

        //到1970/01/01 00:00:00的小时数
        int parseHour = (int) (Clock.systemUTC().millis() / (1000 * 3600));

        //到1970/01/01 00:00:00的天数
        int parseDay = (int) parseHour / 24;

        List<DailyDealInfo> curHourDailyDealInfoList = dataOP.getDailyDealInfoByHour(parseDay);
        if (!curHourDailyDealInfoList.isEmpty() && curHourDailyDealInfoList.get(0).getParseHour() == parseHour) {
            log.info("in hour:{} of day:{}, already parse", parseHour, parseDay);
            return Collections.EMPTY_LIST;
        }

        Node node = nodeList.elementAt(0).getChildren().elementAt(3);

        nodeFilter = new TagNameFilter("tr");
        nodeList = new NodeList();
        node.collectInto(nodeList, nodeFilter);
        NodeList dealNodeList;
        for (int i = 1; i < nodeList.toNodeArray().length - 1; i++) {

            Node tmpNode = nodeList.elementAt(i);
            if (tmpNode instanceof TableRow && ("#fff9e5").equalsIgnoreCase(((TableRow) tmpNode).getAttribute("bgcolor"))) {
                continue;
            }
            nodeFilter = new TagNameFilter("td");
            dealNodeList = new NodeList();
            tmpNode.collectInto(dealNodeList, nodeFilter);

            int length = dealNodeList.size();

            DailyDealInfo.Builder builder = new DailyDealInfo.Builder();
            String department_Name = CharMatcher.WHITESPACE.removeFrom(dealNodeList.elementAt(0).toPlainTextString());
            if (StringUtils.equalsIgnoreCase("楼盘名称", department_Name)) {
                continue;
            }
            if (length > 0) {
                builder.name(department_Name);
            }
            if (length > 1) {
                builder.district(CharMatcher.WHITESPACE.removeFrom(dealNodeList.elementAt(1).toPlainTextString()));
            }
            if (length > 2) {
                builder.dealNumber(Integer.parseInt(parseSpan(dealNodeList.elementAt(2))));
            }
            if (length > 3) {
                builder.bookingNumber(Integer.parseInt(parseSpan(dealNodeList.elementAt(3))));
            }
            if (length > 4) {
                builder.dealArea(Double.parseDouble(parseSpan(dealNodeList.elementAt(4))));
            }
            if (length > 5) {
                builder.dealAvgPrice(Double.parseDouble(parseSpan(dealNodeList.elementAt(5))));
            }

            builder.parseHour(parseHour);

            builder.parseDay(parseDay);

            DailyDealInfo dailyDealInfo = builder.build();


            String name = dailyDealInfo.getName().replace("·", "&middot;");
            //获取区域信息
            Optional<String> opt = getDistrictByName(name);
            if (opt.isPresent()) {
                dailyDealInfo.setDistrictPart(dailyDealInfo.getDistrict());
                dailyDealInfo.setDistrict(opt.get());
            }

            dailyDealInfoList.add(dailyDealInfo);

            log.info("parse daily deal info:{}", dailyDealInfo.toString());
        }

        log.info("in hour:{} of day:{}, get total {} daily deal info count", parseHour, parseDay, dailyDealInfoList.size());

        List<DailyDealInfo> tmpDailyDealInfoList = new ArrayList<>();

        dailyDealInfoList.forEach(e -> {
            try {
                tmpDailyDealInfoList.add((DailyDealInfo) e.clone());
            } catch (Exception e1) {
            }
        });

        updateDealInfo(parseHour, parseDay, tmpDailyDealInfoList,curHourDailyDealInfoList);

        return dailyDealInfoList;
    }

    /**
     * 每天交易情况插入到数据库
     *
     * @param dailyDealInfoList
     */
    private void insertDailyDealInfoToDB(List<DailyDealInfo> dailyDealInfoList) {
        LocalDateTime localDateTime = LocalDateTime.now();
        dailyDealInfoList.forEach(e -> {
            dataOP.insertDailyDealInfo(e);
            //每天23点讲当天的成交情况写入到日志文件,用于集成elk
            if (localDateTime.getHour() == MappingSet.RECORD_HOUR) {
                ESOP.writeToES("log/daily_deal_info_brief_es", JSONObject.toJSONString(e));
            }
        });
    }

    /**
     * 更新这个小时楼盘销售情况
     *
     * @param parseHour
     * @param parseDay
     * @param dailyDealInfoList
     * @param curHourDailyDealInfoList
     * @return
     */
    public boolean updateDealInfo(int parseHour, int parseDay, List<DailyDealInfo> dailyDealInfoList, List<DailyDealInfo> curHourDailyDealInfoList) {

            //更新数据库
            insertDailyDealInfoToDB(dailyDealInfoList);

            //跟前一个小时的成交记录做diff得到这个小时的成交记录
            dailyDealInfoList
                    .stream()
                    .filter(e -> curHourDailyDealInfoList.contains(e))
                    .forEach(w -> {
                        DailyDealInfo dailyDealInfo = curHourDailyDealInfoList.get(curHourDailyDealInfoList.indexOf(w));
                        double previousDealArea = w.getDealArea();
                        double previousDealAvgPrice = w.getDealAvgPrice();
                        w.setDealNumber(w.getDealNumber() - dailyDealInfo.getDealNumber());
                        w.setBookingNumber(w.getBookingNumber() - dailyDealInfo.getBookingNumber());
                        w.setDealArea(w.getDealArea() - dailyDealInfo.getDealArea());
                        w.setDealAvgPrice((previousDealAvgPrice * previousDealArea
                                - dailyDealInfo.getDealAvgPrice() * dailyDealInfo.getDealArea())
                                / w.getDealArea());
                    });


        try {
            FileOP.writeFile("log/dailyDealInfo", String.valueOf(new Date()), dailyDealInfoList);
        } catch (Exception e) {
            log.error("failed to write daily deal info");
        }

        for (DailyDealInfo dailyDealInfo : dailyDealInfoList) {
            String name = dailyDealInfo.getName();
            name = name.replace("·", "&middot;");
            dailyDealInfo.setName(name);
            if (needParseDepartmentSet.contains(dailyDealInfo.getName())) {
                findSellHouse(dailyDealInfo);
            }
        }

        log.info("finish to parse in hour:{} of day:{}", parseHour, parseDay);
        return true;
    }

    /***
     * 根据数据库的可售房子与透明网的已售房子做快照对比,找到这个小时内销售的房子
     *
     * @param dailyDealInfo
     */
    private void findSellHouse(DailyDealInfo dailyDealInfo) {

        if (dailyDealInfo.getDealNumber() == 0) {
            return;
        }
        //从数据库中获取未销售的楼房数据
        List<HouseInfo> daUnSellHouseInfoList = dataOP.getUnSellHouseByName(dailyDealInfo.getName());

        if (daUnSellHouseInfoList.isEmpty()) {
            return;
        }
        //爬取当前楼盘的可售房子
        String url = daUnSellHouseInfoList.get(0).getUrl().substring(0, daUnSellHouseInfoList.get(0).getUrl().indexOf("?"));

        List<DepartmentInfo> departmentInfoList = new ArrayList<>();
        DepartmentInfo departmentInfo = new DepartmentInfo();
        departmentInfo.setName(daUnSellHouseInfoList.get(0).getDepartmentName());
        departmentInfo.setDistrictName(daUnSellHouseInfoList.get(0).getDistrictName());
        departmentInfo.setDistrictCode(daUnSellHouseInfoList.get(0).getDistrictCode());
        departmentInfo.setUrl(url);
        departmentInfoList.add(departmentInfo);

        List<HouseInfo> currentSellHouseInfoList = new ArrayList<>();
        List<HouseInfo> diffSellHouseInfoList = new ArrayList<>();
        Map<String, HouseInfo> houseInfoMap = new HashMap<>();
        if (daUnSellHouseInfoList != null) {
            houseInfoMap = daUnSellHouseInfoList.stream().collect(Collectors.toMap(HouseInfo::getHashCode, java.util.function.Function.identity()));
        }

        houseParser.run(departmentInfoList, false, houseInfoMap, currentSellHouseInfoList, HouseStateInfo.STATE_SOLD);

        updatePrice(dailyDealInfo, currentSellHouseInfoList, diffSellHouseInfoList);
    }

    /**
     * 记录每个楼盘的成交价
     *
     * @param dailyDealInfo
     * @param currentSellHouseInfoList
     * @param diffSellHouseInfoList
     */
    private void updatePrice(DailyDealInfo dailyDealInfo, List<HouseInfo> currentSellHouseInfoList, List<HouseInfo> diffSellHouseInfoList) {

        double totalPrice = dailyDealInfo.getDealAvgPrice() * dailyDealInfo.getDealArea();
        int dealNumber = dailyDealInfo.getDealNumber();

        //如果发现当前该楼盘的成交量与实际上的成交量有问题,那肯定是有次爬取失败导致数据不一致
        //这个时候,将未记录的成交房子置为当前均价
        if (dealNumber != currentSellHouseInfoList.size()) {
            try {
                currentSellHouseInfoList.stream().forEach(
                        e ->
                        {
                            e.setDealPrice(dailyDealInfo.getDealAvgPrice());
                            e.setStatus("已售");
                            dataOP.updateHouseDealInfo(e);
                        }
                );
                FileOP.writeFile("log/dailyDealInfo_parse_error", String.valueOf(new Date()), dailyDealInfo);
                FileOP.writeFile("log/dailyDealInfo_parse_again", String.valueOf(new Date()), dailyDealInfo);
                FileOP.writeFile("log/dailyDealInfo_parse_error", String.valueOf(new Date()), currentSellHouseInfoList);
                FileOP.writeFile("log/dailyDealInfo_parse_diff", String.valueOf(new Date()), diffSellHouseInfoList);
            } catch (Exception e) {
                log.error("failed to write daily deal parse error info ");
            }
            log.error("sell house can't be matched");
        }

        try {
            FileOP.writeFile("log/dailyDealInfo_parse", String.valueOf(new Date()), currentSellHouseInfoList);
        } catch (Exception e) {
            log.error("failed to write daily deal parse info");
        }


        log.info(currentSellHouseInfoList.toString());

        double originTotalPrice = currentSellHouseInfoList.stream().mapToDouble(e -> e.getTotalPrice()).sum();

        //有些楼盘没有备案价
        if (originTotalPrice == 0) {
            currentSellHouseInfoList.stream().forEach(
                    e -> e.setDealPrice(dailyDealInfo.getDealAvgPrice()));
        } else {
            currentSellHouseInfoList.stream().forEach(
                    e ->
                    {
                        e.setDealPrice((int) (totalPrice * (e.getOriginPrice() + e.getDecorationPrice()) / originTotalPrice));
                        e.setDealPercent(Double.parseDouble(new java.text.DecimalFormat("#.00").format(totalPrice / originTotalPrice)));
                    }
            );

        }


        for (HouseInfo houseInfo : currentSellHouseInfoList) {
            houseInfo.setStatus("已售");
            dataOP.updateHouseDealInfo(houseInfo);
            //写入到日志文件用于集成elk
            ESOP.writeToES("log/daily_deal_info_detail_es", JSONObject.toJSONString(houseInfo));
        }

    }


    private Optional<String> getDistrictByName(String name) {
        HouseInfo matchHouseInfo = dataOP.getHouseInfoByDepartmentName(name);
        if (matchHouseInfo != null) {
            return Optional.of(matchHouseInfo.getDistrictName());
        }
        return Optional.fromNullable(null);
    }

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
        return sb.toString();
    }

    public List<DailyBriefInfo> parseDailyBriefInfo() throws IOException, ParserException {


        Parser parser = new Parser(new URL("http://www.tmsf.com/index.jsp").openConnection());
        NodeFilter nodeFilter = new HasAttributeFilter("id", "myCont5");
        NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);
        if (nodeList.toNodeArray().length == 0) {
            return Collections.EMPTY_LIST;
        }

        List<DailyBriefInfo> dailyBriefInfoList = new ArrayList<>();

        //到1970/01/01 00:00:00的小时数
        int parseHour = (int) (Clock.systemUTC().millis() / (1000 * 3600));

        //到1970/01/01 00:00:00的天数
        int parseDay = (int) parseHour / 24;

        NodeList infoNodeList = nodeList.elementAt(0).getChildren().elementAt(1)
                .getChildren().elementAt(1).getChildren();

        for (int i = 5; i <= 13; i = i + 2) {
            DailyBriefInfo dailyBriefInfo = new DailyBriefInfo();
            dailyBriefInfo.setDistrict(CharMatcher.WHITESPACE.trimFrom(infoNodeList.elementAt(i).getChildren().elementAt(1).toPlainTextString()));
            dailyBriefInfo.setDealNumber(Integer.parseInt(CharMatcher.WHITESPACE.trimFrom(infoNodeList.elementAt(i).getChildren().elementAt(3).toPlainTextString())));
            dailyBriefInfo.setBookingNumber(Integer.parseInt(CharMatcher.WHITESPACE.trimFrom(infoNodeList.elementAt(i).getChildren().elementAt(5).toPlainTextString())));
            dailyBriefInfo.setRecentNumber(Integer.parseInt(CharMatcher.WHITESPACE.trimFrom(infoNodeList.elementAt(i).getChildren().elementAt(7).toPlainTextString())));
            dailyBriefInfo.setParseDay(parseDay);
            dailyBriefInfo.setParseHour(parseHour);

            dailyBriefInfoList.add(dailyBriefInfo);
            dataOP.insertBriefDealInfo(dailyBriefInfo);

            ESOP.writeToES("log/daily_brief_info_es", JSONObject.toJSONString(dailyBriefInfo));
        }

        return dailyBriefInfoList;

    }

}
