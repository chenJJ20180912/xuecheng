package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程分类接口", description = "课程分类接口，提供课程分类的增、删、改、查")
public interface CategoryControllerApi {

    @ApiOperation("查询分类")
    QueryResponseResult<CategoryNode> findList();
}
