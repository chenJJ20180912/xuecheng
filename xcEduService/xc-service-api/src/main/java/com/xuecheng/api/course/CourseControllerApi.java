package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.response.CmsPublishResult;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "课程相关综合服务", description = "课程多维度综合服务")
public interface CourseControllerApi {

    @ApiOperation("课程视图查询")
    public CourseView courseview(String id);

    @ApiOperation("预览课程")
    public CoursePublishResult preview(String id);

    @ApiOperation("发布课程")
    public CmsPublishResult publishCourse(String id);

}
