package com.xuecheng.test.rabbitmq.rabbitmq;

import com.rabbitmq.client.*;
import com.xuecheng.test.rabbitmq.ActiveMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author chenjia
 * @version 1.0
 * @date 2020/5/14 9:25
 * @desc 生产者 helloWorld
 */
public class Producer06_rpc {

    private static final String QUEUE_SEND = "QUEUE_SEND";
    private static final String QUEUE_RECEIVE = "QUEUE_RECEIVE";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = ActiveMQUtils.newChannel();
        channel.queueDeclare(QUEUE_SEND, true, false, false, null);
        channel.queueDeclare(QUEUE_RECEIVE, true, false, false, null);
        // 往QUEUE_SEND 发送数据等待对方服务处理，处理结果放在QUEUE_RECEIVE队列
        // 本次模拟 1 + 2 = 3
        channel.basicPublish("",QUEUE_SEND,null,"1,2".getBytes());
        channel.basicConsume(QUEUE_RECEIVE,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("result:"+new String(body,"utf-8"));
            }
        });
        // 不能关闭通道和连接,否则会模拟失败
        //ActiveMQUtils.close();
    }
}
