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
public class Consumer05_header_sms {


    private static final String EXCHANGE_HEADERS = "CMS_EXCHANGE_HEADERS";

    private static final String QUEUE_SMS = "QUEUE_SMS";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_SMS, true, false, false, null);
        Map<String,Object> header_sms = new HashMap<String,Object>();
        header_sms.put("type",QUEUE_SMS);
        channel.exchangeDeclare(EXCHANGE_HEADERS, BuiltinExchangeType.HEADERS);
        channel.queueBind(QUEUE_SMS,EXCHANGE_HEADERS,"",header_sms);
        DefaultConsumer callback = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Receive:"+new String(body,"utf-8"));
            }
        };
        channel.basicConsume(QUEUE_SMS, true, callback);
    }
}
