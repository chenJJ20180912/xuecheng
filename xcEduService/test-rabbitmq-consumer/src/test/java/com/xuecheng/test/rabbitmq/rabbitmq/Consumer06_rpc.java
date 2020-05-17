package com.xuecheng.test.rabbitmq.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/14 9:25
 * @desc 生产者 helloWorld
 */
public class Consumer06_rpc {

    private static final String QUEUE_SEND = "QUEUE_SEND";
    private static final String QUEUE_RECEIVE = "QUEUE_RECEIVE";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_SEND, true, false, false, null);
        channel.queueDeclare(QUEUE_RECEIVE, true, false, false, null);
        // 生产者发送消息给QUEUE_SEND，对于消费者而言就是监听这个队列
        channel.basicConsume(QUEUE_SEND, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 调用本地服务
                channel.basicPublish("", QUEUE_RECEIVE, null, add(new String(body, "utf-8")).getBytes());
            }
        });
        // 不能关闭通道和连接
        //ActiveMQUtils.close();
    }

    public static String add(String param) {
        String[] split = param.split(",");
        System.out.println(String.format("params:%s,%s",split));
        return (Integer.valueOf(split[0]) + Integer.valueOf(split[1])) + "";
    }
}
