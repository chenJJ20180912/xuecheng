package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.cms.response.CmsPublishResult;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CourseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;

    @Override
    @GetMapping("/courseview/{id}")
    public CourseView courseview(@PathVariable("id")String id) {
        return courseService.courseview(id);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id")String id) {
        return courseService.preview(id);
    }

    @Override
    @PostMapping("/publishCourse/{id}")
    public CmsPublishResult publishCourse(@PathVariable("id")String id) {
        return courseService.publishCourse(id);
    }
}
