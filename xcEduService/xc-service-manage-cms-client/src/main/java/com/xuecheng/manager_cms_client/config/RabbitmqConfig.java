package com.xuecheng.manager_cms_client.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    public static final String EXCHANGE_CMS_PAGE_POST = "EXCHANGE_CMS_PAGE_POST";

    public static final String QUEUE_CMS_PAGE_POST = "QUEUE_CMS_PAGE_POST";

    @Value("${xuecheng.mq.receiveQueue}")
    private String RECEIVE_QUEUE;

    @Value("${xuecheng.mq.routingKey}")
    private String ROUTING_KET;

    // 构建交换机
    @Bean
    public Exchange EXCHANGE_CMS_PAGE_POST() {
        // durable(true) 持久化 消息队列重启后交换机依旧保留
        return ExchangeBuilder.directExchange(EXCHANGE_CMS_PAGE_POST).durable(true).build();
    }

    // 构建队列
    @Bean
    public Queue RECEIVE_QUEUE() {
        return QueueBuilder.durable(RECEIVE_QUEUE).build();
    }

    // 绑定交换机和队列
    @Bean
    public Binding bindReceive(@Qualifier("EXCHANGE_CMS_PAGE_POST") Exchange exchange,
                           @Qualifier("RECEIVE_QUEUE") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KET).noargs();
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
