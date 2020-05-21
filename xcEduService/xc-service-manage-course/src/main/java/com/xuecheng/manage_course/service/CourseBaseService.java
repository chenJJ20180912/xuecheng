package com.xuecheng.manage_course.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_course.dao.CourseBaseMapper;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseBaseService {

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if(courseListRequest == null){
            courseListRequest = new CourseListRequest();
        }
        if(page<=0){
            page = 0;
        }
        if(size<=0){
            size = 20;
        }
        // 启动分页 原理是将页码和size存放到ThreadLocal中 当mybatis构建sql语句的时候通过拦截器将分页参数加上去
        PageHelper.startPage(page,size);
        PageInfo<CourseInfo> pageInfo = PageInfo.of(courseBaseMapper.findCourseListPage(courseListRequest));
        QueryResult<CourseInfo> courseIncfoQueryResult = new QueryResult<>();
        courseIncfoQueryResult.setList(pageInfo.getList());
        courseIncfoQueryResult.setTotal(pageInfo.getTotal());
        return new QueryResponseResult<>(CommonCode.SUCCESS, courseIncfoQueryResult);
    }
}
