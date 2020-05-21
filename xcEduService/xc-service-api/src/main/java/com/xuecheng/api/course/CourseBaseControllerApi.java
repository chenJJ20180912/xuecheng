package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "我的课程接口", description = "我的课程接口，提供课程计划的增、删、改、查")
public interface CourseBaseControllerApi {

    @ApiOperation("查询我的课程列表")
    public QueryResponseResult<CourseInfo> findCourseList(
            int page,
            int size,
            CourseListRequest courseListRequest
    );

}
