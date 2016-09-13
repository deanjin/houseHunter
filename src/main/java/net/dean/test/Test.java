package net.dean.test;

import net.dean.common.AssistantOP;
import net.dean.common.FileOP;
import net.dean.object.HouseInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dean on 16/8/28.
 */
public class Test {
    public static void main(String[] args){
        Test.testCombination();
    }

    private static void testCombination(){
        List<HouseInfo> houseInfoList = new ArrayList<>();
        HouseInfo houseInfo = new HouseInfo();
        houseInfo.setOriginArea(80);
        houseInfoList.add(houseInfo);
        houseInfo = new HouseInfo();
        houseInfo.setOriginArea(90);
        houseInfoList.add(houseInfo);
        houseInfo = new HouseInfo();
        houseInfo.setOriginArea(100);
        houseInfoList.add(houseInfo);
        houseInfo = new HouseInfo();
        houseInfo.setOriginArea(110);
        houseInfoList.add(houseInfo);
        houseInfo = new HouseInfo();
        houseInfo.setOriginArea(130);
        houseInfoList.add(houseInfo);

        List<Integer> numList = new ArrayList<>();
        for(int i=0;i<houseInfoList.size();i++){
            numList.add(i);
        }

        List<List<Integer>> combinationList = AssistantOP.getCombinationForList(numList,2);
        for(List<Integer> list : combinationList){
            double dealArea = list.stream().mapToDouble(e->houseInfoList.get(e).getOriginArea()).sum();
            if(dealArea==200){
                for(int i=list.size();i>0;--i){
                    houseInfoList.remove(i);
                }
                break;
            }
        }

        combinationList.forEach(System.out::println);
    }


    private static void testHouseCombination(){

        HouseInfo houseInfo = new HouseInfo();
        houseInfo.setOriginArea(109.46);
        List<HouseInfo> currentSellHouseInfoList = new ArrayList<>();
        currentSellHouseInfoList.add(houseInfo);
        houseInfo = new HouseInfo();
        houseInfo.setOriginArea(89.72);
        currentSellHouseInfoList.add(houseInfo);
        int dealNumber = 1;

        boolean isFound = false;
        List<HouseInfo> tmpHouseInfos = new ArrayList<>();
        //有可能当前小时的统计数落后于实际的这个销售数
        //将当前小时的所有出售的房子按照实际销售数量做组合排列,选出其中的一组总销售面积满足实际销售面积
        if(currentSellHouseInfoList.size() > dealNumber){
            List<Integer> numList = new ArrayList<>();
            for(int i=0;i<currentSellHouseInfoList.size();i++){
                numList.add(i);
            }

            try {
                List<List<Integer>> combinationList = AssistantOP.getCombinationForList(numList, dealNumber);
                for (List<Integer> list : combinationList) {
                    double dealArea = list.stream().mapToDouble(e -> currentSellHouseInfoList.get(e).getOriginArea()).sum();
                    if (dealArea == 89.72) {
                        for (int i = list.size(); i > 0; --i) {
                            tmpHouseInfos.add(currentSellHouseInfoList.get(i));
                        }
                        isFound = true;
                        break;
                    }
                }
            }catch(Exception e){
                FileOP.writeFile("log/daily_error_"+ LocalDate.now().toString(),
                        String.format("failed to updatePrice:%s, exception:%s",null,e));
            }
        }

        if(isFound){
            currentSellHouseInfoList.clear();
            currentSellHouseInfoList.addAll(tmpHouseInfos);
        }
    }
}
