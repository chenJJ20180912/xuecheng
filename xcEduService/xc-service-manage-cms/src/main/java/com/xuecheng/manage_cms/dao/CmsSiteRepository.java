package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {

    /**
     * 根据域 端口 和访问地址查找
     * @param siteDomain
     * @param sitePort
     * @param siteWebPath
     * @return
     */
    CmsSite findBySiteDomainAndSitePortAndSiteWebPath(String siteDomain,String sitePort,String siteWebPath);
}
