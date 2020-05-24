package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程营销接口", description = "课程营销接口，提供课程营销的增、删、改、查")
public interface CourseMarketControllerApi {

    @ApiOperation("获取课程营销信息")
    public CourseMarket getCourseMarketById(String    courseId);

    @ApiOperation("更新课程营销信息")
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket);
}
