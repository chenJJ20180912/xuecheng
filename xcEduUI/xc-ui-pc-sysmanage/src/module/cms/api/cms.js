import http from './../../../base/api/public'
import querystring from 'querystring'
let sysConfig = require('@/../config/sysConfig')
let apiUrl = sysConfig.xcApiUrlPre;

//页面查询
export const page_list = (page,size,params) =>{
  //将params对象数据拼装成key/value串
  let queryString =  querystring.stringify(params);
  //请求服务端的页面查询接口
  return http.requestQuickGet(apiUrl+'/cms/page/list/'+page+'/'+size+"?"+queryString);
};
//新增页面
export const page_add = params =>{
  return http.requestPost(apiUrl+'/cms/page/add',params)
};
//根据id查询页面
export const page_get = id =>{
  return http.requestQuickGet(apiUrl+'/cms/page/get/'+id)
};
//修改页面提交
export const page_edit = (id,params) =>{
 return http.requestPut(apiUrl+'/cms/page/edit/'+id,params)
};
//删除页面
export const page_del= (id) =>{
  return http.requestDelete(apiUrl+'/cms/page/del/'+id)
};
export const site_list = (page,size,params) =>{
  //将params对象数据拼装成key/value串
  let queryString =  querystring.stringify(params);
  //请求服务端的页面查询接口
  return http.requestQuickGet(apiUrl+'/cms/site/list/'+page+'/'+size+"?"+queryString);
};
// 站点下拉框
export  const site_getAll = () =>{
  return http.requestGet(apiUrl+"/cms/site/findAll")
};
// 模板下拉框
export  const template_getAll = () =>{
  return http.requestGet(apiUrl+"/cms/template/findAll")
};

//新增站点
export const site_add = params =>{
  return http.requestPost(apiUrl+'/cms/site/add',params)
};
//根据id查询站点
export const site_get = id =>{
  return http.requestQuickGet(apiUrl+'/cms/site/get/'+id)
};
//修改站点提交
export const site_edit = (id,params) =>{
  return http.requestPut(apiUrl+'/cms/site/edit/'+id,params)
};
//删除站点
export const site_del= (id) =>{
  return http.requestDelete(apiUrl+'/cms/site/del/'+id)
};

export const template_list = (page,size,params) =>{
  //将params对象数据拼装成key/value串
  let queryString =  querystring.stringify(params);
  //请求服务端的页面查询接口
  return http.requestQuickGet(apiUrl+'/cms/template/list/'+page+'/'+size+"?"+queryString);
};
//新增模板
export const template_add = params =>{
  return http.requestPost(apiUrl+'/cms/template/add',params)
};
//根据id查询模板
export const template_get = id =>{
  return http.requestQuickGet(apiUrl+'/cms/template/get/'+id)
};
//修改模板提交
export const template_edit = (id,params) =>{
  return http.requestPut(apiUrl+'/cms/template/edit/'+id,params)
};
//删除模板
export const template_del= (id) =>{
  return http.requestDelete(apiUrl+'/cms/template/del/'+id)
};

// 文件上传
export const template_uploadFile_URL = () =>{
  return apiUrl+'/cms/template/uploadTemplate/'
};
// 查询文件内容
export const template_queryFile = (id) =>{
  return http.requestGet(apiUrl+'/cms/template/queryTemplate/'+id)
};
// 文件下载
export const template_downloadFile = (id,fileName) =>{
  // 需要注意的是这一并不是通过http.requestGet 去调用get请求，
  // 而是通过 window.location.href的方式
  // 否则下载的文件不会直接保存到本地，
  // 除了这种方式以外还可以用window.open(url) 去解决这个问题
  let url = apiUrl+'/cms/template/downTemplate/'+id+'?fileName='+fileName+'.ftl'
  //window.location.href = url
  window.open(url)
};

export const previewPage = (id) =>{
  let url = apiUrl+'/cms/template/preview/'+id
  window.open(url)
};

// 发布界面
export const page_post = (id) =>{
  return http.requestPost(apiUrl+'/cms/template/postPage/'+id)
};

// 查看发布日志
export const page_logs_list = (id) =>{
  return http.requestGet(apiUrl+'/cms/page/findLogs/'+id)
}
