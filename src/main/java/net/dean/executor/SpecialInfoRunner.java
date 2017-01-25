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
        correctSoldHouseInfo();
    }

    /**
     * 每天晚上23点校准当前销售但是未成功记录的房子信息
     */
    private void correctSoldHouseInfo(){
        SpecialParser specialParser = new SpecialParser();

        List<DepartmentInfo> departmentInfoList = new ArrayList<>();

        departmentInfoList.add( new DepartmentInfo("西溪河滨之城","http://www.tmsf.com/newhouse/property_33_64897079_price.htm","西湖区","330108","29137"));
        departmentInfoList.add( new DepartmentInfo("黄龙金茂悦","http://www.tmsf.com/newhouse/property_33_57411595_price.htm","拱墅","330105","34528"));
        departmentInfoList.add( new DepartmentInfo("凯德&middot;湖墅观邸","http://www.tmsf.com/newhouse/property_33_93148643_price.htm","拱墅","330105","33566"));
        departmentInfoList.add( new DepartmentInfo("天峻公寓","http://www.tmsf.com/newhouse/property_330184_81564505_price.htm","余杭","330184","17378"));
        departmentInfoList.add( new DepartmentInfo("星空公寓","http://www.tmsf.com/newhouse/property_330184_254120984_price.htm","余杭","330184","24538"));
        departmentInfoList.add( new DepartmentInfo("万科&middot;新都会1958","http://www.tmsf.com/newhouse/property_33_232615031_price.htm","下城","330103","42436"));
        departmentInfoList.add( new DepartmentInfo("东方星城","http://www.tmsf.com/newhouse/property_33_238384144_price.htm","江干","330104","26876"));
        departmentInfoList.add( new DepartmentInfo("绿城九龙仓&middot;柳岸晓风", "http://www.tmsf.com/newhouse/property_33_239218542_price.htm", "滨江", "330108", "41484"));

        departmentInfoList.add( new DepartmentInfo("北大资源未名府","http://www.tmsf.com/newhouse/property_330184_265972817_price.htm","余杭","330184","17081"));
        departmentInfoList.add( new DepartmentInfo("绿城西子田园牧歌","http://www.tmsf.com/newhouse/property_33_2582_price.htm","拱墅","330105","18442"));

        departmentInfoList.add( new DepartmentInfo("云杉郡景中心","http://www.tmsf.com/newhouse/property_33_264942875_price.htm","滨江","330108","16677"));

        departmentInfoList.add( new DepartmentInfo("九龙仓&middot;珑玺", "http://www.tmsf.com/newhouse/property_33_231599256_price.htm", "拱墅", "330105", "29552"));
        departmentInfoList.add( new DepartmentInfo("水色宜居", "http://www.tmsf.com/newhouse/property_33_163840737_price.htm", "拱墅", "330105", "33672"));
        departmentInfoList.add( new DepartmentInfo("孔雀蓝轩", "http://www.tmsf.com/newhouse/property_33_177105285_price.htm", "拱墅", "330105", "26355"));
        departmentInfoList.add( new DepartmentInfo("学院华庭", "http://www.tmsf.com/newhouse/property_33_101409102_price.htm", "西湖", "330106", "50365"));
//        departmentInfoList.add( new DepartmentInfo("白马湖和院", "http://www.tmsf.com/newhouse/property_33_115301085_price.htm", "滨江", "330108", "17244"));
        departmentInfoList.add( new DepartmentInfo("滨江&middot;铂金海岸", "http://www.tmsf.com/newhouse/property_33_272625341_price.htm", "江干", "330104", "25407"));
        departmentInfoList.add( new DepartmentInfo("西溪银泰商业中心", "http://www.tmsf.com/newhouse/property_33_122036672_price.htm", "西湖", "330106", "0"));
        departmentInfoList.add( new DepartmentInfo("湖漫雅筑", "http://www.tmsf.com/newhouse/property_33_25749011_price.htm", "滨江", "330108", "0"));
        departmentInfoList.add( new DepartmentInfo("御溪花苑", "http://www.tmsf.com/newhouse/property_33_49559595_price.htm", "西湖", "330106", "0"));
        departmentInfoList.add( new DepartmentInfo("玉泉二期&middot;香樟洋房", "http://www.tmsf.com/newhouse/property_33_302207316_price.htm", "西湖", "330106", "0"));

        departmentInfoList.add( new DepartmentInfo("金都艺墅","http://www.tmsf.com/newhouse/property_33_2554_price.htm","之江","330110","21496"));
        departmentInfoList.add( new DepartmentInfo("运河金麟府","http://www.tmsf.com/newhouse/property_33_240333664_price.htm","拱墅","330105","0"));

        departmentInfoList.add( new DepartmentInfo("卓蓝华庭", "http://www.tmsf.com/newhouse/property_33_164427716_price.htm", "江干", "330104", "12491"));
        departmentInfoList.add( new DepartmentInfo("运河宸园", "http://www.tmsf.com/newhouse/property_33_49436919_price.htm", "拱墅", "330105", "27433"));

//        departmentInfoList.add( new DepartmentInfo("广厦天都城", "http://www.tmsf.com/newhouse/property_330184_20262405_price.htm", "余杭", "330184", "12491"));

        departmentInfoList.add( new DepartmentInfo("云荷廷", "http://www.tmsf.com/newhouse/property_33_61780345_price.htm", "之江", "330110", "28374"));

        departmentInfoList.add( new DepartmentInfo("阳光郡公寓", "http://www.tmsf.com/newhouse/property_33_8911_price.htm", "拱墅", "330105", "19877"));
        departmentInfoList.add( new DepartmentInfo("紫蝶苑", "http://www.tmsf.com/newhouse/property_33_59170394_price.htm", "西湖", "330106", "23189"));
        departmentInfoList.add( new DepartmentInfo("万科郡西澜山", "http://www.tmsf.com/newhouse/property_330184_186063736_price.htm", "余杭", "330184", "18219"));

        departmentInfoList.add( new DepartmentInfo("西溪蓝海", "http://www.tmsf.com/newhouse/property_330184_177675733_price.htm", "余杭", "330184", "14792"));
        departmentInfoList.add( new DepartmentInfo("雍荣华庭", "http://www.tmsf.com/newhouse/property_33_226845036_price.htm", "拱墅", "330105", "27560"));

        departmentInfoList.add( new DepartmentInfo("都会翡翠花苑", "http://www.tmsf.com/newhouse/property_33_235799135_price.htm", "江干", "330104", "33441"));
        departmentInfoList.add( new DepartmentInfo("运河金麟府", "http://www.tmsf.com/newhouse/property_33_240333664_price.htm", "拱墅", "330105", "37695"));
        departmentInfoList.add( new DepartmentInfo("映月台公寓", "http://www.tmsf.com/newhouse/property_330184_290596929_price.htm", "余杭", "330184", "22902"));
        departmentInfoList.add( new DepartmentInfo("溪岸悦府", "http://www.tmsf.com/newhouse/property_330184_166388413_price.htm", "余杭", "330184", "25625"));

        departmentInfoList.add( new DepartmentInfo("阳光城&middot;文澜府", "http://www.tmsf.com/newhouse/property_33_238789032_price.htm", "拱墅", "330105", "32057"));

        departmentInfoList.add( new DepartmentInfo("萍实公寓", "http://pay.tmsf.com/newhouse/property_33_78158625_price.htm", "拱墅", "330105", "30976"));

        departmentInfoList.add( new DepartmentInfo("滨江&middot;锦绣之城", "http://www.tmsf.com/newhouse/property_33_290876212_price.htm", "拱墅", "330105", "44585"));

        departmentInfoList.add( new DepartmentInfo("海域晶华公寓", "http://www.tmsf.com/newhouse/property_33_92241652_price.htm", "江干", "330104", "47232"));

        departmentInfoList.add( new DepartmentInfo("玉观邸", "http://www.tmsf.com/newhouse/property_33_16444178_price.htm", "下沙", "330186", "15948"));

        departmentInfoList.add( new DepartmentInfo("百翘星辉名阁", "http://www.tmsf.com/newhouse/property_33_140153398_price.htm", "下沙", "330186", "12534"));
        departmentInfoList.add( new DepartmentInfo("碧月华庭", "http://www.tmsf.com/newhouse/property_33_231571583_price.htm", "拱墅", "330105", "36524"));

        departmentInfoList.add( new DepartmentInfo("新城香悦奥府", "http://www.tmsf.com/newhouse/property_330181_242385206_price.htm", "萧山", "330181", "24488"));

        departmentInfoList.add( new DepartmentInfo("君宸公寓", "http://www.tmsf.com/newhouse/property_330181_226325424_price.htm", "萧山", "330181", "27207"));

        departmentInfoList.add( new DepartmentInfo("江南之星公寓", "http://www.tmsf.com/newhouse/property_330181_302653066_price.htm", "萧山", "330181", "28536"));

        departmentInfoList.add( new DepartmentInfo("滨盛金茂府", "http://www.tmsf.com/newhouse/property_33_283414761_price.htm", "滨江", "330108", "39138"));

        departmentInfoList.add( new DepartmentInfo("春森俪湾", "http://www.tmsf.com/newhouse/property_33_295846466_price.htm", "西湖", "330106", "21791"));

        departmentInfoList.add( new DepartmentInfo("玖樟公寓", "http://www.tmsf.com/newhouse/property_33_339187325_price.htm", "江干", "330104", "33000"));


        specialParser.correctSoldHouse(departmentInfoList);

    }
}
