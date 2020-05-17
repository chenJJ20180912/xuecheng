package com.xuecheng.test.rabbitmq.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.ActiveMQUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/14 9:25
 * @desc 生产者 helloWorld
 */
public class Producer05_header {

    private static final String EXCHANGE_HEADERS = "CMS_EXCHANGE_HEADERS";

    private static final String QUEUE_SMS = "QUEUE_SMS";
    private static final String QUEUE_EMAIL = "QUEUE_EMAIL";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_SMS, true, false, false, null);
        channel.queueDeclare(QUEUE_EMAIL, true, false, false, null);

        // 声明路由器（header模式）
        channel.exchangeDeclare(EXCHANGE_HEADERS, BuiltinExchangeType.HEADERS);
        Map<String,Object> header_sms = new HashMap<String,Object>();
        header_sms.put("type",QUEUE_SMS);
        Map<String,Object> header_email = new HashMap<String,Object>();
        header_email.put("type",QUEUE_EMAIL);
        // 绑定交换机和队列
        channel.queueBind(QUEUE_SMS, EXCHANGE_HEADERS, "",header_sms);
        channel.queueBind(QUEUE_EMAIL, EXCHANGE_HEADERS, "",header_email);
        Map<String,Object> headers = new HashMap<String, Object>();
        headers.put("type",QUEUE_SMS);
        //headers.put("type",QUEUE_EMAIL);
        AMQP.BasicProperties.Builder propertiesBuilder = new AMQP.BasicProperties.Builder();
        propertiesBuilder.headers(headers);
        for (int i = 0; i < 5; i++) {
            String msg = "Producer05_HEADERS(" + System.currentTimeMillis() + ")";
            String routingKey = "CMS.EMAIL";
            msg = routingKey + ":" + msg;
            channel.basicPublish(EXCHANGE_HEADERS, "", propertiesBuilder.build(), msg.getBytes());
        }
        ActiveMQUtils.close();
    }
}
