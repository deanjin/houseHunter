package net.dean.object;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;

/**
 * Created by dean on 16/7/3.
 */
public class DailyBriefInfo {

    private int     id;
    private String  district;
    private int     dealNumber;
    private int     bookingNumber;
    private int     recentNumber;
    private int     parseDay;
    private int     parseHour;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getRecentNumber() {
        return recentNumber;
    }

    public void setRecentNumber(int recentNumber) {
        this.recentNumber = recentNumber;
    }

    public int getParseDay() {
        return parseDay;
    }

    public void setParseDay(int parseDay) {
        this.parseDay = parseDay;
    }

    public int getParseHour() {
        return parseHour;
    }

    public void setParseHour(int parseHour) {
        this.parseHour = parseHour;
    }

    public DailyBriefInfo() {
    }

    public DailyBriefInfo(String district, int dealNumber, int bookingNumber, int recentNumber, int parseDay, int parseHour) {
        this.district = district;
        this.dealNumber = dealNumber;
        this.bookingNumber = bookingNumber;
        this.recentNumber = recentNumber;
        this.parseDay = parseDay;
        this.parseHour = parseHour;
    }

    public DailyBriefInfo(int id, String district, int dealNumber, int bookingNumber, int recentNumber, int parseDay, int parseHour) {
        this.id = id;
        this.district = district;
        this.dealNumber = dealNumber;
        this.bookingNumber = bookingNumber;
        this.recentNumber = recentNumber;
        this.parseDay = parseDay;
        this.parseHour = parseHour;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
