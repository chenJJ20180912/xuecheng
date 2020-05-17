package com.xuecheng.test.rabbitmq.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/14 9:25
 * @desc
 */
public class Consumer05_header_email {

    private static final String EXCHANGE_HEADERS = "CMS_EXCHANGE_HEADERS";

    private static final String QUEUE_EMAIL = "QUEUE_EMAIL";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_EMAIL, true, false, false, null);
        Map<String,Object> header_email = new HashMap<String,Object>();
        header_email.put("type",QUEUE_EMAIL);
        channel.exchangeDeclare(EXCHANGE_HEADERS, BuiltinExchangeType.HEADERS);
        channel.queueBind(QUEUE_EMAIL,EXCHANGE_HEADERS,"",header_email);
        DefaultConsumer callback = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Receive:"+new String(body,"utf-8"));
            }
        };
        channel.basicConsume(QUEUE_EMAIL, true, callback);
    }
}
