package net.dean.object;

import java.util.Date;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by dean on 16/6/27.
 */
public class HouseInfo {


    private int     id;
    private String  departmentName;     //小区
    private String  districtName;       //所属区
    private String  districtCode;       //所属区编码
    private String  sellCredit;         //预售证
    private String  buildingNumber;     //楼幢数
    private double  floor;              //楼层
    private String  doorNumber;         //门牌号
    private double  originArea;         //建筑面积
    private double  finalArea;          //实际面积
    private double  areaPercent;        //得房率
    private double  originPrice;        //备案价
    private double  decorationPrice;    //装修价
    private double  totalPrice;         //总价
    private double  dealPrice;          //成交价
    private double  dealPercent;        //折扣
    private Date    dealDate;           //成交日期
    private String  hashCode;           //小区+楼幢数+门牌号
    private String  url;                //URL

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public double getFloor() {
        return floor;
    }

    public void setFloor(double floor) {
        this.floor = floor;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public double getOriginArea() {
        return originArea;
    }

    public void setOriginArea(double originArea) {
        this.originArea = originArea;
    }

    public double getFinalArea() {
        return finalArea;
    }

    public void setFinalArea(double finalArea) {
        this.finalArea = finalArea;
    }

    public double getAreaPercent() {
        return areaPercent;
    }

    public void setAreaPercent(double areaPercent) {
        this.areaPercent = areaPercent;
    }

    public double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(double originPrice) {
        this.originPrice = originPrice;
    }

    public double getDecorationPrice() {
        return decorationPrice;
    }

    public void setDecorationPrice(double decorationPrice) {
        this.decorationPrice = decorationPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(double dealPrice) {
        this.dealPrice = dealPrice;
    }

    public Date getDealDate() {
        return dealDate;
    }

    public void setDealDate(Date dealDate) {
        this.dealDate = dealDate;
    }

    private String  status;             //状态

    private DepartmentInfo departmentInfo;    //小区信息

    public DepartmentInfo getDepartmentInfo() {
        return departmentInfo;
    }

    public HouseInfo setDepartmentInfo(DepartmentInfo departmentInfo) {
        this.departmentInfo = departmentInfo;
        return this;
    }

    public String getSellCredit() {
        return sellCredit;
    }

    public void setSellCredit(String sellCredit) {
        this.sellCredit = sellCredit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDealPercent() {
        return dealPercent;
    }

    public void setDealPercent(double dealPercent) {
        this.dealPercent = dealPercent;
    }

    public static class Builder{
        private String  departmentName;     //小区
        private String  districtName;       //所属区
        private String  districtCode;       //所属区编码
        private String  sellCredit;         //预售证
        private String  buildingNumber;     //楼幢数
        private double  floor;              //楼层
        private String  doorNumber;         //门牌号
        private double  originArea;         //建筑面积
        private double  finalArea;          //实际面积
        private double  areaPercent;        //得房率
        private double  originPrice;        //备案价
        private double  decorationPrice;    //装修价
        private double  totalPrice;         //总价
        private double  dealPrice;          //成交价
        private double  dealPercent;        //折扣
        private Date    dealDate;           //成交日期
        private String  status;             //状态
        private String  hashCode;           //小区+楼幢数+门牌号
        private String  url;                //url

        public Builder departmentName(String value){
            departmentName = value;
            return this;
        }

        public Builder sellCredit(String value){
            sellCredit = value;
            return this;
        }

        public Builder buildingNumber(String value){
            buildingNumber = value;
            return this;
        }

        public Builder floor(String value){
            floor = Double.parseDouble(value);
            return this;
        }

        public Builder doorNumber(String value){
            doorNumber = value;
            return this;
        }

        public Builder originArea(String value){
            originArea = Double.parseDouble(value);
            return this;
        }

        public Builder finalArea(String value){
            finalArea = Double.parseDouble(value);
            return this;
        }

        public Builder areaPercent(String value){
            areaPercent = Double.parseDouble(value);
            return this;
        }

        public Builder originPrice(String value){
            originPrice = Double.parseDouble(value);
            return this;
        }

        public Builder decorationPrice(String value){
            decorationPrice = Double.parseDouble(value);
            return this;
        }

        public Builder totalPrice(String value){
            totalPrice = Double.parseDouble(value);
            return this;
        }

        public Builder dealPrice(String value){
            dealPrice = Double.parseDouble(value);
            return this;
        }

        public Builder dealDate(Date value){
            dealDate = value;
            return this;
        }

        public Builder status(String value){
            status = value;
            return this;
        }

        public HouseInfo build(){
            return new HouseInfo(this);
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHashCode() {
            return hashCode;
        }

        public void setHashCode(String hashCode) {
            this.hashCode = hashCode;
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

        public double getDealPercent() {
            return dealPercent;
        }

        public void setDealPercent(double dealPercent) {
            this.dealPercent = dealPercent;
        }
    }

    private HouseInfo(Builder builder){

        this.departmentName = builder.departmentName;
        this.districtName = builder.districtName;
        this.districtCode = builder.districtCode;
        this.sellCredit = builder.sellCredit;
        this.buildingNumber = builder.buildingNumber;
        this.floor = builder.floor;
        this.doorNumber = builder.doorNumber;
        this.originArea = builder.originArea;
        this.finalArea = builder.finalArea;
        this.areaPercent = builder.areaPercent;
        this.originPrice = builder.originPrice;
        this.decorationPrice = builder.decorationPrice;
        this.totalPrice = builder.totalPrice;
        this.dealPrice = builder.dealPrice;
        this.dealDate = builder.dealDate;
        this.dealPercent = builder.dealPercent;
        this.status = builder.status;
        this.hashCode = builder.hashCode;
        this.url = builder.url;
    }

    public HouseInfo(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HouseInfo)) return false;

        HouseInfo houseInfo = (HouseInfo) o;

        return StringUtils.equalsIgnoreCase(houseInfo.getSellCredit(),this.getSellCredit())
                &&StringUtils.equalsIgnoreCase(houseInfo.getDepartmentName(),this.getDepartmentName())
                &&StringUtils.equalsIgnoreCase(houseInfo.getBuildingNumber(),this.getBuildingNumber())
                &&StringUtils.equalsIgnoreCase(houseInfo.getDoorNumber(),this.getDoorNumber())
                &&houseInfo.getOriginArea()== this.getOriginArea();
    }

    @Override
    public int hashCode() {
        return Hashing.md5().hashString(
                        this.getSellCredit()
                        +this.getDepartmentName()
                        +this.getBuildingNumber()
                        +this.getDoorNumber()
                        +this.getOriginArea(), Charsets.UTF_8).asInt();
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }

}
