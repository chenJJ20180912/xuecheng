package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsTemplateControllerApi;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryTemplateRequest;
import com.xuecheng.framework.domain.cms.response.CmsTemplateResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/template")
@RestController
public class CmsTemplateController extends BaseController implements CmsTemplateControllerApi {

    @Autowired
    private CmsTemplateService cmsTemplateService;


    @Override
    @GetMapping("/findAll")
    public QueryResult<CmsTemplate> findAll() {
        return cmsTemplateService.findAll();
    }


    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryTemplateRequest queryTemplateRequest) {

        return cmsTemplateService.findList(page, size, queryTemplateRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsTemplateResult add(@RequestBody CmsTemplate cmsTemplate) {
        return cmsTemplateService.add(cmsTemplate);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsTemplateResult findById(@PathVariable("id") String id) {
        return cmsTemplateService.getById(id);
    }

    @Override
    @PutMapping("/edit/{id}")//这里使用put方法，http 方法中put表示更新
    public CmsTemplateResult edit(@PathVariable("id") String id, @RequestBody CmsTemplate cmsTemplate) {
        return cmsTemplateService.update(id, cmsTemplate);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsTemplateService.delete(id);
    }

    @Override
    @GetMapping("/queryTemplate/{id}")
    public QueryResult<String> queryTemplate(@PathVariable("id") String templateId) {
        QueryResult<String> result = new QueryResult<String>();
        String data = cmsTemplateService.queryTemplate(templateId);
        if(StringUtils.isNotEmpty(data)){
            List<String> datas = new ArrayList<>();
            datas.add(data);
            result.setList(datas);
        }
        return result;
    }

}
