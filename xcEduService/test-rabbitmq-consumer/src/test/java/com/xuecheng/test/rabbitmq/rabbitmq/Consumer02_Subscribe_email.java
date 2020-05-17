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
public class Consumer02_Subscribe_email {

    private static final String EXCHANGE_FANOUT = "CMS_EXCHANGE_FANOUT";

    private static final String QUEUE_EMAIL = "QUEUE_EMAIL";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_EMAIL, true, false, false, null);
        channel.exchangeDeclare(EXCHANGE_FANOUT, BuiltinExchangeType.FANOUT);
        channel.queueBind(QUEUE_EMAIL,EXCHANGE_FANOUT,"");
        DefaultConsumer callback = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String exchange = envelope.getExchange();
                String routingKey = envelope.getRoutingKey();
                System.out.println(String.format("consumerTag:%s\texchange:%s\troutingKey:%s\t",
                        consumerTag,exchange,routingKey));
                System.out.println("Receive:"+new String(body,"utf-8"));
            }
        };
        channel.basicConsume(QUEUE_EMAIL,true, callback);
    }
}
