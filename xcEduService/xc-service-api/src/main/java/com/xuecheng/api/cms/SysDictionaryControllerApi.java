package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.domain.system.SysDictionaryValue;
import com.xuecheng.framework.domain.system.response.SysDictionaryResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "数据字典接口", description = "提供数据字典接口的管理、查询功能")
public interface SysDictionaryControllerApi {
    //数据字典
    @ApiOperation(value = "数据字典查询接口")
    SysDictionary getByType(String type);

    @ApiOperation(value = "查询数据字典")
    QueryResponseResult list(int page, int size, SysDictionary sysDictionary);

    @ApiOperation(value = "新增数据字典")
    SysDictionaryResult add(SysDictionary sysDictionary);

    @ApiOperation(value = "修改数据字典")
    SysDictionaryResult edit(String id,SysDictionary sysDictionary);

    @ApiOperation(value = "删除数据字典")
    ResponseResult del(String id);

    @ApiOperation(value = "新增字典明细")
    SysDictionaryResult addValue(String id,SysDictionaryValue sysDictionaryValue);

    @ApiOperation(value = "修改数据字典明细")
    SysDictionaryResult editValue(String id, String sdId, SysDictionaryValue sysDictionaryValue);

    @ApiOperation(value = "删除数据字典明细")
    ResponseResult delValue(String id,String sdId);

}
