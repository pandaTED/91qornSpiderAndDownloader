package cn.panda.spider;

import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.downloadutil.DownloadThreadPool;
import cn.panda.spider.downloadutil.VideoDownloader;
import cn.panda.spider.entity.Porn91;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class VideoGet{

    @Resource
    Porn91Dao porn91Dao;

    private String url;

    private String title;

    private Porn91 porn91;

    public void setPorn91(Porn91 porn91) {
        this.porn91 = porn91;
        if(null != porn91){
            if(StringUtils.isNotBlank(porn91.getVideoLink())){
                    this.url = porn91.getVideoLink();
            }

            if(StringUtils.isNotBlank(porn91.getTitleXpath())){
                    this.title = porn91.getTitleXpath();
            }
        }
    }

    @Resource
    DownloadThreadPool downloadThreadPool;

    @Resource
    BrowserDriverBuild browserDriverBuild;

    public void run() {

        WebDriver driver = browserDriverBuild.getDriver();

        // Get the login page
        driver.get(url);

        WebElement source = null;
        try {
            source = driver.findElement(By.tagName("source"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(null != source){

            String src = null;
            try {
                src = source.getAttribute("src");
            } catch (Exception e) {
                e.printStackTrace();
            }

//        String pageSource = driver.getPageSource();

            if(null != src){
                log.info("================开始===========================");
                log.info(src);
                log.info("================结束===========================");
                VideoDownloader videoDownloader = new VideoDownloader(src,title);
                downloadThreadPool.execute(videoDownloader);

                porn91.setVideoSource(src);
                porn91.setDownloadTime(new Date());

                porn91Dao.save(porn91);
            }
        }

        driver.close();
    }
}
