package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.model.response.ResponseResult;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CourseBaseResult extends ResponseResult {

    private CourseBase courseBase;
    public CourseBaseResult(CourseBase courseBase){
        super();
        this.courseBase = courseBase;
    }
}
