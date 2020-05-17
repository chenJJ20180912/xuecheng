package com.xuecheng.test.rabbitmq.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.ActiveMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/14 9:25
 * @desc 生产者 helloWorld
 */
public class Producer04_topic {


    private static final String EXCHANGE_TOPIC = "CMS_EXCHANGE_TOPIC";

    private static final String QUEUE_SMS = "QUEUE_SMS";
    private static final String QUEUE_EMAIL = "QUEUE_EMAIL";
    // # 代表一个词或者多个词
    // * 代表一个词
    private static final String ROUTING_KEY_SMS = "CMS.#.SMS.#";
    private static final String ROUTING_KEY_EMAIL = "CMS.#.EMAIL.#";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_SMS, true, false, false, null);
        channel.queueDeclare(QUEUE_EMAIL, true, false, false, null);

        // 声明路由器（路由模式）
        channel.exchangeDeclare(EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);

        // 绑定交换机和队列
        channel.queueBind(QUEUE_SMS, EXCHANGE_TOPIC, ROUTING_KEY_SMS);
        channel.queueBind(QUEUE_EMAIL, EXCHANGE_TOPIC, ROUTING_KEY_EMAIL);
        for (int i = 0; i < 5; i++) {
            String msg = "Producer04_topic(" + System.currentTimeMillis() + ")";
            String routingKey = "CMS.EMAIL";
            msg = routingKey + ":" + msg;
            channel.basicPublish(EXCHANGE_TOPIC, routingKey, null, msg.getBytes());
        }
        ActiveMQUtils.close();
    }
}
