package com.xuecheng.framework.domain.cms;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@ToString
@Document(collection = "cms_page_post_log")
public class CmsPagePostLog {

    // 页面id
    private String pageId;

    // 站点ip
    private String host;

    //是否成功
    private String success;

    //反馈消息
    private String msg;

    // 反馈时间
    private Date createTime;
}
