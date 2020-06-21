package com.xuecheng.manage_cms.ribbon;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRibbon(){
//        String url = "http://XC-SERVICE-MANAGE-CMS/cms/page/list/1/10";
//        for(int i=0;i<10;i++) {
//            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
//            Map body = response.getBody();
//            System.out.println(body);
//        }
    }
}
