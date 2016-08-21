package net.dean.executor;

import net.dean.object.DepartmentInfo;
import net.dean.watcher.parser.SpecialParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dean on 16/7/10.
 */
public class SpecialInfoRunner {

    private final static Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) throws Exception {
        SpecialInfoRunner specialInfoRunner = new SpecialInfoRunner();
        specialInfoRunner.run();
    }

    public void run() throws Exception {
        SpecialParser specialParser = new SpecialParser();

        List<DepartmentInfo> departmentInfoList = new ArrayList<>();
        DepartmentInfo departmentInfo = new DepartmentInfo();
//        departmentInfo.setName("西溪河滨之城");
//        departmentInfo.setDistrictName("西湖");
//        departmentInfo.setDistrictCode("330106");
//        departmentInfo.setUrl("http://www.tmsf.com/newhouse/property_33_64897079_price.htm");
        departmentInfo.setName("天峻公寓");
        departmentInfo.setDistrictName("余杭");
        departmentInfo.setDistrictCode("330184");
        departmentInfo.setUrl("http://www.tmsf.com/newhouse/property_330184_81564505_price.htm");
        departmentInfoList.add(departmentInfo);

        specialParser.parseConstraintHouse(departmentInfoList);
    }
}
