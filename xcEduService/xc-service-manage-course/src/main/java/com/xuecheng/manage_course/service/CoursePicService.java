package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CoursePicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CoursePicService {
    @Autowired
    private CoursePicRepository coursePicRepository;

    @Transactional
    public ResponseResult add(String courseId, String pic) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        CoursePic coursePic = optional.isPresent()?optional.get():new CoursePic();
        coursePic.setPic(pic);
        coursePic.setCourseid(courseId);
        coursePicRepository.save(coursePic);
        return new ResponseResult();
    }

    @Transactional
    public ResponseResult delete(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if(optional.isPresent()){
            // 文件也要删除

            // 删除
            coursePicRepository.deleteById(courseId);
        }
        return new ResponseResult();
    }

    public CoursePic findById(String courseId) {
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if(optional.isPresent()){
            return optional.get();
        }
        return new CoursePic();
    }
}
