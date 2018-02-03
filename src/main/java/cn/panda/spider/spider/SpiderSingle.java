package cn.panda.spider.spider;

import cn.panda.downloader.Utils.VideoDownloader;
import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.entity.Porn91;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author ZhuYunpeng
 * woscaizi@gmail.com
 * 2018/1/30
 */
@Component
public class SpiderSingle implements PageProcessor {


    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    Porn91Dao porn91Dao;

    private List<String> targetList;

    public void setTargetList(List<String> targetList) {
        this.targetList = targetList;
    }

    @Bean
    SpiderSingle getSpiderSingle(){
        return new SpiderSingle();
    }

    private Site site = Site.me().
            setDomain("91porn.com").
            addCookie("language","cn_CN").
            addCookie("91username","woscaizi").     //请自己登录91根据实际填写
            addCookie("__cfduid","dd76563328fbdc0c75f7b747cec8299f41517663725").   //请自己登录91根据实际填写
            addCookie("CLIPSHARE","moc2laeofvvkqe0n9p84u7lv36").  //请自己登录91根据实际填写
            addCookie("DUID","1bceHNaZ3xC4SBgk7gWZ4VIJYn3F9W9aZWbaYIOYcL0bX9dD").
            addCookie("EMAILVERIFIED","no").
            addCookie("level","7").
            addCookie("user_level","7").
            addCookie("USERNAME","5aac56IPj0UEX%2FOfwC5c8%2B%2FtPE90SjprkvDxYaBxuv%2FK3ZcN6Q").   //请自己登录91根据实际填写
            setRetryTimes(3).
            setSleepTime(1000).
            setTimeOut(10000);

    @Override
    public Site getSite() {
        return site;
    }


    @Override
    public void process(Page page) {

       String vedioUrl =  page.getHtml().xpath("//div/video/source/@src").toString();
       String name = page.getHtml().xpath("//*[@id=\"viewvideo-title\"]/text()").toString();

//        System.out.println("============================");
//        System.out.println(page.getHtml().toString());
//        System.out.println("============================");


        //添加目标链接
       page.addTargetRequests(targetList);


       logger.info("vedioUrl====>"+vedioUrl);

        if(null != vedioUrl){

            logger.info("====================");
            logger.info("spider===================="+vedioUrl);
            logger.info("name===================="+name);
            logger.info("====================");


            try {
                Porn91 porn91 = porn91Dao.getByVideoLink(page.getUrl().toString());

                porn91.setVideoLink(page.getUrl().toString());
                porn91.setVideoSource(vedioUrl);
                porn91.setIsDownloaded(0);

                porn91Dao.save(porn91);     //保存视频下载链接
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                VideoDownloader videoDownloader = new VideoDownloader(vedioUrl,name);
                new Thread(videoDownloader).start();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

//
//    public void getVedio(String url, HttpClientDownloader httpClientDownloader){
//
//        Spider.create(getSpiderSingle()).addUrl(url).
//                setScheduler(new QueueScheduler()).
//                setDownloader(httpClientDownloader).
//                thread(1).
//                run();
//
//    }


}
