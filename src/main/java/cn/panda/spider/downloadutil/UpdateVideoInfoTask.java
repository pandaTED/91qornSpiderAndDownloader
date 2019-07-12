package cn.panda.spider.downloadutil;

import cn.panda.spider.dao.Porn91Dao;
import cn.panda.spider.entity.Porn91;
import cn.panda.spider.util.BeanUtil;

public class UpdateVideoInfoTask implements  Runnable{

    private Porn91 porn91;

    public UpdateVideoInfoTask(Porn91 porn91) {
        this.porn91 = porn91;
    }

    @Override
    public void run() {

        Thread.currentThread().setName("UpdateVideoInfoTask-"+System.currentTimeMillis());

        Porn91Dao porn91Dao = BeanUtil.getBean(Porn91Dao.class);

        try {
            porn91Dao.save(porn91);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
