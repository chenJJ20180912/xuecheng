package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPublishResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

@Api(value="cms模板文件管理接口",description = "cms模板管理接口，提供模板的增、删、改、查")
public interface CmsTemplateFileControllerApi {

    @ApiOperation("上传文件")
    ResponseResult uploadTemplate(String templateId,MultipartFile file);

    @ApiOperation("查看文件")
    void downTemplate(String templateId,String fileName);


    @ApiOperation("页面预览")
    void previewPage(String pageId);

    @ApiOperation("页面发布")
    ResponseResult postPage( String pageId);
    @ApiOperation("一键发布页面")
    CmsPublishResult publishPage(CmsPage cmsPage);
}
