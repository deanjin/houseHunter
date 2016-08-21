package net.dean.common;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterators;
import com.google.common.hash.Hashing;
import net.dean.dal.DataOP;
import net.dean.object.HouseInfo;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.tags.LinkTag;

import java.util.*;

/**
 * Created by dean on 16/7/18.
 */
public class AssistantOP {

    private static DataOP dataOP = new DataOP();

    public static void main(String[] args){
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

    public static List<List<Integer>> getCombinationForList(List<Integer> list, int n){
        if(list==null || list.isEmpty() || n > list.size()){
            return Collections.EMPTY_LIST;
        }
        List<List<Integer>> finalList = new ArrayList<>();
        List<Integer> curCombinationList = new ArrayList<>();

        generateCombination(finalList,list,0,curCombinationList,n);

        return finalList;
    }

    private static void generateCombination(List<List<Integer>> finalList, List<Integer> list, int n, List<Integer> curCombinationList, int num){
        if(num == n){
            finalList.add(curCombinationList);
            return;
        }

        for(Integer i : list){
            List<Integer> nextCombinationList = new ArrayList<>();
            nextCombinationList.addAll(curCombinationList);
            nextCombinationList.add(i);
            List<Integer> nextList = new ArrayList<>();
            nextList.addAll(list);
            Iterator<Integer> iterator = nextList.iterator();
            while(iterator.hasNext()) {
                if(iterator.next()<=i) {
                    iterator.remove();
                }
            }
            if(nextCombinationList.size()>num){
                break;
            }
            generateCombination(finalList,nextList,nextCombinationList.size(), nextCombinationList,num);
        }
    }
}
