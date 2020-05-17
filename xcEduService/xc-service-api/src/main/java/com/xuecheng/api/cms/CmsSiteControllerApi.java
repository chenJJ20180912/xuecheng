package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QuerySiteRequest;
import com.xuecheng.framework.domain.cms.response.CmsSiteResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms站点管理接口", description = "cms站点管理接口，提供站点的增、删、改、查")
public interface CmsSiteControllerApi {

    @ApiOperation("获取站点所有数据")
    QueryResult<CmsSite> findAll();

    //页面查询
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页记录数", required = true, paramType = "path", dataType = "int")
    })
    QueryResponseResult findList(int page, int size, QuerySiteRequest querySiteRequest);

    //新增页面
    @ApiOperation("新增页面")
    CmsSiteResult add(CmsSite cmsSite);

    //根据页面id查询页面信息
    @ApiOperation("根据页面id查询页面信息")
    CmsSiteResult findById(String id);

    //修改页面
    @ApiOperation("修改页面")
    CmsSiteResult edit(String id, CmsSite cmsSite);

    //删除页面
    @ApiOperation("删除页面")
    ResponseResult delete(String id);
}
