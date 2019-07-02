package cn.panda.spider.spider;
import java.util.Date;

import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.entity.Porn91;
import cn.panda.spider.overall.Overall;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.annotation.PostConstruct;
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

    List<String> existVideoLink;

    Spider spider;

    public void setSpider(Spider spider) {
        this.spider = spider;
    }

    @PostConstruct
    void init(){
        existVideoLink = porn91Dao.getAllExistVideoLink();
    }


    @Override
    public void process(Page page) {

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

            String videoLinkString = page.getHtml().xpath(String.format(videoLink,i)).toString().replace("&viewtype=basic&category=mr","").replaceAll(ragex,"");

            if(!existVideoLink.contains(videoLinkString)){


                String videoLinkStr = page.getHtml().xpath(String.format(videoLink, i)).toString().replace("&viewtype=basic&category=mr","").replaceAll(ragex,"");
                String titleXpathStr = page.getHtml().xpath(String.format(titleXpath, i)).toString();
                String imgUrlStr = page.getHtml().xpath(String.format(imgUlr, i)).toString();
                String zuozheLinkStr = page.getHtml().xpath(String.format(zuozheLink, i)).toString();
                String zuozheNameStr = page.getHtml().xpath(String.format(zuozheName, i)).toString();
                String shijianStr = page.getHtml().xpath(String.format(shijian, i)).toString();
                String tianjiashijianStr = page.getHtml().xpath(String.format(tianjiashijian, i)).toString().replace(" ", "");
                String chakanStr = page.getHtml().xpath(String.format(chakan, i)).toString().replace(" ", "");
                String shoucangStr = page.getHtml().xpath(String.format(shoucang, i)).toString().replace(" ", "");
                String liuyanStr = page.getHtml().xpath(String.format(liuyan, i)).toString().replace(" ", "");
                String jifenStr = page.getHtml().xpath(String.format(jifen, i)).toString().replace(" ", "");


                System.out.println("=================新增========================");

                System.out.println(videoLinkStr);
                System.out.println(titleXpathStr);
                System.out.println(imgUrlStr);
                System.out.println(zuozheLinkStr);
                System.out.println(zuozheNameStr);
                System.out.println(shijianStr);
                System.out.println(tianjiashijianStr);
                System.out.println(chakanStr);
                System.out.println(shoucangStr);
                System.out.println(liuyanStr);
                System.out.println(jifenStr);

                System.out.println("=================新增========================");

                Porn91 porn91 = new Porn91();
                porn91.setVideoLink(videoLinkStr);
                porn91.setTitleXpath(titleXpathStr);
                porn91.setImgUlr(imgUrlStr);
                porn91.setZuozheLink(zuozheLinkStr);
                porn91.setZuozheName(zuozheNameStr);
                porn91.setShijian(shijianStr);
                porn91.setTianjiashijian(tianjiashijianStr);
                porn91.setChakan(chakanStr);
                porn91.setShoucang(shoucangStr);
                porn91.setLiuyan(liuyanStr);
                porn91.setJifen(jifenStr);

                porn91List.add(porn91);

            }

        }

        try{

            System.out.println("===================");
            System.out.println("lastSaveTimeStamp========>"+Overall.lastSaveTimeStamp);
            System.out.println("===================");

            //一分钟内没有新的视频抓取到，爬虫自动停止
            if((System.currentTimeMillis() - Overall.lastSaveTimeStamp) > 1000*60){
                  spider.stop();
            }

            if(porn91List.size() != 0){
                porn91Dao.save(porn91List);
                Overall.lastSaveTimeStamp = System.currentTimeMillis();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public Site getSite() {
        return site;
    }


}
