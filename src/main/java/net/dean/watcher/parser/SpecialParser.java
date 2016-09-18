package net.dean.watcher.parser;

import net.dean.common.ESOP;
import net.dean.common.FileOP;
import net.dean.dal.DataOP;
import net.dean.object.DepartmentInfo;
import net.dean.object.HouseInfo;
import net.dean.object.HouseStateInfo;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by dean on 16/7/16.
 * 一些特殊的爬取操作
 */
public class SpecialParser {

    private static final Logger log = LoggerFactory.getLogger(SpecialParser.class);

    private HouseParser houseParser = new HouseParser();
    private DataOP dataOP = new DataOP();

    public static void main(String[] args){
        SpecialParser specialParser = new SpecialParser();
        List<DepartmentInfo> departmentInfoList = new ArrayList<>();

        departmentInfoList.add( new DepartmentInfo("西溪河滨之城","http://www.tmsf.com/newhouse/property_33_64897079_price.htm","西湖区","330108","29137"));

        specialParser.updateConstraintHouse(departmentInfoList);
    }

    /***
     * 爬取该楼盘处于某一个状态的房间信息
     * @param departmentInfoList
     * @param houseStateInfo    例如:可售,已售
     */
    public void parseHouseByType(List<DepartmentInfo> departmentInfoList, HouseStateInfo houseStateInfo){
        for(DepartmentInfo departmentInfo : departmentInfoList) {
            List<HouseInfo> houseInfoList = houseParser.run(departmentInfoList, false, null, null, houseStateInfo);

            List<HouseInfo> soldHouseInfoList = dataOP.getSoldHouseByName(departmentInfo.getName());
            for(HouseInfo houseInfo : houseInfoList){
                int index = soldHouseInfoList.indexOf(houseInfo);
                if(index != -1){
                    dataOP.updateConstraintHouseDealInfo(soldHouseInfoList.get(index));
                }else{
                    log.error("can't find sold house for constraint house:{}", houseInfo);
                }
            }

        }
    }

    /***
     * 更新限制的为可售
     * @param departmentInfoList
     */
    public void updateConstraintHouse(List<DepartmentInfo> departmentInfoList){
        for(DepartmentInfo departmentInfo : departmentInfoList) {
            List<HouseInfo> houseInfoList = houseParser.run(departmentInfoList, false, null, null, HouseStateInfo.STATE_CONSTRAINT);
            List<HouseInfo> constraintHouseInfoList = dataOP.getConstraintHouseByName(departmentInfo.getName());
            for(HouseInfo houseInfo : constraintHouseInfoList){
                int index = houseInfoList.indexOf(houseInfo);
                if(index == -1){
                    dataOP.updateUnsoldHouseDealInfo(houseInfo);
                }else{
                    log.error("can't update sold house for constraint house:{}", houseInfo);
                }
            }

        }
    }

    /***
     * 同步透明网与数据库的已售信息
     * @param departmentInfoList
     */
    public void correctSoldHouse(List<DepartmentInfo> departmentInfoList){

        for(DepartmentInfo departmentInfo : departmentInfoList) {
            List<DepartmentInfo> departmentInfos = new ArrayList<>();
            departmentInfos.add(departmentInfo);
            List<HouseInfo> houseInfoList = houseParser.run(departmentInfos, false, null, null, HouseStateInfo.STATE_SOLD);
            List<HouseInfo> constraintInfoList = houseParser.run(departmentInfos, false, null, null, HouseStateInfo.STATE_CONSTRAINT);
            List<HouseInfo> soldHouseInfoList = dataOP.getSoldHouseByName(departmentInfo.getName());
            List<HouseInfo> unsoldHouseInfoList = dataOP.getUnSellHouseByName(departmentInfo.getName());
            List<HouseInfo> diffHouseInfoList = new ArrayList<>();
            if(soldHouseInfoList.size() == houseInfoList.size() || houseInfoList.size()+constraintInfoList.size()==soldHouseInfoList.size()){
                continue;
            }

            for(HouseInfo webHouseInfo : houseInfoList){
                for(HouseInfo dbHouseInfo: unsoldHouseInfoList){
                    if(dbHouseInfo.getHashCode().equalsIgnoreCase(webHouseInfo.getHashCode())){
                        diffHouseInfoList.add(dbHouseInfo);
                        break;
                    }
                }
            }

            double dealPercent = dataOP.getMinDealPercent(houseInfoList.get(0).getDepartmentName());
            for(HouseInfo houseInfo : diffHouseInfoList){

                if(dealPercent != 0){

                    houseInfo.setStatus("已售");
                    houseInfo.setDealPercent(dealPercent);
                    houseInfo.setDealPrice((houseInfo.getOriginPrice()+houseInfo.getDecorationPrice())*dealPercent);
                        dataOP.updateHouseDealInfo(houseInfo);
                        //写入到日志文件用于集成elk
                        ESOP.writeToES("log/daily_deal_info_detail_es", JSONObject.toJSONString(houseInfo));
                        try {
                            FileOP.writeFile("log/dailyDealInfo_parse_daily_deal_correct", String.valueOf(new Date()), JSONObject.toJSONString(houseInfo));
                        }catch(Exception e){

                        }

                }
            }

        }
    }
}
