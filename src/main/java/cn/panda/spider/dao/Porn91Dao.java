package cn.panda.spider.dao;

import cn.panda.spider.entity.Porn91;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Porn91Dao extends JpaRepository<Porn91,Long> {


    /**
     * 将要下载的视频网页链接获取出来
     * @return
     */
    @Query(nativeQuery = true,value = "SELECT video_link FROM porn91 WHERE title_xpath LIKE '%眼镜%' OR title_xpath LIKE '%露脸%' ORDER BY shoucang*1 DESC LIMIT 0,2000")
    List<String> getAllAndOrderByChakanDesc();

    Porn91 getByVideoLink(String url);
}
