package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeachplanMapper {

    List<TeachplanNode> selectList(String courseId);
}
