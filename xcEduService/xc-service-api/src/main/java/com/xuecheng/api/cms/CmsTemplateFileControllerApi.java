package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Api(value="cms模板文件管理接口",description = "cms模板管理接口，提供模板的增、删、改、查")
public interface CmsTemplateFileControllerApi {

    @ApiOperation("上传文件")
    ResponseResult uploadTemplate(String templateId,MultipartFile file);

    @ApiOperation("查看文件")
    void downTemplate(String templateId,String fileName);


    @GetMapping("/preview/{id}")
    void previewPage(String pageId);

    @PostMapping("/postPage/{id}")
    ResponseResult postPage(@PathVariable("id") String pageId);
}
