package com.xuecheng.test.rabbitmq.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/14 9:25
 * @desc
 */
public class Consumer04_topic_sms {


    private static final String EXCHANGE_TOPIC = "CMS_EXCHANGE_TOPIC";

    private static final String QUEUE_SMS = "QUEUE_SMS";

    private static final String ROUTING_KEY_SMS = "CMS.#.SMS.#";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_SMS, true, false, false, null);
        channel.exchangeDeclare(EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);
        channel.queueBind(QUEUE_SMS,EXCHANGE_TOPIC,ROUTING_KEY_SMS);
        DefaultConsumer callback = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Receive:"+new String(body,"utf-8"));
            }
        };
        channel.basicConsume(QUEUE_SMS, true, callback);
    }
}
