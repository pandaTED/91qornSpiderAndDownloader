package cn.panda.spider.spider;

import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.downloadutil.DownloadThreadPool;
import cn.panda.spider.downloadutil.VideoDownloader;
import cn.panda.spider.entity.Porn91;
import cn.panda.spider.overall.Overall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuYunpeng
 * woscaizi@gmail.com
 * 2018/1/30
 */
@Component
public class SpiderFor91DetailPage implements PageProcessor {


    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    Porn91Dao porn91Dao;

    /**
     * 全局工具，主要是分批保存视频源链接
     */
    @Resource
    Overall overall;

    /**
     * 线程池
     */
    @Resource
    DownloadThreadPool downloadThreadPool;

    private List<String> targetList;

    public void setTargetList(List<String> targetList) {
        this.targetList = targetList;
    }

    @Bean
    SpiderFor91DetailPage getSpiderSingle(){
        return new SpiderFor91DetailPage();
    }

    //TODO
    // 改为模拟登陆获取这些信息？？
    private Site site = Site.me().
            setDomain("91porn.com").
            addCookie("language","cn_CN").
            setSleepTime(30*1000).
            setTimeOut(10000);

    @Override
    public Site getSite() {
        return site;
    }


    @Override
    public void process(Page page) {

       String vedioUrl =  page.getHtml().xpath("//div/video/source/@src").toString();
       String name = page.getHtml().xpath("//*[@id=\"viewvideo-title\"]/text()").toString();
       String isMissing = "//*[@id=\"container\"]/div";

        System.out.println("============================");
        System.out.println(page.getHtml().toString());
        System.out.println("============================");

        //添加目标链接
       page.addTargetRequests(targetList);
        int statusCode = page.getStatusCode();
        logger.info("statusCode=============>{}",statusCode);

        if(null != vedioUrl){


            logger.info("Overall===========================>"+overall);

            logger.info("====================");
            logger.info("spider===================="+vedioUrl);
            logger.info("name===================="+name);
            logger.info("====================");


            try {
                Porn91 porn91 = porn91Dao.getByVideoLink(page.getUrl().toString());

                porn91.setVideoLink(page.getUrl().toString());
                porn91.setVideoSource(vedioUrl);

                overall.saveVideoSource(porn91);

//              porn91Dao.save(porn91);     //保存视频下载链接
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                //修改为线程池的方式
                  //TODO
                VideoDownloader videoDownloader = new VideoDownloader(vedioUrl,name);
                downloadThreadPool.execute(videoDownloader);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{

            logger.info("===============>视频链接是空了！！！！！");
            Overall.failTimes++;

        }
    }


}
