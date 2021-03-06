package com.xuecheng.test.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.test.rabbitmq.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-17 19:23
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class Producer07_topics_springboot {
    @Autowired
    RabbitTemplate rabbitTemplate;

    //使用rabbitTemplate发送消息
    @Test
    public void testSendEmail(){

        String message = "send email message to user";
        /**
         * 参数：
         * 1、交换机名称
         * 2、routingKey
         * 3、消息内容
         */
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPIC,"CMS.EMAIL",message);
    }
    @Test
    public void testSendSms(){
        String message = "send SMS message to user";
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPIC,"CMS.SMS",message);
    }

    @Test
    public void testSendSmsAndEmail(){
        String message = "send SMS AND EMAIL message to user";
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPIC,"CMS.SMS.EMAIL",message);
    }
}
