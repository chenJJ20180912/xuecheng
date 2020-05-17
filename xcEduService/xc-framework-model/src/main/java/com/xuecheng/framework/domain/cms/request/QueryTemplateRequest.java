package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * 模板查询条件
 */
@Data
public class QueryTemplateRequest {
    @ApiModelProperty("站点id")
    private String siteId;
    @ApiModelProperty("模版名称")
    private String templateName;
}
