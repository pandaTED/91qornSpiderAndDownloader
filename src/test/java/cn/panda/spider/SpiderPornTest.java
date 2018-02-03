package cn.panda.spider;


import cn.panda.spider.dao.Porn91Dao;
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
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpiderPornTest {

    @Resource
    SpiderFor91 spiderFor91;

    @Resource
    Porn91Dao porn91Dao;

    @Resource
    SpiderSingle spiderSingle;


    //TODO
    /**
     * 爬取91qorn网站所有视频的网页链接等信息入口
     */
    @Test
    public void test1(){

        //加入代理支持
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1",1080)));

        Spider.create(spiderFor91).addUrl("http://91porn.com/v.php?next=watch&page=1").
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

//      urlList.forEach(e->spiderSingle.getVedio(e,httpClientDownloader));

        spiderSingle.setTargetList(urlList);

        Spider.create(spiderSingle).
                addUrl(urlList.get(0)).
                setScheduler(new QueueScheduler()).
                setDownloader(httpClientDownloader).
                thread(10).run();


    }


}
