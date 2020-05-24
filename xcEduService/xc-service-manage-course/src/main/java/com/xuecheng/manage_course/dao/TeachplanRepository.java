package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {

    /**
     * 通过课程编号和节点等级查找数据
     *
     * @param courseId
     * @param grade
     * @return
     */
    List<Teachplan> findByCourseidAndGrade(String courseId, String grade);

    //定义方法根据课程id和父结点id查询出结点列表，可以使用此方法实现查询根结点
    public List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}
