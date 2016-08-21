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

//        runDepartment("西溪河滨之城","http://www.tmsf.com/newhouse/property_33_64897079_price.htm","西湖区","330108","29137",null);
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


//        departmentInfo.setName("西溪河滨之城");
//        departmentInfo.setDistrictName("西湖");
//        departmentInfo.setDistrictCode("330106");
//        departmentInfo.setUrl("http://www.tmsf.com/newhouse/property_33_64897079_price.htm");
//        SpecialParser specialParser = new SpecialParser();

        departmentInfo.setName("凯德·湖墅观邸");
        departmentInfo.setDistrictName("拱墅");
        departmentInfo.setDistrictCode("330105");
        departmentInfo.setUrl("http://www.tmsf.com/newhouse/property_33_93148643_price.htm");
        SpecialParser specialParser = new SpecialParser();


        departmentInfoList.add(departmentInfo);
        specialParser.diffSoldHouse(departmentInfoList);
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

    public void diffSoldHouse(List<DepartmentInfo> departmentInfoList){

        for(DepartmentInfo departmentInfo : departmentInfoList) {
            List<HouseInfo> houseInfoList = houseParser.run(departmentInfoList, false, null, null, HouseStateInfo.STATE_SOLD);

            List<HouseInfo> soldHouseInfoList = dataOP.getSoldHouseByName(departmentInfo.getName());

            List<HouseInfo> diffHouseInfoList = new ArrayList<>();

            for(HouseInfo webHouseInfo : houseInfoList){
                boolean contain = false;
                for(HouseInfo dbHouseInfo: soldHouseInfoList){
                    if(dbHouseInfo.getHashCode().equalsIgnoreCase(webHouseInfo.getHashCode())){
                        contain = true;
                        break;
                    }
                }
                if(!contain) {
                    diffHouseInfoList.add(webHouseInfo);
                }
            }

            String test ="test";
//            for(HouseInfo houseInfo : houseInfoList){
//                int index = soldHouseInfoList.indexOf(houseInfo);
//                if(index != -1){
//                    dataOP.updateConstraintHouseDealInfo(soldHouseInfoList.get(index));
//                }else{
//                    log.error("can't find sold house for constraint house:{}", houseInfo);
//                }
//            }

        }
    }
}
