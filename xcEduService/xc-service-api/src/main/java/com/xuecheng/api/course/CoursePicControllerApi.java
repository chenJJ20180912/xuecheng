package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程图片接口", description = "课程图片接口，提供课程图片的增、删、改、查")
public interface CoursePicControllerApi {
    @ApiOperation("新增课程图片")
    ResponseResult add(String courseId,String pic);

    @ApiOperation("删除课程图片")
    ResponseResult delete(String courseId);

    @ApiOperation("查找课程图片")
    CoursePic findById(String courseId);
}
