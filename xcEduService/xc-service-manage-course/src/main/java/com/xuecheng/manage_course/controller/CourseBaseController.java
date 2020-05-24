package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseBaseControllerApi;
import com.xuecheng.api.course.TeachplanControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
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
            @PathVariable("page") int page, @PathVariable("size") int size,
            CourseListRequest courseListRequest) {
        return courseBaseService.findCourseList(page, size, courseListRequest);
    }

    @Override
    @PostMapping("/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseBaseService.addCourseBase(courseBase);
    }

    @Override
    @GetMapping("/get/{courseId}")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) throws RuntimeException {
        return courseBaseService.getCoursebaseById(courseId);
    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseResult updateCourseBase(@PathVariable("id") String id, @RequestBody CourseBase courseBase) {
        return courseBaseService.updateCoursebase(id, courseBase);
    }
}
