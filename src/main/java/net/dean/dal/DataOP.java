package net.dean.dal;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import net.dean.object.DailyBriefInfo;
import net.dean.object.DailyDealInfo;
import net.dean.object.HouseInfo;
import net.dean.object.SellCreditInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dean on 16/7/5.
 */
public class DataOP {

    private final static Logger log = LoggerFactory.getLogger(DataOP.class);

    private static SqlSessionFactory sessionFactory;

    public static void main(String[] args){
        DailyDealInfo dailyDealInfo = new DailyDealInfo();
        dailyDealInfo.setName("test");
        dailyDealInfo.setDistrict("test1");
        dailyDealInfo.setBookingNumber(1);
        dailyDealInfo.setDealNumber(2);
        dailyDealInfo.setDealArea(11.1);
        dailyDealInfo.setDealAvgPrice(12.1);

        DataOP dataOP = new DataOP();
        dataOP.insertDailyDealInfo(dailyDealInfo);
    }

    static{
        try {
            Reader reader    = Resources.getResourceAsReader("config/dal.xml");
            sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        }catch(Exception e){
            log.error("init sql session factory failed",e);
        }
    }

    public boolean insertDailyDealInfo(DailyDealInfo dailyDealInfo){
        try {
        SqlSession session = sessionFactory.openSession();
            int success = session.insert("insertDailyDealInfo",dailyDealInfo);
            session.commit();
            session.close();
            return true;
        }catch(Exception e){
            log.error("when do insertDailyDealInfo {} catch exception: {}", dailyDealInfo,e);
        }
        return false;
    }

    public boolean insertBriefDealInfo(DailyBriefInfo dailyBriefInfo){
        try {
            SqlSession session = sessionFactory.openSession();
            session.insert("insertDailyBriefInfo",dailyBriefInfo);
            session.commit();
            session.close();
            return true;
        }catch(Exception e){
            log.error("when do insertBriefDealInfo {} catch exception: {}", dailyBriefInfo,e);
        }
        return false;
    }


    public boolean insertHouseInfo(HouseInfo houseInfo){
        try {
            SqlSession session = sessionFactory.openSession();
            int success = session.insert("insertHouseInfo",houseInfo);
            session.commit();
            session.close();
            return true;
        }catch(Exception e){
            log.error("when do insertHouseInfo {} catch exception: {}", houseInfo,e);
        }
        return false;
    }


    public boolean insertSellCreditInfo(SellCreditInfo sellCreditInfo){
        try {
            SqlSession session = sessionFactory.openSession();
            int success = session.insert("insertSellCreditInfo",sellCreditInfo);
            session.commit();
            session.close();
            return true;
        }catch(Exception e){
            log.error("when do insertSellCreditInfo {} catch exception: {}", sellCreditInfo,e);
        }
        return false;
    }

    public HouseInfo getHouseInfoByDepartmentName(String departmentName){
        try {
            SqlSession session = sessionFactory.openSession();
            List<HouseInfo> houseInfoList = session.selectList("selectDepartmentForName",departmentName);
            session.commit();
            session.close();
            return houseInfoList.get(0);
        }catch(Exception e){
            log.error("when do getHouseInfoByDepartmentName {} catch exception: {}", departmentName,e);
        }
        return null;
    }

    public List<DailyDealInfo> getDailyDealInfoByHour(int parseDay){
        try {
            SqlSession session = sessionFactory.openSession();
            List<DailyDealInfo> DailyDealInfoList = session.selectList("getDailyDealInfoByDate",parseDay);
            session.commit();
            session.close();
            return DailyDealInfoList;
        }catch(Exception e){
            log.error("when do getDailyDealInfoByHour {} catch exception: {}", parseDay,e);
        }
        return null;
    }

    public List<HouseInfo> getUnSellHouseByName(String name){
        try {
            SqlSession session = sessionFactory.openSession();
            List<HouseInfo> houseInfoList = session.selectList("selectUnSellHouseForName",name);
            session.commit();
            session.close();
            return houseInfoList;
        }catch(Exception e){
            log.error("when do getUnSellHouseByName {} catch exception: {}", name,e);
        }
        return null;
    }

    public List<HouseInfo> getSoldHouseByName(String name){
        try {
            SqlSession session = sessionFactory.openSession();
            List<HouseInfo> houseInfoList = session.selectList("selectSoldHouseForName",name);
            session.commit();
            session.close();
            return houseInfoList;
        }catch(Exception e){
            log.error("when do getSoldHouseByName {} catch exception: {}", name,e);
        }
        return null;
    }


    public boolean updateHouseDealInfo(HouseInfo houseInfo){
        try {
            SqlSession session = sessionFactory.openSession();
            List<HouseInfo> houseInfoList = session.selectList("updateHouseDealInfo",houseInfo);
            session.commit();
            session.close();
            return true;
        }catch(Exception e){
            log.error("when do updateHouseDealInfo {} catch exception: {}", houseInfo ,e);
        }
        return false;
    }

    public boolean updateConstraintHouseDealInfo(HouseInfo houseInfo){
        try {
            SqlSession session = sessionFactory.openSession();
            List<HouseInfo> houseInfoList = session.selectList("updateConstraintHouseDealInfo",houseInfo);
            session.commit();
            session.close();
            return true;
        }catch(Exception e){
            log.error("when do updateConstraintHouseDealInfo {} catch exception: {}", houseInfo ,e);
        }
        return false;
    }

    public List<HouseInfo> getAllHouseInfoByDepartmentName(String departmentName){
        List<HouseInfo> houseInfoList = new ArrayList<>();
        try {
            SqlSession session = sessionFactory.openSession();
            houseInfoList = session.selectList("getAllHouseForName",departmentName);
            session.commit();
            session.close();
        }catch(Exception e){
            log.error("when do getAllHouseInfoByDepartmentName {} catch exception: {}", departmentName,e);
        }
        return houseInfoList;
    }

    public boolean updateHouseInfo(HouseInfo houseInfo){
        try {
            SqlSession session = sessionFactory.openSession();
            session.update("updateHouseInfoForName",houseInfo);
            session.commit();
            session.close();
            return true;
        }catch(Exception e){
            log.error("when do updateHouseInfo {} catch exception: {}", houseInfo ,e);
        }
        return false;
    }
}
