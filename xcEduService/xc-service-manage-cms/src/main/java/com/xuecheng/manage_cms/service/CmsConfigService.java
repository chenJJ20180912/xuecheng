package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.request.QueryConfigRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsConfigResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CmsConfigService {

    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    public QueryResult<CmsConfig> findAll() {
        List<CmsConfig> all = cmsConfigRepository.findAll();
        QueryResult<CmsConfig> result = new QueryResult<CmsConfig>();
        result.setList(all);
        return result;
    }

    public QueryResponseResult findList(int page, int size, QueryConfigRequest queryConfigRequest) {
        if (queryConfigRequest == null) {
            queryConfigRequest = new QueryConfigRequest();
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("id", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsConfig cmsConfig = new CmsConfig();
        //设置条件值（站点id）
        if (StringUtils.isNotEmpty(queryConfigRequest.getId())) {
            cmsConfig.setId(queryConfigRequest.getId());
        }
        //设置模板id作为查询条件
        if (StringUtils.isNotEmpty(queryConfigRequest.getName())) {
            cmsConfig.setName(queryConfigRequest.getName());
        }
        //定义条件对象Example
        Example<CmsConfig> example = Example.of(cmsConfig, exampleMatcher);
        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsConfig> all = cmsConfigRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    public CmsConfigResult add(CmsConfig cmsConfig) {
        if (cmsConfig == null) {
            ExceptionCast.cast(CmsCode.CMS_PARAMS_IS_NULL);
        }
        cmsConfig.setId(null);
        cmsConfigRepository.save(cmsConfig);
        return new CmsConfigResult(CommonCode.SUCCESS, cmsConfig);

    }

    public CmsConfigResult getById(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_SITE_NOT_EXISTS);
        }
        CmsConfig cmsConfig = optional.get();
        return new CmsConfigResult(CommonCode.SUCCESS, cmsConfig);
    }

    public CmsConfigResult update(String id, CmsConfig cmsConfig) {
        //根据id从数据库查询页面信息
        CmsConfig one = this.getById(id).getCmsConfig();
        one.setName(cmsConfig.getName());
        one.setModel(cmsConfig.getModel());
        //提交修改
        cmsConfigRepository.save(one);
        return new CmsConfigResult(CommonCode.SUCCESS, one);
    }

    //根据id删除页面
    public ResponseResult delete(String id) {

        //先查询一下
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (optional.isPresent()) {
            cmsConfigRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
