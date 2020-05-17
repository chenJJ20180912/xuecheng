package com.xuecheng.test.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    public static final String EXCHANGE_TOPIC = "CMS_EXCHANGE_TOPIC";
    public static final String QUEUE_SMS = "QUEUE_SMS";
    public static final String QUEUE_EMAIL = "QUEUE_EMAIL";

    // 构建交换机
    @Bean
    public Exchange EXCHANGE_TOPIC() {
        // durable(true) 持久化 消息队列重启后交换机依旧保留
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPIC).durable(true).build();
    }

    // 构建队列
    @Bean
    public Queue QUEUE_SMS() {
        return new Queue(QUEUE_SMS);
    }

    // 构建队列
    @Bean
    public Queue QUEUE_EMAIL() {
        return new Queue(QUEUE_EMAIL);
    }

    // 绑定交换机和队列
    @Bean
    public Binding bindQueue_SMS(@Qualifier("QUEUE_SMS") Queue queue, @Qualifier("EXCHANGE_TOPIC") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("CMS.#.SMS.#").noargs();
    }

    //绑定交换机和队列
    @Bean
    public Binding bindQueue_EMAIL(@Qualifier("QUEUE_EMAIL") Queue queue, @Qualifier("EXCHANGE_TOPIC") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("CMS.#.EMAIL.#").noargs();
    }
}
