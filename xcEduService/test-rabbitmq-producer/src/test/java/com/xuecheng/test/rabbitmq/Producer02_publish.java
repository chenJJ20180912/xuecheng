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
public class Producer02_publish {


    private static final String EXCHANGE_FANOUT = "CMS_EXCHANGE_FANOUT";

    private static final String QUEUE_SMS = "QUEUE_SMS";
    private static final String QUEUE_EMAIL = "QUEUE_EMAIL";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_SMS, true, false, false, null);
        channel.queueDeclare(QUEUE_EMAIL, true, false, false, null);

        ///构建一个交换机
        /**
         * 参数
         *
         * BuiltinExchangeType 交换机的类型
         * FANOUT   发布订阅模式
         * DIRECT   路由模式
         * TOPIC    通配符模式
         * HEADERS  头信息匹配模式
         *
         */
        channel.exchangeDeclare(EXCHANGE_FANOUT, BuiltinExchangeType.FANOUT);

        // 绑定交换机和队列
        /**
         * String queue, String exchange, String routingKey
         * queue            队列名称
         * exchange         交换机名称
         * routingKey       路由键
         */
        channel.queueBind(QUEUE_SMS,EXCHANGE_FANOUT,"");
        channel.queueBind(QUEUE_EMAIL,EXCHANGE_FANOUT,"");
        // 通过channel发送消息
        // String exchange, String routingKey, BasicProperties props, byte[] body
        /**
         * exchange     交换机  设置为上面定义的交换机名称
         * routingKey   路由键  发布订阅模式的路由键设置为 ""
         * props        消息的属性，一般不设置
         * body         消息体
         */
        for(int i=0;i<5;i++){
            String msg = "Producer02_publish(" + System.currentTimeMillis() + ")";
            channel.basicPublish(EXCHANGE_FANOUT, "", null, msg.getBytes());
        }
        ActiveMQUtils.close();
    }
}
