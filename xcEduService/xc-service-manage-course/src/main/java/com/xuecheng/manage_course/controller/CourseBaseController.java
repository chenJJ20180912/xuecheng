package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseBaseControllerApi;
import com.xuecheng.api.course.TeachplanControllerApi;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_course.service.CourseBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coursebase")
public class CourseBaseController implements CourseBaseControllerApi {

    @Autowired
    private CourseBaseService courseBaseService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(
            @PathVariable("page") int page,@PathVariable("size") int size,
            CourseListRequest courseListRequest) {
        return courseBaseService.findCourseList(page,size,courseListRequest);
    }
}
