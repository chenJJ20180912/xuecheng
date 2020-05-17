package com.xuecheng.manage_cms.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPagePostLog;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPagePostLogRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CmsPostPage {

    @Autowired
    private CmsPagePostLogRepository cmsPagePostLogRepository;

    // 接收到日志信息
    @RabbitListener(queues = RabbitmqConfig.QUEUE_CMS_PAGE_POST)
    public void receiveLog(String msg) {
        CmsPagePostLog log = JSON.parseObject(msg, CmsPagePostLog.class);
        cmsPagePostLogRepository.save(log);
    }
}
