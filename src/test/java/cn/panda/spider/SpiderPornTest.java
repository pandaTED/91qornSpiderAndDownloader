package cn.panda.spider;


import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.downloadutil.DownloadThreadPool;
import cn.panda.spider.downloadutil.VideoDownloader;
import cn.panda.spider.entity.Porn91;
import cn.panda.spider.spider.SpiderFor91;
import cn.panda.spider.spider.SpiderFor91DetailPage;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class SpiderPornTest {

    @Resource
    SpiderFor91 spiderFor91;

    @Resource
    Porn91Dao porn91Dao;

    @Resource
    SpiderFor91DetailPage spiderFor91DetailPage;

    @Resource
    DownloadThreadPool downloadThreadPool;

    //TODO
    /**
     * 爬取91qorn网站所有视频的网页链接等信息入口
     */
    @Test
    public void test1(){

        //加入代理支持
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1",2080)));

        //创建爬虫spider类
        Spider spider = Spider.create(spiderFor91);

        //将当前类传入到spiderFor91中
        spiderFor91.setSpider(spider);

        //开始运行，如果1分钟内没有新的视频获取到，将自动停止爬虫
        spider.addUrl("http://91porn.com/v.php?next=watch&page=1").
                setScheduler(new QueueScheduler()).
                setDownloader(httpClientDownloader).
                thread(5).
                run();
    }


    //TODO
    /**
     * 下载视频入口
     *
     */
    @Test
    public void test3(){

        //修改cn.panda.spider.dao.Porn91Dao.getAllAndOrderByChakanDesc，获取不同类型的视频链接
        List<String> urlList = porn91Dao.getAllAndOrderByChakanDesc();

        //加入代理支持
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
//        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1",2080)));

        System.out.println("===================================");
        System.out.println("满足条件的视频：===>"+urlList.size());
        System.out.println("===================================");

        spiderFor91DetailPage.setTargetList(urlList);

       Spider.create(spiderFor91DetailPage).
                addUrl(urlList.get(0)).
                setScheduler(new QueueScheduler()).
                setDownloader(httpClientDownloader).
                thread(1).run();


    }


    /**
     * 使用Phantom来获取视频链接
     */
    @Test
    public void test5() throws InterruptedException {

        List<Porn91> toBeDownload = porn91Dao.getToBeDownload();
        int size = toBeDownload.size();
        log.info("需要下载的视频个数================>{}",size);

        //手动创建线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("video-get-%d").build();

        ExecutorService executorService = new ThreadPoolExecutor(12,
                24,
                10L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                namedThreadFactory);


//        ExecutorService executorService = Executors.newFixedThreadPool(10);


        for (Porn91 porn91 : toBeDownload) {
            VideoGet videoGet = new VideoGet(porn91);
            executorService.submit(videoGet);
        }

        try {
            executorService.awaitTermination(3000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        executorService.shutdown();

    }


    /**
     * 手动下载视频
     * @throws InterruptedException
     */
    @Test
    public void test4() {

        List<Porn91> videoSourceLinkList = porn91Dao.getVideoSourceLink();

        //手动创建线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("video-downloader-thread-%d").build();

//        ExecutorService executorService = Executors.newFixedThreadPool(300);
        ExecutorService executorService = new ThreadPoolExecutor(10,
                24,
                10L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                namedThreadFactory);

        Porn91 porn91 = null;

        for (int i = 0; i < videoSourceLinkList.size(); i++) {
            porn91 = videoSourceLinkList.get(i);
            executorService.execute(new VideoDownloader(porn91.getVideoSource(),porn91.getTitleXpath(),porn91Dao));
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(3000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void test6(){

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {

            WebTask webTask = new WebTask();
            executorService.submit(webTask);
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(1000,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    class WebTask implements Runnable{

        @Override
        public void run() {

            String chromeDriverPath = "E:\\chromedriver.exe";
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors", "--silent");
            WebDriver driver = new ChromeDriver(options);

            driver.get("http://www.baidu.com");

            driver.manage().timeouts().implicitlyWait(1000*5,TimeUnit.SECONDS);

            String title = driver.getTitle();

            System.out.println("title===============>"+title);
        }
    }


}
