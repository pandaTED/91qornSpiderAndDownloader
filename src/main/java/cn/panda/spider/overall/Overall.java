package cn.panda.spider.overall;

import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.entity.Porn91;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuYunpeng
 * woscaizi@gmail.com
 * 2018/2/4
 */

@Component
public class Overall {

     volatile static List<Porn91> videoSourceList = new ArrayList<>();

    @Resource
    Porn91Dao porn91Dao;

    Logger logger = Logger.getLogger(getClass());


    /**
     * 向List中添加videoLink并返回当前List的大小
     * @param porn91
     * @return
     */
    private Integer addVideoSource(Porn91 porn91){
        synchronized (videoSourceList){
            videoSourceList.add(porn91);
            return videoSourceList.size();
        }

    }


    /**
     * 当videoSourceList大小达到100时，将List分割，并把之前的100个保存到数据库
     * @param size 当前 videoSourceList的大小
     */
    private void saveToMysqlVideoSource(Integer size){

//      Integer num = 1;
        List<Porn91> toSaveList = null;

        logger.info("源视频List大小为====>"+size);

        synchronized (videoSourceList){

            if(size > 20){

                //分割List
//            num = (int)Math.ceil(size / 100D) ;
                toSaveList = videoSourceList.subList(0,20);
                //保存

                try {
                    porn91Dao.save(toSaveList);
                    logger.info("视频源地址保存到mysql成功");

                } catch (Exception e) {
                    logger.info("视频源地址保存到mysql失败===================>");
                    e.printStackTrace();
                }

                //移除已保存
                toSaveList.forEach(e->videoSourceList.remove(e));

            }

        }

    }


    /**
     * 保存视频源链接
     * @param porn91
     * @return
     */
    public void saveVideoSource(Porn91 porn91){

        synchronized (videoSourceList){

            Integer size = addVideoSource(porn91);

            try {
                saveToMysqlVideoSource(size);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }



}
