package net.dean.object;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.dean.watcher.parser.DepartmentParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

/**
 * Created by dean on 16/6/30.
 */
public class DepartmentInfo {

    private String name;
    private String districtName;
    private String districtCode;
    private String averPrice;
    private String url;

    public String getName() {
        return name;
    }

    public String getAverPrice() {
        return averPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAverPrice(String averPrice) {
        this.averPrice = averPrice;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private Map<String, String> sellCreditMap = Maps.newHashMap();
    private Map<String, String> buildingMap = Maps.newHashMap();
    private List<Integer> pageList = Lists.newArrayList();

    public Map<String, String> getSellCreditMap() {
        return sellCreditMap;
    }

    public void setSellCreditMap(Map<String, String> sellCreditMap) {
        this.sellCreditMap = sellCreditMap;
    }

    public Map<String, String> getBuildingMap() {
        return buildingMap;
    }

    public void setBuildingMap(Map<String, String> buildingMap) {
        this.buildingMap = buildingMap;
    }

    public List<Integer> getPageList() {
        return pageList;
    }

    public void setPageList(List<Integer> pageList) {
        this.pageList = pageList;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public static class Builder{
        private String name;
        private String distinct;
        private String averPrice;
        private String url;

        public Builder name(String value){
            this.name = value;
            return this;
        }

        public Builder averPrice(String value){
            this.averPrice = value;
            return this;
        }

        public Builder url(String value){
            this.url = value;
            return this;
        }

        public DepartmentInfo build(){
            return new DepartmentInfo(this);
        }

        public String getDistinct() {
            return distinct;
        }

        public void setDistinct(String distinct) {
            this.distinct = distinct;
        }
    }

    private DepartmentInfo(Builder builder){
        this.name = builder.name;
        this.averPrice = builder.averPrice;
        this.url = builder.url;
    }

    public DepartmentInfo(){}

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
