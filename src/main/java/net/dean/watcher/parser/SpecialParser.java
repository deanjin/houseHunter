package net.dean.watcher.parser;

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
        departmentInfo.setDistrictName("西湖区");
        departmentInfo.setDistrictCode("330108");
        departmentInfo.setUrl("http://www.tmsf.com/newhouse/property_33_64897079_price.htm");
        departmentInfoList.add(departmentInfo);


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
}
