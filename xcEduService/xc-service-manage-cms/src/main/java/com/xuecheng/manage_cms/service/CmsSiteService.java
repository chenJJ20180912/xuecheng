package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QuerySiteRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsSiteResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CmsSiteService {

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    public QueryResult<CmsSite> findAll() {
        List<CmsSite> all = cmsSiteRepository.findAll();
        QueryResult<CmsSite> result = new QueryResult<CmsSite>();
        result.setList(all);
        return result;
    }

    public QueryResponseResult findList(int page, int size, QuerySiteRequest querySiteRequest) {
        if (querySiteRequest == null) {
            querySiteRequest = new QuerySiteRequest();
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("siteName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("siteDomain", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("sitePort", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("siteWebPath", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsSite cmsSite = new CmsSite();
        //设置条件值（站点id）
        if (StringUtils.isNotEmpty(querySiteRequest.getSiteName())) {
            cmsSite.setSiteName(querySiteRequest.getSiteName());
        }
        //设置模板id作为查询条件
        if (StringUtils.isNotEmpty(querySiteRequest.getSiteDomain())) {
            cmsSite.setSiteDomain(querySiteRequest.getSiteDomain());
        }
        //设置页面别名作为查询条件
        if (StringUtils.isNotEmpty(querySiteRequest.getSitePort())) {
            cmsSite.setSitePort(querySiteRequest.getSitePort());
        }
        //设置页面别名作为查询条件
        if (StringUtils.isNotEmpty(querySiteRequest.getSiteWebPath())) {
            cmsSite.setSiteWebPath(querySiteRequest.getSiteWebPath());
        }
        //定义条件对象Example
        Example<CmsSite> example = Example.of(cmsSite, exampleMatcher);
        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsSite> all = cmsSiteRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    public CmsSiteResult add(CmsSite cmsSite) {
        if (cmsSite == null) {
            ExceptionCast.cast(CmsCode.CMS_PARAMS_IS_NULL);
        }
        CmsSite cmsSite1 = cmsSiteRepository.findBySiteDomainAndSitePortAndSiteWebPath(
                cmsSite.getSiteDomain(), cmsSite.getSitePort(), cmsSite.getSiteWebPath());
        if (cmsSite1 != null) {
            //抛出异常，异常内容就是页面已经存在
            ExceptionCast.cast(CmsCode.CMS_SITE_EXISTS);
        }
        cmsSite.setSiteId(null);
        cmsSiteRepository.save(cmsSite);
        return new CmsSiteResult(CommonCode.SUCCESS, cmsSite);

    }

    public CmsSiteResult getById(String id) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_SITE_NOT_EXISTS);
        }
        CmsSite cmsSite = optional.get();
        return new CmsSiteResult(CommonCode.SUCCESS, cmsSite);
    }

    public CmsSiteResult update(String id, CmsSite cmsSite) {
        //根据id从数据库查询页面信息
        CmsSite one = this.getById(id).getCmsSite();
        one.setSiteDomain(cmsSite.getSiteDomain());
        one.setSiteName(cmsSite.getSiteName());
        one.setSitePort(cmsSite.getSitePort());
        one.setSiteWebPath(cmsSite.getSiteWebPath());
        one.setSitePhysicalPath(cmsSite.getSitePhysicalPath());
        //提交修改
        cmsSiteRepository.save(one);
        return new CmsSiteResult(CommonCode.SUCCESS, one);
    }

    //根据id删除页面
    public ResponseResult delete(String id) {

        //先查询一下
        Optional<CmsSite> optional = cmsSiteRepository.findById(id);
        if (optional.isPresent()) {
            cmsSiteRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
