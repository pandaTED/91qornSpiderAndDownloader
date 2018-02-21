package cn.panda.spider;


import cn.panda.spider.downloadutil.DownloadThreadPool;
import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.downloadutil.VideoDownloader;
import cn.panda.spider.entity.Porn91;
import cn.panda.spider.spider.SpiderFor91;
import cn.panda.spider.spider.SpiderSingle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpiderPornTest {

    @Resource
    SpiderFor91 spiderFor91;

    @Resource
    Porn91Dao porn91Dao;

    @Resource
    SpiderSingle spiderSingle;

//    @Resource
//    VideoDownloaderPool videoDownloaderPool;

//    @Resource
//    DownloadThreadPool downloadThreadPool;


    //TODO
    /**
     * 爬取91qorn网站所有视频的网页链接等信息入口
     */
    @Test
    public void test1(){

        //加入代理支持
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1",1080)));

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


    @Test
    public void test2(){

        List<String> porn91List = porn91Dao.getAllAndOrderByChakanDesc();
        System.out.println(porn91List);
    }



    //TODO
    /**
     * 下载视频入口
     */
    @Test
    public void test3(){

        //修改cn.panda.spider.dao.Porn91Dao.getAllAndOrderByChakanDesc，获取不同类型的视频链接
        List<String> urlList = porn91Dao.getAllAndOrderByChakanDesc();

        //加入代理支持
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1",1080)));

        System.out.println("===================================");
        System.out.println("满足条件的视频：===>"+urlList.size());
        System.out.println("===================================");

        spiderSingle.setTargetList(urlList);

        Spider.create(spiderSingle).
                addUrl(urlList.get(0)).
                setScheduler(new QueueScheduler()).
                setDownloader(httpClientDownloader).
                thread(10).run();


    }

    @Test
    public void test4() throws InterruptedException {


        List<Porn91> videoSourceLinkList = porn91Dao.getVideoSourceLink();

        ExecutorService executorService = Executors.newFixedThreadPool(100);

//        System.out.println("videoSourceLinkList-Size======================>"+videoSourceLinkList.size());
//        videoSourceLinkList.forEach(e->executorService.execute(new VideoDownloader(e,String.valueOf(System.currentTimeMillis()+(int)(Math.random()*10)))));
//        videoSourceLinkList.forEach(e->executorService.execute(new VideoDownloader()));

        Porn91 porn91 = null;

        for (int i = 0; i < videoSourceLinkList.size(); i++) {

            porn91 = videoSourceLinkList.get(i);
            executorService.execute(new VideoDownloader(porn91.getVideoSource(),porn91.getTitleXpath()));

            Thread.sleep(1000*3);

        }

    }


}
