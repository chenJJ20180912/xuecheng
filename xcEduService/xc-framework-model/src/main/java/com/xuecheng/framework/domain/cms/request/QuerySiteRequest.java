package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuerySiteRequest {

    @ApiModelProperty("站点名称")
    private String siteName;
    @ApiModelProperty("站点域名")
    private String siteDomain;
    @ApiModelProperty("站点端口")
    private String sitePort;
    @ApiModelProperty("站点访问地址")
    private String siteWebPath;
}
