package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.ToString;

/**
 * Created by mrt on 2018/3/5.
 */
@ToString
public enum CmsCode implements ResultCode {
    CMS_PARAMS_IS_NULL(false,24001,"参数不可为空！"),
    CMS_ADDPAGE_EXISTSNAME(false,24001,"页面名称已存在！"),
    CMS_GENERATEHTML_DATAURLISNULL(false,24002,"从页面信息中找不到获取数据的url！"),
    CMS_GENERATEHTML_DATAISNULL(false,24003,"根据页面的数据url获取不到数据！"),
    CMS_GENERATEHTML_TEMPLATEISNULL(false,24004,"页面模板为空！"),
    CMS_GENERATEHTML_HTMLISNULL(false,24005,"生成的静态html为空！"),
    CMS_GENERATEHTML_SAVEHTMLERROR(false,24005,"保存静态html出错！"),
    CMS_COURSE_PERVIEWISNULL(false,24007,"预览页面为空！"),
    CMS_PAGE_NOT_EXISTS(false,24008,"页面不存在！"),

    CMS_SITE_EXISTS(false,24101,"[域名:端口/访问地址]已存在！"),
    CMS_SITE_NOT_EXISTS(false,24102,"站点不存在！"),



    CMS_TEMPLATE_EXISTS(false,24201,"[站点 + 模板名称]已存在！"),
    CMS_TEMPLATE_NOT_EXISTS(false,24202,"模板不存在！"),
    CMS_TEMPLATE_FILE_UPLOAD_FAILD(false,24203,"文件上传失败！"),
    CMS_TEMPLATE_FILE_LOAD_FAILD(false,24204,"文件加载失败！"),
    CMS_TEMPLATE_FILE_DOWN_FAILD(false,24205,"文件下载失败！"),
    CMS_TEMPLATE_FILE_NOT_EXISTS(false,24206,"模板文件不存在！"),
    CMS_TEMPLATE_FILE_CONVERT_FAIL(false,24206,"模板文件转换异常！"),
    ;

    //操作代码
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private CmsCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
