package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsConfigModel;
import com.xuecheng.framework.domain.cms.request.QueryConfigRequest;
import com.xuecheng.framework.domain.cms.response.CmsConfigResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/config")
@RestController
public class CmsConfigController implements CmsConfigControllerApi {

    @Autowired
    private CmsConfigService cmsConfigService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size")int size, QueryConfigRequest queryConfigRequest) {

        return cmsConfigService.findList(page,size,queryConfigRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsConfigResult add(@RequestBody CmsConfig cmsConfig) {
        return cmsConfigService.add(cmsConfig);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsConfigResult findById(@PathVariable("id") String id) {
        return  cmsConfigService.getById(id) ;
    }

    @Override
    @GetMapping("/findByDataUrl/{id}")
    public CmsConfig findByDataUrl(@PathVariable("id") String id){
        return  cmsConfigService.getById(id).getCmsConfig() ;
    }

    @Override
    @PutMapping("/edit/{id}")//这里使用put方法，http 方法中put表示更新
    public CmsConfigResult edit(@PathVariable("id")String id, @RequestBody CmsConfig cmsConfig) {
        return cmsConfigService.update(id,cmsConfig);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsConfigService.delete(id);
    }

    @Override
    @PostMapping("/{id}/model/add")
    public CmsConfigResult addCmsConfigModel(@PathVariable("id") String id,@RequestBody CmsConfigModel configModel) {
        return cmsConfigService.addCmsConfigModel(id,configModel);
    }

    @Override
    @PutMapping("/{id}/model/edit/{key}")
    public CmsConfigResult editCmsConfigModel(@PathVariable("id") String id,@PathVariable("key") String key,@RequestBody  CmsConfigModel configModel) {
        return cmsConfigService.editCmsConfigModel(id,key,configModel);
    }

    @Override
    @DeleteMapping("/{id}/model/del/{key}")
    public CmsConfigResult delCmsConfigModel(@PathVariable("id") String id,@PathVariable("key")String key) {
        return cmsConfigService.delCmsConfigModel(id,key);
    }
}
