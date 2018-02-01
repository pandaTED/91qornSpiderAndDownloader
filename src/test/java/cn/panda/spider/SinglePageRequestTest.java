package cn.panda.springspider;


import org.apache.http.HttpHeaders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SinglePageRequestTest {


        @Test
        public void test1(){


            final String uri = "http://91porn.com/view_video.php?viewkey=6e43e730a8febe1c6315&page=697&viewtype=basic&category=mr";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity responseEntity = restTemplate.getForEntity(uri,Object.class);

            System.out.println(responseEntity);

        }





}
