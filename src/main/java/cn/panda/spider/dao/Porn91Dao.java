package cn.panda.springspider.dao;

import cn.panda.springspider.entity.Porn91;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Porn91Dao extends JpaRepository<Porn91,Long> {

    @Query(nativeQuery = true,value = "SELECT video_link FROM porn91 WHERE title_xpath LIKE '%眼镜%' OR title_xpath LIKE '%露脸%' ORDER BY shoucang*1 DESC LIMIT 100,1000")
    List<String> getAllAndOrderByChakanDesc();
}
