package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.request.QueryConfigRequest;
import com.xuecheng.framework.domain.cms.response.CmsConfigResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Api(value="cms数据配置管理接口",description = "cms数据配置管理接口，提供数据配置的增、删、改、查")
public interface CmsConfigControllerApi {
    //数据配置查询
    @ApiOperation("分页查询数据配置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page",value = "页码",required=true,paramType="path",dataType="int"),
            @ApiImplicitParam(name="size",value = "每页记录数",required=true,paramType="path",dataType="int")
    })
    public QueryResponseResult findList(int page, int size, QueryConfigRequest queryConfigRequest);
    //新增数据配置
    @ApiOperation("新增数据配置")
    public CmsConfigResult add(CmsConfig cmsConfig);

    //根据数据配置id查询数据配置信息
    @ApiOperation("根据数据配置id查询数据配置信息")
    public CmsConfigResult findById(String id);

    @GetMapping("/findByDataUrl/{id}")
    CmsConfig findByDataUrl(@PathVariable("id") String id);

    //修改数据配置
    @ApiOperation("修改数据配置")
    public CmsConfigResult edit(String id,CmsConfig cmsConfig);

    //删除数据配置
    @ApiOperation("删除数据配置")
    public ResponseResult delete(String id);
}
