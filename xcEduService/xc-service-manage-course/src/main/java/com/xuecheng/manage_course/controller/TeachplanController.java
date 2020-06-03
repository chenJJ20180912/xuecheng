package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.TeachplanControllerApi;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.TeachplanResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.TeachplanServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teachplan")
public class TeachplanController implements TeachplanControllerApi {

    @Autowired
    private TeachplanServie teachplanServie;

    //查询课程计划
    @Override
    @GetMapping("/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return teachplanServie.findTeachplanList(courseId);
    }

    //添加课程计划
    @Override
    @PostMapping("/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return teachplanServie.addTeachplan(teachplan);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult del(@PathVariable("id") String tearchplanId) {
        return teachplanServie.del(tearchplanId);
    }

    @Override
    @GetMapping("/get/{id}")
    public TeachplanResult findById(@PathVariable("id")String tearchplanId) {
        return teachplanServie.findById(tearchplanId);
    }
}
