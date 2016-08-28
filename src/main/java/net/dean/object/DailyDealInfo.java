package net.dean.object;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by dean on 16/7/3.
 */
public class DailyDealInfo implements Serializable, Cloneable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getDealNumber() {
        return dealNumber;
    }

    public void setDealNumber(int dealNumber) {
        this.dealNumber = dealNumber;
    }

    public int getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(int bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public double getDealArea() {
        return dealArea;
    }

    public void setDealArea(double dealArea) {
        this.dealArea = dealArea;
    }

    public double getDealAvgPrice() {
        return dealAvgPrice;
    }

    public void setDealAvgPrice(double dealAvgPrice) {
        this.dealAvgPrice = dealAvgPrice;
    }

    private String  name;
    private String  district;
    private String  districtPart;
    private int     dealNumber;
    private int     bookingNumber;
    private double  dealArea;
    private double  dealAvgPrice;
    private int     parseDay;
    private int     parseHour;
    private LocalDateTime localDateTime = LocalDateTime.now();
    private int     parseOK=1;

    public int getParseHour() {
        return parseHour;
    }

    public void setParseHour(int parseHour) {
        this.parseHour = parseHour;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public int getParseDay() {
        return parseDay;
    }

    public void setParseDay(int parseDay) {
        this.parseDay = parseDay;
    }

    public String getDistrictPart() {
        return districtPart;
    }

    public void setDistrictPart(String districtPart) {
        this.districtPart = districtPart;
    }

    public int getParseOK() {
        return parseOK;
    }

    public void setParseOK(int parseOK) {
        this.parseOK = parseOK;
    }


    public static class Builder{
        private String  name;
        private String  district;
        private String  districtPart;
        private int     dealNumber;
        private int     bookingNumber;
        private double  dealArea;
        private double  dealAvgPrice;
        private int     parseDay;
        private int     parseHour;
        private LocalDateTime localDateTime;

        public Builder name(String value){
            this.name=value;
            return this;
        }

        public Builder district(String value){
            this.district=value;
            return this;
        }

        public Builder dealNumber(int value){
            this.dealNumber=value;
            return this;
        }

        public Builder bookingNumber(int value){
            this.bookingNumber=value;
            return this;
        }

        public Builder dealArea(double value){
            this.dealArea=value;
            return this;
        }

        public Builder dealAvgPrice(double value){
            this.dealAvgPrice=value;
            return this;
        }

        public Builder parseHour(int value){
            this.parseHour=value;
            return this;
        }

        public Builder parseDay(int value){
            this.parseDay=value;
            return this;
        }

        public DailyDealInfo build(){
            return new DailyDealInfo(this);
        }

        public int getParseDay() {
            return parseDay;
        }

        public void setParseDay(int parseDay) {
            this.parseDay = parseDay;
        }

        public String getDistrictPart() {
            return districtPart;
        }

        public void setDistrictPart(String districtPart) {
            this.districtPart = districtPart;
        }
    }

    private DailyDealInfo(Builder builder){
        this.name = builder.name;
        this.district = builder.district;
        this.districtPart = builder.districtPart;
        this.dealNumber = builder.dealNumber;
        this.bookingNumber = builder.bookingNumber;
        this.dealArea = builder.dealArea;
        this.dealAvgPrice = builder.dealAvgPrice;
        this.parseDay = builder.parseDay;
        this.parseHour = builder.parseHour;
    }

    public DailyDealInfo(){}


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DailyDealInfo)){
            return false;
        }
        return StringUtils.equalsIgnoreCase(((DailyDealInfo)obj).getName(), this.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return SerializationUtils.clone(this);
    }
}
