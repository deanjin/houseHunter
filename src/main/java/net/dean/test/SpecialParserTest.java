package net.dean.test;

import net.dean.object.DepartmentInfo;
import net.dean.watcher.parser.SpecialParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dean on 16/8/28.
 */
public class SpecialParserTest {


    public static void testCorrectHouseInfo(){
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
}
