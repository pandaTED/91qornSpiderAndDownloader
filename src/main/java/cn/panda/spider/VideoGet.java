package cn.panda.spider;

import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.downloadutil.DownloadThreadPool;
import cn.panda.spider.downloadutil.VideoDownloader;
import cn.panda.spider.entity.Porn91;
import cn.panda.spider.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class VideoGet implements Runnable {


    private String url;

    private String title;

    private Porn91 porn91;


    public VideoGet(Porn91 porn91) {
        this.porn91 = porn91;
        if (null != porn91) {
            if (StringUtils.isNotBlank(porn91.getVideoLink())) {
                this.url = porn91.getVideoLink();
            }

            if (StringUtils.isNotBlank(porn91.getTitleXpath())) {
                this.title = porn91.getTitleXpath();
            }
        }
    }

    @Override
    public void run() {


        BrowserDriverBuild browserDriverBuild = new BrowserDriverBuild();

        WebDriver driver = browserDriverBuild.getDriver();

        System.out.println("driver=================>"+driver);

        if (null != driver) {

            System.out.println("title==============>"+title+",url=================>"+url);

            if (null != title && null != url) {

                try {
                    driver.get(url);
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                WebElement source = null;
                try {
                    source = driver.findElement(By.tagName("source"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (null != source) {

                    String src = null;
                    try {
                        src = source.getAttribute("src");
                        System.out.println("src================>"+src);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//        String pageSource = driver.getPageSource();

                    DownloadThreadPool downloadThreadPool = BeanUtil.getBean(DownloadThreadPool.class);

                    if (null != src) {
                        VideoDownloader videoDownloader = new VideoDownloader(src, title);
                        downloadThreadPool.execute(videoDownloader);

                        porn91.setVideoSource(src);
                        porn91.setDownloadTime(new Date());

                        Porn91Dao porn91Dao = BeanUtil.getBean(Porn91Dao.class);

                        porn91Dao.save(porn91);
                    }
                }


                try {
                    driver.quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {

                try {
                    driver.quit();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }
    }
}
