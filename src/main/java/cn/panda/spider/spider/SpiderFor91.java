package cn.panda.spider.spider;

import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.entity.Porn91;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpiderFor91 implements PageProcessor{

    @Resource
    Porn91Dao porn91Dao;

    private Site site = Site.me().
            setDomain("91porn.com").
            addCookie("language","cn_CN").
            setRetryTimes(3).
            setSleepTime(1000).
            setTimeOut(10000);

    String ragex = "&page=\\d+";

    String listUrl = "http://91porn\\.com/v\\.php\\?next=watch&page=\\d+";

    @Override
    public void process(Page page) {

        Porn91 porn91 = null;

        page.addTargetRequests(page.getHtml().links().regex(listUrl).all());

        String videoLink = "//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/div[1]/a/@href";
        String titleXpath = "//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/a[1]/@title";
        String imgUlr = "//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/div[1]/a/img/@src";
        String zuozheLink = "//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/a[2]/@href";
        String zuozheName = "//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/a[2]/text()";
        String shijian ="//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/text(5)";
        String tianjiashijian ="//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/text(7)";
        String chakan ="//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/text(11)";
        String shoucang ="//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/text(12)";
        String liuyan ="//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/text(14)";
        String jifen ="//*[@id=\"videobox\"]/table/tbody/tr/td/div[%s]/text(15)";


        List<Porn91> porn91List = new ArrayList<>();

        for(int i =1;i<=20;i++){

            System.out.println("=========================================");
            System.out.println(page.getHtml().xpath(String.format(videoLink,i)));
            System.out.println(page.getHtml().xpath(String.format(titleXpath,i)));
            System.out.println(page.getHtml().xpath(String.format(imgUlr,i)));
            System.out.println(page.getHtml().xpath(String.format(zuozheLink,i)));
            System.out.println(page.getHtml().xpath(String.format(zuozheName,i)));
            System.out.println(page.getHtml().xpath(String.format(shijian,i)));
            System.out.println(page.getHtml().xpath(String.format(tianjiashijian,i)).toString().replace(" ",""));
            System.out.println(page.getHtml().xpath(String.format(chakan,i)).toString().replace(" ",""));
            System.out.println(page.getHtml().xpath(String.format(shoucang,i)).toString().replace(" ",""));
            System.out.println(page.getHtml().xpath(String.format(liuyan,i)).toString().replace(" ",""));
            System.out.println(page.getHtml().xpath(String.format(jifen,i)).toString().replace(" ",""));
            System.out.println("=========================================");

            porn91 = new Porn91();
            porn91.setImgUlr(page.getHtml().xpath(String.format(imgUlr,i)).toString());
            porn91.setChakan(page.getHtml().xpath(String.format(chakan,i)).toString().replace(" ",""));
            porn91.setJifen(page.getHtml().xpath(String.format(jifen,i)).toString().replace(" ",""));
            porn91.setLiuyan(page.getHtml().xpath(String.format(liuyan,i)).toString().replace(" ",""));
            porn91.setShijian(page.getHtml().xpath(String.format(shijian,i)).toString());
            porn91.setShoucang(page.getHtml().xpath(String.format(shoucang,i)).toString().replace(" ",""));
            porn91.setTianjiashijian(page.getHtml().xpath(String.format(tianjiashijian,i)).toString().replace(" ",""));
            porn91.setTitleXpath(page.getHtml().xpath(String.format(titleXpath,i)).toString());
            porn91.setVideoLink(page.getHtml().xpath(String.format(videoLink,i)).toString().replace("&viewtype=basic&category=mr","").replaceAll(ragex,""));
            porn91.setZuozheName(page.getHtml().xpath(String.format(zuozheName,i)).toString());
            porn91.setZuozheLink(page.getHtml().xpath(String.format(zuozheLink,i)).toString());

            porn91List.add(porn91);
        }

        try{
            porn91Dao.save(porn91List);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Site getSite() {
        return site;
    }


}
