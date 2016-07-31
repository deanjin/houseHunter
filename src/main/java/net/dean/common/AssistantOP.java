package net.dean.common;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import net.dean.dal.DataOP;
import net.dean.object.HouseInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by dean on 16/7/18.
 */
public class AssistantOP {

    private static DataOP dataOP = new DataOP();

    public static void main(String[] args){
        AssistantOP.updateHashCodeForHouse("西溪河滨之城");
    }

    public static void updateHashCodeForHouse(String departmentName){
        if(StringUtils.isBlank(departmentName)){
            return;
        }
        List<HouseInfo> houseInfoList = dataOP.getAllHouseInfoByDepartmentName(departmentName);
        houseInfoList.forEach(
                e-> {
                    e.setHashCode(Hashing.md5().hashString(
                                    e.getSellCredit()
                                    + e.getDepartmentName()
                                    + e.getBuildingNumber()
                                    + e.getDoorNumber()
                                    + e.getOriginArea(), Charsets.UTF_8).toString());
                    dataOP.updateHouseInfo(e);
                }
        );
    }
}
