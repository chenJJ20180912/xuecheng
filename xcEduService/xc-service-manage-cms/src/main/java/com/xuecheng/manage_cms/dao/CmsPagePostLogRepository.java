package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPagePostLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsPagePostLogRepository extends MongoRepository<CmsPagePostLog,String> {

    List<CmsPagePostLog> findByPageId(String pageId);

    void deleteByPageId(String pageId);
}
