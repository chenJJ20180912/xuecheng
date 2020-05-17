package com.xuecheng.test.rabbitmq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/14 9:25
 * @desc 生产者 helloWorld
 */
public class Producer01 {

    private static final String QUEUE = "helloWorld";

    public static void main(String[] args) {
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
        try {
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
            channel.queueDeclare(QUEUE,true,false,false,null);


            // 通过channel发送消息
            // String exchange, String routingKey, BasicProperties props, byte[] body
            /**
             * exchange     交换机  没有的话设置为""
             * routingKey   路由键  不似乎用交换机的时候路由键需要设置为队列名称
             * props        消息的属性，一般不设置
             * body         消息体
             */
            String msg = QUEUE + "("+System.currentTimeMillis()+")";
            channel.basicPublish("",QUEUE,null,msg.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
