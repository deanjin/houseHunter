package net.dean.test;

import net.dean.object.SellCreditInfo;
import net.dean.watcher.parser.SellCreditParser;

/**
 * Created by dean on 16/8/28.
 */
public class SellCreditParserTest {

    public static void testParseSellCredit() {
        try {
            SellCreditParser sellCreditParser = new SellCreditParser();
            sellCreditParser.run("http://www.tmsf.com/index.jsp");
        } catch (Exception e) {

        }
    }

    public static void testParseOneSellCredit() {
        SellCreditInfo sellCreditInfo = new SellCreditInfo();
        sellCreditInfo.setUrl("/newhouse/property_33_232615031_price.htm");
        sellCreditInfo.setName("万科&middot;新都会1958");
        sellCreditInfo.setSellCredit("2016000104");
        SellCreditParser sellCreditParser = new SellCreditParser();
        sellCreditParser.parseOneSellCredit(sellCreditInfo);
    }
}
