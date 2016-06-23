package net.dean.executor;

import net.dean.watcher.html.Crawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by dean on 16/6/24.
 */
public class Runner {

    private final static Logger log = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args){
        Crawler crawler = new Crawler();
        try {
            crawler.addUrl("http://www.tmsf.com/daily.htm");
            crawler.run();
        }catch(Exception e){
            log.error("",e);
        }
    }
}
