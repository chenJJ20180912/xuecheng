package com.xuecheng.manager_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuecheng.framework.domain.cms.CmsPagePostLog;
import com.xuecheng.framework.exception.CustomException;
import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.ResultCode;
import com.xuecheng.manager_cms_client.config.RabbitmqConfig;
import com.xuecheng.manager_cms_client.service.CmsPageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map;

@Component
public class CmsPostPage {

    @Autowired
    private CmsPageService cmsPageService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "${xuecheng.mq.receiveQueue}")
    public void postPage(String msg){
        CmsPagePostLog log = new CmsPagePostLog();
        Map map = JSON.parseObject(msg, Map.class);
        String pageId = (String) map.get("pageId");
        log.setHost(getLocalHost());
        log.setCreateTime(new Date());
        log.setPageId(pageId);
        try {
            cmsPageService.savePageToServerPath(pageId);
            log.setSuccess("1");
            log.setMsg("发布成功!");
        }catch (Exception e){
            e.printStackTrace();
            log.setSuccess("0");
            if(e instanceof CustomException){
                log.setMsg(((CustomException) e).getResultCode().toString());
            }else{
                ResultCode resultCode = ExceptionCatch.EXCEPTIONS.get(e);
                if(resultCode != null){
                    log.setMsg(resultCode.message());
                }else{
                    log.setMsg(e.getMessage());
                }
            }
        }
        // 将反馈消息写如反馈队列
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_CMS_PAGE_POST,
                RabbitmqConfig.QUEUE_CMS_PAGE_POST, JSONObject.toJSONString(log));
    }

    // 获取当前cms客户端服务所在的ip
    private String getLocalHost(){
        String localHost = "";
        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return localHost;
    }
}
