package com.xuecheng.test.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestConsumer {

    public static void main(String[] args) {
        SpringApplication.run(TestConsumer.class,args);
    }

}
