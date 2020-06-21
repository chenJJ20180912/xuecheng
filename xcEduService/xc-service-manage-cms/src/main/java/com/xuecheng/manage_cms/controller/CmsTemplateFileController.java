package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsTemplateFileControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/template")
@Controller
public class CmsTemplateFileController extends BaseController implements CmsTemplateFileControllerApi {

    @Autowired
    private CmsTemplateService cmsTemplateService;

    @Override
    @PostMapping("/uploadTemplate/{id}")
    @ResponseBody
    public ResponseResult uploadTemplate(@PathVariable("id") String templateId, @RequestParam("file") MultipartFile file) {
        return cmsTemplateService.uploadTemplate(templateId, file);
    }

    @Override
    @GetMapping("/downTemplate/{id}")
    public void downTemplate(@PathVariable("id") String templateId, @RequestParam("fileName") String fileName) {
        String templateFileContent =cmsTemplateService.queryTemplate(templateId);
        if (StringUtils.isNotEmpty(templateFileContent)) {
            try {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream;charset=GBK");
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso-8859-1"));
                IOUtils.write(templateFileContent, response.getOutputStream(), "utf-8");
                response.getOutputStream().flush();
            } catch (IOException e) {
                ExceptionCast.cast(CmsCode.CMS_TEMPLATE_FILE_DOWN_FAILD);
            }
        }
    }

    @Override
    @GetMapping("/preview/{id}")
    public void previewPage(@PathVariable("id") String pageId) {
        String templateFileContent = cmsTemplateService.previewPage(pageId);
        if (StringUtils.isNotEmpty(templateFileContent)) {
            try {
                response.setContentType("text/html;charset=utf‐8");
                IOUtils.write(templateFileContent, response.getOutputStream(), "utf-8");
                response.getOutputStream().flush();
            } catch (IOException e) {
                ExceptionCast.cast(CmsCode.CMS_TEMPLATE_FILE_DOWN_FAILD);
            }
        }
    }

    @Override
    @PostMapping("/postPage/{id}")
    @ResponseBody
    public ResponseResult postPage(@PathVariable("id") String pageId){
        return cmsTemplateService.postPage(pageId);
    }

    @Override
    @PostMapping("/publishPage")
    @ResponseBody
    public CmsPublishResult publishPage(@RequestBody CmsPage cmsPage) {
        return cmsTemplateService.publishPage(cmsPage);
    }
}
