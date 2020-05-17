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
public class Producer03_routing {


    private static final String EXCHANGE_DIRECT = "CMS_EXCHANGE_DIRECT";

    private static final String QUEUE_SMS = "QUEUE_SMS";
    private static final String QUEUE_EMAIL = "QUEUE_EMAIL";

    private static final String ROUTING_KEY_SMS = "CMS.SMS";
    private static final String ROUTING_KEY_EMAIL = "CMS.EMAIL";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_SMS, true, false, false, null);
        channel.queueDeclare(QUEUE_EMAIL, true, false, false, null);

        // 声明路由器（路由模式）
        channel.exchangeDeclare(EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT);

        // 绑定交换机和队列
        channel.queueBind(QUEUE_SMS, EXCHANGE_DIRECT, ROUTING_KEY_SMS);
        channel.queueBind(QUEUE_EMAIL, EXCHANGE_DIRECT, ROUTING_KEY_EMAIL);
        for (int i = 0; i < 5; i++) {
            String msg = "Producer03_routing(" + System.currentTimeMillis() + ")";
            String routingKey = ROUTING_KEY_SMS;
            if (i % 2 == 0) {
                routingKey = ROUTING_KEY_EMAIL;
            }
            msg = routingKey + ":" + msg;
            channel.basicPublish(EXCHANGE_DIRECT, routingKey, null, msg.getBytes());
        }
        ActiveMQUtils.close();
    }
}
