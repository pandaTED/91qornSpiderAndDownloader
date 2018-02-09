package cn.panda.spider.downloadutil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ZhuYunpeng
 * woscaizi@gmail.com
 * 2018/2/4
 */
@Component
public class DownloadThreadPool {

    Logger logger = Logger.getLogger(getClass());

    //创建固定量的线程池
        //后期修改为手动创建线程池
            //TODO
    ExecutorService executorService;

    @PostConstruct
    void init(){
        if(executorService == null){
            executorService = Executors.newFixedThreadPool(50);
        }
    }

    /**
     * 将线程放入线程池
     */
    public void execute(VideoDownloader videoDownloader){

      executorService.execute(videoDownloader);
//      executorService.shutdown(); //关闭线程池
    }

}
