package cn.panda.spider.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * @author ZhuYunpeng
 * woscaizi@gmail.com
 * 2018/2/23
 * 未完成//TODO
 */
public class TumblrSpider implements PageProcessor{


    private Site site = Site.me().
            setDomain("www.tumblr.com").
            addCookie("anon_id","LVCPRWVYQRKSUTWIULSTYRWCTXWDAKBI").
            addCookie("devicePixelRatio","1").
            addCookie("documentWidth","1567").
            addCookie("language","%2Czh_CN").
            addCookie("logged_in","1").
            addCookie("nts","false").
            addCookie("pfe","1527385149").
            addCookie("pfl","ZDVlMTMzMDNlNzRjM2RjNTcxYjNmYjk0MzZlMmNhZjMzYzY4Y2Y1MDAxN2QyYjI0MWIxMzM0YjU5OGI3MTQ0ZCxzdTR0bnpuNHpkN2Yybmt1ZmZoN2VkdHgzZDQ2dzd2eiwxNTE5NjA5MTM4").
            addCookie("pfp","TrLqGIGXPJU6SCoyMlFa8r9wFMTKEa2UqYmB8kq6").
            addCookie("pfs","2ViGADOoFIe2ottWdEdQyZGrfg").
            addCookie("pfu","199145436").
            addCookie("pfx","185aa93ef4cd4f52e42d9bc5625a26d7ff21b5f9c4772f7c2ceef9b27593a79e%230%237320107726").
            addCookie("rxx","8jq658vli5c.11b9fzse&v=1").
            addCookie("tmgioct","5a93652c7d6e010997074630").
            setRetryTimes(3).
            setSleepTime(1000).
            setTimeOut(10000);

    @Override
    public void process(Page page) {


        ////video//source/src()

        String videoSourceXpath = "//video//source";

        System.out.println("===============================");
        System.out.println("===============================");
        System.out.println(page.getHtml().xpath(videoSourceXpath));
        System.out.println("===============================");
        System.out.println("===============================");


    }

    @Override
    public Site getSite() {
        return site;
    }


    public static void main(String[] args) {


        //加入代理支持
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1",1080)));

        Spider.create(new TumblrSpider()).
                addUrl("https://www.tumblr.com/likes").
                setScheduler(new QueueScheduler()).
                setDownloader(httpClientDownloader).
                thread(5).
                run();


    }


}
