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
public class Consumer01 {
    private static final String QUEUE = "helloWorld";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);
        // 设置虚拟机，RabbitMQ中存在虚拟机的概念，
        // 如果需要启动多个rabbitMQ只用配置多个虚拟机就可以，不用安装多个RabbitMQ服务
        factory.setVirtualHost("/");
        Connection connection = null;
        Channel channel = null;
        // 获取连接
        connection = factory.newConnection();
        // 获取通道
        channel = connection.createChannel();

        //定义队列
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        /**
         * queue        队列名称
         * durable      是否持久化(MQ服务器关闭,没有被读取的消息不丢失)
         * exclusive    排外，设置为true的话在当前connection中只允许当前通道可以读取到消息
         * autoDelete   自动删除，当设置为true后会自动删除队列
         * arguments    设置队列的一些拓展参数，比如超时等 这些参数都是RabbitMQ提供的
         */
        channel.queueDeclare(QUEUE, true, false, false, null);


        // 通过通道接收消息 String queue, Consumer callback
        /**
         * queue 队列名称
         * callback 接收到消息之后的处理方案
         */
        DefaultConsumer callback = new DefaultConsumer(channel) {
            /**
             *
             * @param consumerTag 消息的id
             * @param envelope    信封(对路由键 交换机等信息的封装)
             * @param properties  发送的时候设置的消息属性
             * @param body        消息体
             * @throws IOException
             */
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
        /**
         * String queue, boolean autoAck, Consumer callback
         * queue 队列
         * autoAck 自动回复
         * callback 对消息的监听处理
         */
        channel.basicConsume(QUEUE, true,callback);
    }
}
