package net.dean.watcher.parser;

import com.alibaba.fastjson.JSONObject;
import net.dean.common.ESOP;
import net.dean.dal.DataOP;
import net.dean.object.DepartmentInfo;
import net.dean.object.HouseInfo;
import net.dean.object.HouseStateInfo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dean on 16/7/16.
 */
public class SpecialParser {

    private static final Logger log = LoggerFactory.getLogger(SpecialParser.class);

    private HouseParser houseParser = new HouseParser();
    private DataOP dataOP = new DataOP();

    public static void main(String[] args){

        List<DepartmentInfo> departmentInfoList = new ArrayList<>();
        DepartmentInfo departmentInfo = new DepartmentInfo();

        departmentInfo.setName("西溪河滨之城");
        departmentInfo.setDistrictName("西湖");
        departmentInfo.setDistrictCode("330106");
        departmentInfo.setUrl("http://www.tmsf.com/newhouse/property_33_64897079_price.htm");
        SpecialParser specialParser = new SpecialParser();
//
//        departmentInfo.setName("凯德·湖墅观邸");
//        departmentInfo.setDistrictName("拱墅");
//        departmentInfo.setDistrictCode("330105");
//        departmentInfo.setUrl("http://www.tmsf.com/newhouse/property_33_93148643_price.htm");
//        SpecialParser specialParser = new SpecialParser();
//

        departmentInfoList.add(departmentInfo);
        specialParser.correctSoldHouse(departmentInfoList);
    }

    public void parseConstraintHouse(List<DepartmentInfo> departmentInfoList){
        for(DepartmentInfo departmentInfo : departmentInfoList) {
            List<HouseInfo> houseInfoList = houseParser.run(departmentInfoList, false, null, null, HouseStateInfo.STATE_CONSTRAINT);

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
                    houseInfo.setDealPrice(houseInfo.getOriginPrice()*dealPercent);
                        dataOP.updateHouseDealInfo(houseInfo);
                        //写入到日志文件用于集成elk
                        ESOP.writeToES("log/daily_deal_info_detail_es", JSONObject.toJSONString(houseInfo));

                }
            }

        }
    }
}
