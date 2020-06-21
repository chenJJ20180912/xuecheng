package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseView implements java.io.Serializable {
    // 基础信息
    private CourseBase courseBase;
    // 课程营销
    private CourseMarket courseMarket;
    // 图片
    private CoursePic coursePic;
    // 教学计划
    private TeachplanNode teachplanNode;
}
