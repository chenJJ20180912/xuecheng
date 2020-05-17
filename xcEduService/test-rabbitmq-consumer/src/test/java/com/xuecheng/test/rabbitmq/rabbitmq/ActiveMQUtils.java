package com.xuecheng.test.rabbitmq.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/14 16:21
 * @desc
 */
public class ActiveMQUtils {

    private static Connection connection;

    private static Channel channel;

    public static Channel newChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);
        // 如果需要启动多个rabbitMQ只用配置多个虚拟机就可以，不用安装多个RabbitMQ服务
        factory.setVirtualHost("/");
        connection = factory.newConnection();
        channel = connection.createChannel();
        return channel;
    }

    public static void close() {
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
