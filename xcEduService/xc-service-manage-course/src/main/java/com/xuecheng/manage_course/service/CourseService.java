package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPublishResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {
    @Autowired
    private CourseBaseService courseBaseService;
    @Autowired
    private CourseMarketService courseMarketService;
    @Autowired
    private CoursePicService coursePicService;
    @Autowired
    private TeachplanServie teachplanServie;
    @Autowired
    private CmsPageClient cmsPageClient;


    @Value("${course‐publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course‐publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course‐publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course‐publish.siteId}")
    private String publish_siteId;
    @Value("${course‐publish.templateId}")
    private String publish_templateId;
    @Value("${course‐publish.previewUrl}")
    private String previewUrl;

    /**
     * 根据课程id查找课程相关信息
     *
     * @param id
     * @return
     */
    public CourseView courseview(String id) {
        CourseView courseView = new CourseView();
        CourseBase courseBase = courseBaseService.getCoursebaseById(id);
        courseView.setCourseBase(courseBase);
        CourseMarket courseMarket = courseMarketService.getCourseMarketById(id);
        courseView.setCourseMarket(courseMarket);
        CoursePic coursePic = coursePicService.findById(id);
        courseView.setCoursePic(coursePic);
        TeachplanNode teachplanNode = teachplanServie.findTeachplanList(id);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    // 课程预览
    public CoursePublishResult preview(String courseId) {
        CmsPage cmsPage = buildCmsPage(courseId);
        //远程请求cms保存页面信息
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //页面id
        String pageId = cmsPageResult.getCmsPage().getPageId();
        //页面url
        String pageUrl = previewUrl + pageId;
        return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
    }

    public CmsPage buildCmsPage(String courseId){
        CourseBase one = courseBaseService.getCoursebaseById(courseId);
        CmsPage cmsPage = new CmsPage();
        //站点
        cmsPage.setSiteId(publish_siteId);//课程预览站点
        //模板
        cmsPage.setTemplateId(publish_templateId);
        //页面名称
        cmsPage.setPageName(courseId + ".html");
        //页面别名
        cmsPage.setPageAliase(one.getName());
        //页面访问路径
        cmsPage.setPageWebPath(publish_page_webpath);
        //页面存储路径
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        //数据url
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        return cmsPage;
    }
    @Transactional
    public CmsPublishResult publishCourse(String courseId) {
        CmsPage cmsPage = buildCmsPage(courseId);
        // 远程调用cms的一键发布接口
        CmsPublishResult cmsPublishResult = cmsPageClient.publishCourse(cmsPage);
        if(!cmsPublishResult.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //修改课程发布状态
        CourseBase one = courseBaseService.getCoursebaseById(courseId);
        one.setStatus("202002");
        ResponseResult result = courseBaseService.updateCoursebase(one.getId(), one);
        return new CmsPublishResult(CommonCode.SUCCESS,cmsPublishResult.getPageUrl());
    }
}
