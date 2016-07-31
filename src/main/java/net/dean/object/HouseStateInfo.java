package net.dean.object;

/**
 * Created by dean on 16/7/13.
 */
public enum HouseStateInfo {

    STATE_SELL_AVAILABLE("&housestate=1","可售"),
    STATE_SOLD("&housestate=2","可售"),
    STATE_BOOKING("&housestate=3","已经预定"),
    STATE_CONSTRAINT("&housestate=4","限制房产");

    private String name;
    private String desc;

    HouseStateInfo(String name,String desc){
        this.name = name;
        this.desc = desc;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
