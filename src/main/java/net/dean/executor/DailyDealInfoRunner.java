package net.dean.executor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import net.dean.common.MappingSet;
import net.dean.object.DailyDealInfo;
import net.dean.object.DailyBriefInfo;
import net.dean.calculator.IndexCalculator;
import net.dean.watcher.parser.DailyDealParser;



/**
 * Created by dean on 16/7/8.
 */
public class DailyDealInfoRunner {

    private final static Logger log = LoggerFactory.getLogger(DailyDealParser.class);

    public static void main(String[] args) {
        DailyDealInfoRunner dailyDealInfoRunner = new DailyDealInfoRunner();
        dailyDealInfoRunner.run();
    }

    public void run() {
        DailyDealParser dailyDealParser = new DailyDealParser();
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);

        scheduledThreadPoolExecutor.scheduleAtFixedRate(() ->
                {
                    LocalTime localTime = LocalTime.now();

                    log.info("schedule run daily deal info in time:{}", localTime);

                    if(localTime.getMinute() < 20 || localTime.getMinute() > 25){
                        return;
                    }

                    log.info("begin to run daily deal info in time:{}", localTime);
                    try {
                        List<DailyDealInfo> dailyDealInfoList = new ArrayList();
                        if (localTime.getHour() > 9 && localTime.getHour() <= MappingSet.RECORD_HOUR
                                ) {
                            dailyDealInfoList = dailyDealParser.run("http://www.tmsf.com/daily.htm");
                        }

                        List<DailyBriefInfo> dailyBriefInfoList = new ArrayList();
                        if (localTime.getHour() == MappingSet.RECORD_HOUR){
                            dailyBriefInfoList = dailyDealParser.parseDailyBriefInfo();
                        }

                        if(localTime.getHour() == MappingSet.RECORD_HOUR){
                            IndexCalculator indexCalculator = new IndexCalculator();
                            indexCalculator.calHouseIndexer(dailyDealInfoList,dailyBriefInfoList);
                        }
                    } catch (Exception e) {
                        log.error("failed to parse daily deal info in {}", localTime);
                    }
                },
                1, 60
                , TimeUnit.SECONDS
        );
    }

}
