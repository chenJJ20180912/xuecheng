package com.xuecheng.manage_cms.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 生产者其实在生产过程中是不知道具体有哪些消费者的
 * 所以在生产者的程序中只用设置交换机就可以了，
 * 如果消费者在生产者启动之前启动,生产者中的交换机也不用定义
 */
@Configuration
public class RabbitmqConfig {

    public static final String EXCHANGE_CMS_PAGE_POST = "EXCHANGE_CMS_PAGE_POST";
    public static final String QUEUE_CMS_PAGE_POST = "QUEUE_CMS_PAGE_POST";

    // 构建交换机
    @Bean
    public Exchange EXCHANGE_CMS_PAGE_POST() {
        // durable(true) 持久化 消息队列重启后交换机依旧保留
        return ExchangeBuilder.directExchange(EXCHANGE_CMS_PAGE_POST).durable(true).build();
    }

    // 构建队列
    @Bean
    public Queue QUEUE_CMS_PAGE_POST() {
        return QueueBuilder.durable(QUEUE_CMS_PAGE_POST).build();
    }

    @Bean
    public Binding bindSend(@Qualifier("EXCHANGE_CMS_PAGE_POST") Exchange exchange,
                            @Qualifier("QUEUE_CMS_PAGE_POST") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_CMS_PAGE_POST).noargs();
    }
}
