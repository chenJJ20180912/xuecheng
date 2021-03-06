import http from './../../../base/api/public'
import querystring from 'querystring'

let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.xcApiUrlPre;

//查询课程基本信息
export const getCoursebaseById = (id) => {
  return http.requestGet(apiUrl + '/course/coursebase/get/' + id);
};

//我的课程列表
export const findCourseList = (page, size, params) => {
//使用工具类将json对象转成key/value
  let queries = querystring.stringify(params)
  return http.requestQuickGet(apiUrl + "/course/coursebase/list/" + page + "/" + size + "?" + queries)
};

//查询课程分类
export const category_findlist = () => {
  return http.requestQuickGet(apiUrl + '/course/category/list')
};
/*添加课程基础信息*/
export const addCourseBase = params => {
  return http.requestPost(apiUrl + '/course/coursebase/add', params)
};

export  const updateCourseBase = (id,params)  => {
  return http.requestPut(apiUrl + '/course/coursebase/update/'+id, params)
};
/*查询课程计划*/
export const findTeachplanList = courseid => {
  return http.requestQuickGet(apiUrl + '/course/teachplan/list/' + courseid)
};
/*添加课程计划*/
export const addTeachplan = teachplah => {
  return http.requestPost(apiUrl + '/course/teachplan/add', teachplah)
};

//保存课程图片地址到课程数据 库
export const addCoursePic = (courseId, pic) => {
  return http.requestPost(apiUrl + '/course/pic/add/' + courseId + "?pic=" + pic)
};
//查询课程图片
export const findCoursePicList = courseId => {
  return http.requestQuickGet(apiUrl + '/course/pic/get/' + courseId)
};

//删除课程图片
export const deleteCoursePic = courseId => {
  return http.requestDelete(apiUrl + '/course/pic/del/' + courseId)
};
/*预览课程*/
export const preview = id => {
  return http.requestPost(apiUrl + '/course/preview/' + id);
};
/*发布课程*/
export const publish = id => {
  return http.requestPost(apiUrl + '/course/publishCourse/' + id);
};
//查询课程信息
export const findCourseView = courseId => {
  return http.requestQuickGet(apiUrl + '/course/courseview/' + courseId)
};

/*保存媒资信息*/
export const savemedia = teachplanMedia => {
  return http.requestPost(apiUrl + '/course/savemedia', teachplanMedia);
};

// 根据id获取课程运营
export const getCourseMarketById = (id) => {
  return http.requestGet(apiUrl + '/course/coursemarket/get/' + id);
};
// 删除课程计划
export const deleteCoursePlan = (id) => {
  return http.requestDelete(apiUrl + '/course/teachplan/del/' + id);
};
// 更新课程运营
export const updateCourseMarket = (id, params) => {
  return http.requestPut(apiUrl + '/course/coursemarket/update/' + id, params);
};

// 根据id查找课程计划
export const findTeachplanById = (id) => {
  return http.requestGet(apiUrl + '/course/teachplan/get/' + id);
};
