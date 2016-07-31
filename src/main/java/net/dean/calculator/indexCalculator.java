package net.dean.calculator;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.dean.common.ESOP;
import net.dean.common.MappingSet;
import net.dean.object.DailyBriefInfo;
import net.dean.object.DailyDealInfo;
import net.dean.object.DepartmentInfo;
import net.dean.watcher.parser.DepartmentParser;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dean on 16/7/25.
 * 用于计算一手房房价指数
 */
public class IndexCalculator {

    private final static Logger log = LoggerFactory.getLogger(IndexCalculator.class);

    private final static  Map<String, Table<String,String,String>> DISTRICT_MAP = new HashMap<>();

    static{
        Table<String,String,String> districtTables = HashBasedTable.create();
        districtTables.put("/newhouse/property_searchall.htm?sid=330184&keytype=1&propertystate=2" ,"330184","余杭");
        DISTRICT_MAP.put("余杭",districtTables);

        districtTables = HashBasedTable.create();
        districtTables.put("/newhouse/property_searchall.htm?sid=330181&keytype=1&propertystate=2" ,"330181","萧山");
        DISTRICT_MAP.put("萧山",districtTables);
    }

    public static void main(String[] args){
        IndexCalculator indexCalculator = new IndexCalculator();
        Table<String,String,String> districtTable = HashBasedTable.create();
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330103&keytype=1&propertystate=2" ,"330103","下城");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330106&keytype=1&propertystate=2" ,"330106","西湖");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330105&keytype=1&propertystate=2" ,"330105","拱墅");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330104&keytype=1&propertystate=2" ,"330104","江干");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330108&keytype=1&propertystate=2" ,"330108","滨江");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330110&keytype=1&propertystate=2" ,"330110","之江");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330102&keytype=1&propertystate=2" ,"330102","上城");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330186&keytype=1&propertystate=2" ,"330186","下沙");

//        districtTables.put("/newhouse/property_searchall.htm?sid=330181&keytype=1&propertystate=2" ,"330181","萧山");
    }

    public void calHouseIndexer(List<DailyDealInfo> dailyDealInfoList, List<DailyBriefInfo> dailyBriefInfoList){
        if(dailyDealInfoList.isEmpty() || dailyBriefInfoList.isEmpty()){
            return;
        }

        Table<Integer,Double,Integer> detailTable = HashBasedTable.create();

        ESOP.writeToES("log/daily_index_es",calMainDistrictIndexer(dailyDealInfoList,dailyBriefInfoList,detailTable).toJSONString());
        ESOP.writeToES("log/daily_index_es",calSpecialDistrictIndexer(dailyDealInfoList,dailyBriefInfoList,"余杭",detailTable).toJSONString());
        ESOP.writeToES("log/daily_index_es",calSpecialDistrictIndexer(dailyDealInfoList,dailyBriefInfoList,"萧山",detailTable).toJSONString());
        ESOP.writeToES("log/daily_index_es",calHangZhouIndexer(detailTable).toJSONString());

    }

    private JSONObject calHangZhouIndexer(Table<Integer,Double,Integer> detail){

        double totalRemainHouseCount=0,totalPriceSum=0,totalDealCount=0;

        for(Table.Cell<Integer,Double,Integer> cell : detail.cellSet()){
            totalRemainHouseCount += cell.getRowKey();
            totalPriceSum += cell.getColumnKey();
            totalDealCount += cell.getValue();
        }

        totalPriceSum/=detail.size();


        double index = 0;
        if(totalRemainHouseCount != 0){
            index = totalPriceSum * 1000 * totalDealCount / totalRemainHouseCount;
        }

        ESOP.writeToES("daily_index_detail_es", String.format("[杭州市][%s]剩余库存:%f,销售均价总和:%f,销售数量:%f,指数:%f",
                LocalDateTime.now().toString(),totalRemainHouseCount,totalPriceSum,totalDealCount,index));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("district","杭州市");
        jsonObject.put("index",index);
        return jsonObject;
    }

    private JSONObject calMainDistrictIndexer(List<DailyDealInfo> dailyDealInfoList,
                                              List<DailyBriefInfo> dailyBriefInfoList,
                                              Table<Integer,Double,Integer> detail){
        //剩余存库
        final AtomicInteger totalRemainHouseCount = new AtomicInteger(0);
        dailyBriefInfoList.forEach(e -> {
            if(StringUtils.equalsIgnoreCase("主城区",e.getDistrict())) {
                totalRemainHouseCount.addAndGet(e.getRecentNumber());
            }
        });

        //成交均价
        Table<String,String,String> districtTable = HashBasedTable.create();
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330103&keytype=1&propertystate=2" ,"330103","下城");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330106&keytype=1&propertystate=2" ,"330106","西湖");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330105&keytype=1&propertystate=2" ,"330105","拱墅");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330104&keytype=1&propertystate=2" ,"330104","江干");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330108&keytype=1&propertystate=2" ,"330108","滨江");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330110&keytype=1&propertystate=2" ,"330110","之江");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330102&keytype=1&propertystate=2" ,"330102","上城");
        districtTable.put("/newhouse/property_searchall.htm?sid=33&districtid=330186&keytype=1&propertystate=2" ,"330186","下沙");

        double avgPrice = calAvgPrice(districtTable);

        //成交销量总和
        final AtomicInteger totalDealCount = new AtomicInteger(0);
        dailyDealInfoList.forEach(e -> {
            if(MappingSet.isMainDistrict(e.getDistrict())) {
                totalDealCount.addAndGet(e.getDealNumber());
            };
        });

        detail.put(totalRemainHouseCount.get(),avgPrice,totalDealCount.get());

        double index = 0;
        if(totalRemainHouseCount.get() != 0){
            index = avgPrice * 1000 * totalDealCount.get() / totalRemainHouseCount.get();
        }

        ESOP.writeToES("daily_index_detail_es", String.format("[主城区][%s]剩余库存:%d,销售均价总和:%.2f,销售数量:%d,指数:%.2f",
                LocalDateTime.now().toString(),totalRemainHouseCount.get(),avgPrice,totalDealCount.get(),index));


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("district","主城区");
        jsonObject.put("index",index);
        return jsonObject;
    }


    private JSONObject calSpecialDistrictIndexer(List<DailyDealInfo> dailyDealInfoList,
                                                 List<DailyBriefInfo> dailyBriefInfoList,
                                                 String district,
                                                 Table<Integer,Double,Integer> detail){
        //剩余存库
        final AtomicInteger totalRemainHouseCount = new AtomicInteger(0);
        dailyBriefInfoList.forEach(e -> {
            if(e.getDistrict().startsWith(district)) {
                totalRemainHouseCount.addAndGet(e.getRecentNumber());
            }
        });

        //成交均价
        double avgPrice = calAvgPrice(DISTRICT_MAP.get(district));

        //成交销量总和
        final AtomicInteger totalDealCount = new AtomicInteger(0);
        dailyDealInfoList.forEach(e -> {
            if(e.getDistrict().startsWith(district)) {
                totalDealCount.addAndGet(e.getDealNumber());
            };
        });

        detail.put(totalRemainHouseCount.get(),avgPrice,totalDealCount.get());

        double index = 0;
        if(totalRemainHouseCount.get() != 0){
            index = avgPrice * 1000 * totalDealCount.get() / totalRemainHouseCount.get();
        }
        ESOP.writeToES("daily_index_detail_es", String.format("[%s][%s]剩余库存:%d,销售均价总和:%.2f,销售数量:%d,指数:%.2f",
                district,LocalDateTime.now().toString(),totalRemainHouseCount.get(),avgPrice,totalDealCount.get(),index));


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("district",district);
        jsonObject.put("index",index);
        return jsonObject;
    }

    private double calAvgPrice(Table<String,String,String> districtTable){
        if(districtTable.isEmpty()){
            return 0;
        }
        DepartmentParser departmentParser = new DepartmentParser();
        try {
            List<DepartmentInfo> departmentInfoList = departmentParser.getDepartmentByDistrict(districtTable);
            double totalAvgPrice = 0;
            int  totalAvailDepartmentCount = 0;
            int  totalUnAvailDepartmentCount = 0;
            for(DepartmentInfo departmentInfo : departmentInfoList){
                String price = departmentInfo.getAverPrice();
                try{
                    totalAvgPrice += Double.parseDouble(price);
                    totalAvailDepartmentCount++;
                }catch(Exception e){
                    totalUnAvailDepartmentCount++;
                    log.error("failed to parse average price for department:{},current number:{}",departmentInfo,totalUnAvailDepartmentCount);
                }
            }
            if(totalAvailDepartmentCount != 0){
                return totalAvgPrice / totalAvailDepartmentCount;
            }
        }catch(Exception e){
            log.error("calAvgPrice for district:{} catch exception:{}",districtTable,e);
        }
        return 0;
    }

}
