package net.dean.common;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
/**
 * Created by dean on 16/7/26.
 */
public class ESOP {

    private final static Logger log = LoggerFactory.getLogger(ESOP.class);
    /***
     * 每天的成交记录写入到ES中
     * @param message
     */
    public static void writeToES(String file, String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        try {
            FileOP.writeFile(file, message);
        } catch (Exception e) {
            log.error("failed to write daily deal info to es for message:{}", message);
        }
    }
}
