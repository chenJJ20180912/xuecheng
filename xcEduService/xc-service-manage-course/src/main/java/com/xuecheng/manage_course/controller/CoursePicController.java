package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CoursePicControllerApi;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CoursePicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pic")
public class CoursePicController implements CoursePicControllerApi {

    @Autowired
    private CoursePicService coursePicService;

    @Override
    @PostMapping("/add/{courseId}")
    public ResponseResult add(@PathVariable("courseId")String courseId,@RequestParam("pic") String pic) {
        return coursePicService.add(courseId,pic);
    }

    @Override
    @DeleteMapping("/del/{courseId}")
    public ResponseResult delete(@PathVariable("courseId")String courseId) {
        return coursePicService.delete(courseId);
    }

    @Override
    @GetMapping("/get/{courseId}")
    public CoursePic findById(@PathVariable("courseId")String courseId) {
        return coursePicService.findById(courseId);
    }
}
