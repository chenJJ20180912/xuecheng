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
public class Consumer03_routing_email {


    private static final String EXCHANGE_DIRECT = "CMS_EXCHANGE_DIRECT";

    private static final String QUEUE_EMAIL = "QUEUE_EMAIL";

    private static final String ROUTING_KEY_EMAIL = "CMS.EMAIL";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_EMAIL, true, false, false, null);
        channel.exchangeDeclare(EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT);
        channel.queueBind(QUEUE_EMAIL,EXCHANGE_DIRECT,ROUTING_KEY_EMAIL);
        DefaultConsumer callback = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Receive:"+new String(body,"utf-8"));
            }
        };
        channel.basicConsume(QUEUE_EMAIL, true, callback);
    }
}