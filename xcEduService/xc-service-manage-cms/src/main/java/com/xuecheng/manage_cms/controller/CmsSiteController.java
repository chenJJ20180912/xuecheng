package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsSiteControllerApi;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QuerySiteRequest;
import com.xuecheng.framework.domain.cms.response.CmsSiteResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/site")
@RestController
public class CmsSiteController implements CmsSiteControllerApi {

    @Autowired
    private CmsSiteService cmsSiteService;

    @Override
    @GetMapping("findAll")
    public QueryResult<CmsSite> findAll() {
        return cmsSiteService.findAll();
    }


    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size")int size, QuerySiteRequest querySiteRequest) {

        return cmsSiteService.findList(page,size,querySiteRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsSiteResult add(@RequestBody CmsSite cmsSite) {
        return cmsSiteService.add(cmsSite);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsSiteResult findById(@PathVariable("id") String id) {
        return  cmsSiteService.getById(id) ;
    }

    @Override
    @PutMapping("/edit/{id}")//这里使用put方法，http 方法中put表示更新
    public CmsSiteResult edit(@PathVariable("id")String id, @RequestBody CmsSite cmsSite) {
        return cmsSiteService.update(id,cmsSite);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsSiteService.delete(id);
    }
}
