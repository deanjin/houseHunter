package net.dean.watcher.html;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by dean on 16/6/23.
 * 爬虫,爬呀爬
 */
public class Crawler {

    private final ExecutorService crawlerExecutor;

    private final static Logger log =  LoggerFactory.getLogger(Crawler.class);

    private final BlockingQueue<URL> urlQueue = new LinkedBlockingQueue<URL>();

    private final Map<URL, String> htmlMap = new ConcurrentHashMap<>();

    public Crawler(){
        crawlerExecutor = new ThreadPoolExecutor(4,
                                                20,
                                                10,
                                                TimeUnit.MINUTES,
                                                new SynchronousQueue<Runnable>(),
                                                Executors.defaultThreadFactory(),
                                                new ThreadPoolExecutor.AbortPolicy());
    }

    public void addUrl(String url) throws MalformedURLException, InterruptedException{
        if(StringUtils.isNoneBlank(url)){
                URL crawlUrl = new URL(url);
                urlQueue.put(crawlUrl);
        }
    }


    public void run() throws InterruptedException, ExecutionException, TimeoutException, IOException{
        while(true){
            try {
                final URL url = urlQueue.take();
                FutureTask<String> futureTask = new FutureTask(
                        () -> {
                            String html = "";
                            String line;
                            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                            InputStream inputStream = httpURLConnection.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                            while ((line = br.readLine()) != null) {
                                html += line;
                            }
                            br.close();
                            inputStream.close();
                            return html;
                        }
                );
                crawlerExecutor.execute(futureTask);

                String html = futureTask.get(5, TimeUnit.SECONDS);
                htmlMap.putIfAbsent(url,html);
            }catch(InterruptedException e){
                log.error("爬虫被中断");
                throw new InterruptedIOException();
            }
        }
    }


}
