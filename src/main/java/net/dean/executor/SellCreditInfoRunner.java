package net.dean.executor;

import net.dean.setting.URLConfig;
import net.dean.watcher.parser.DailyDealParser;
import net.dean.watcher.parser.SellCreditParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by dean on 16/7/8.
 */
public class SellCreditInfoRunner {

    private final static Logger log = LoggerFactory.getLogger(DailyDealParser.class);

    public static void main(String[] args) {
        SellCreditInfoRunner sellCreditInfoRunner = new SellCreditInfoRunner();
        sellCreditInfoRunner.run();
    }

    public void run() {
        SellCreditParser sellCreditParser = new SellCreditParser();
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);

        scheduledThreadPoolExecutor.scheduleWithFixedDelay(() ->
                {
                    Date date = new Date();
                    try {
                        sellCreditParser.run(URLConfig.URL_PREFIX + "/index.jsp");
                    } catch (Exception e) {
                        log.error("failed to parse sell credit info in {}", date);
                    }
                },
                5, 60 * 60

                , TimeUnit.SECONDS
        );

    }
}
