package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.TeachplanResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class TeachplanServie {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanRepository teachplanRepository;

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    //查询课程计划
    public TeachplanNode findTeachplanList(String courseId) {
        List<TeachplanNode> lst = teachplanMapper.selectList(courseId);
        if (lst != null) {
            return lst.get(0);
        }
        return null;
    }

    //获取课程根结点，如果没有则添加根结点
    public String getTeachplanRoot(String courseId) {
//校验课程id
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
//取出课程计划根结点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");

        if (teachplanList == null || teachplanList.size() == 0) { //新增一个根结点
            Teachplan teachplanRoot = new Teachplan();
            teachplanRoot.setCourseid(courseId);
            teachplanRoot.setPname(courseBase.getName());
            teachplanRoot.setParentid("0");
            teachplanRoot.setGrade("1");//1级 teachplanRoot.setStatus("0");//未发布 teachplanRepository.save(teachplanRoot); return    teachplanRoot.getId();
            teachplanRoot.setStatus("1");
            return teachplanRepository.save(teachplanRoot).getId();
        }
        Teachplan teachplan = teachplanList.get(0);
        return teachplan.getId();
    }

    //添加课程计划
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //校验课程id和课程计划名称
        if (teachplan == null ||
                StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //取出课程id
        String courseid = teachplan.getCourseid();
        //取出父结点id
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)) {
        //如果父结点为空则获取根结点
            parentid = getTeachplanRoot(courseid);
        }
        //取出父结点信息
        Optional<Teachplan> teachplanOptional = teachplanRepository.findById(parentid);
        if (!teachplanOptional.isPresent()) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //父结点
        Teachplan teachplanParent = teachplanOptional.get();
        //父结点级别
        String parentGrade = teachplanParent.getGrade();
        //设置父结点
        teachplan.setParentid(parentid);
        if(StringUtils.isEmpty(teachplan.getStatus())){
            teachplan.setStatus("0");//未发布
        }
        //子结点的级别，根据父结点来判断
        if (parentGrade.equals("1")) {
            teachplan.setGrade("2");
        } else if (parentGrade.equals("2")) {
            teachplan.setGrade("3");
        }
        //设置课程id
        teachplan.setCourseid(teachplanParent.getCourseid());
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public ResponseResult del(String tearchplanId) {
        Optional<Teachplan> optional = teachplanRepository.findById(tearchplanId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        Teachplan teachplan = optional.get();
        // 查找是否存在子节点
        List<Teachplan> children = teachplanRepository.findByCourseidAndParentid(teachplan.getCourseid(), tearchplanId);
        if(!CollectionUtils.isEmpty(children)){
            ExceptionCast.cast(CourseCode.COURSE_PLAN_HAS_CHILDREN);
        }
        teachplanRepository.delete(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public TeachplanResult findById(String tearchplanId) {
        Optional<Teachplan> optional = teachplanRepository.findById(tearchplanId);
        if(!optional.isPresent()){
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        return new TeachplanResult(optional.get());
    }
}
