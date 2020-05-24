package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.domain.system.SysDictionaryValue;
import com.xuecheng.framework.domain.system.response.SysDictionaryResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController implements SysDictionaryControllerApi {

    @Autowired
    private SysDictionaryService sysDictionaryService;

    @Override
    @GetMapping("/get/{type}")
    public SysDictionary getByType(@PathVariable("type") String type) {
        return sysDictionaryService.getByType(type);
    }

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult list(@PathVariable("page") int page, @PathVariable("size") int size,
                                    SysDictionary sysDictionary) {
        return sysDictionaryService.list(page,size,sysDictionary);
    }

    @Override
    @PostMapping("/add")
    public SysDictionaryResult add(@RequestBody SysDictionary sysDictionary) {
        return sysDictionaryService.add(sysDictionary);
    }

    @Override
    @PutMapping("/edit/{id}")
    public SysDictionaryResult edit(@PathVariable("id") String id,@RequestBody SysDictionary sysDictionary) {
        return  sysDictionaryService.edit(id,sysDictionary);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult del(@PathVariable("id") String id) {
        return sysDictionaryService.del(id);
    }

    @Override
    @PostMapping("/{id}/value/add")
    public SysDictionaryResult addValue(@PathVariable("id") String id,@RequestBody SysDictionaryValue sysDictionaryValue) {
        return sysDictionaryService.addValue(id,sysDictionaryValue);
    }

    @Override
    @PutMapping("/{id}/value/edit/{sdId}")
    public SysDictionaryResult editValue(@PathVariable("id") String id,@PathVariable("sdId")  String sdId,@RequestBody SysDictionaryValue sysDictionaryValue) {
        return sysDictionaryService.editValue(id,sdId,sysDictionaryValue);
    }

    @Override
    @DeleteMapping("/{id}/value/del/{sdId}")
    public ResponseResult delValue(@PathVariable("id") String id,@PathVariable("sdId")  String sdId) {
        return sysDictionaryService.delValue(id,sdId);
    }

}
