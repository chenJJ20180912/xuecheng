package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryTemplateRequest;
import com.xuecheng.framework.domain.cms.response.CmsTemplateResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="cms模板管理接口",description = "cms模板管理接口，提供模板的增、删、改、查")
public interface CmsTemplateControllerApi {

    @ApiOperation("获取模板所有数据")
    QueryResult<CmsTemplate> findAll();

    //页面查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    QueryResponseResult findList(int page, int size, QueryTemplateRequest queryTemplateRequest);

    //新增页面
    @ApiOperation("新增页面")
    CmsTemplateResult add(CmsTemplate cmsTemplate);

    //根据页面id查询页面信息
    @ApiOperation("根据页面id查询页面信息")
    CmsTemplateResult findById(String id);

    //修改页面
    @ApiOperation("修改页面")
    CmsTemplateResult edit(String id, CmsTemplate cmsTemplate);

    //删除页面
    @ApiOperation("删除页面")
    ResponseResult delete(String id);

    @ApiOperation("查看文件")
    QueryResult<String> queryTemplate(String templateId);
}
