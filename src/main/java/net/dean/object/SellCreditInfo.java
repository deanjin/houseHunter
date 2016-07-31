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
public class SellCreditInfo {

    private String name;
    private String sellCredit;
    private String url;
    private String creditDate;

    public SellCreditInfo(){

    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCreditDate() {
        return creditDate;
    }

    public void setCreditDate(String creditDate) {
        this.creditDate = creditDate;
    }
}
