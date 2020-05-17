package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryTemplateRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsTemplateResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPagePostLogRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CmsTemplateService {

    private Logger logger = LoggerFactory.getLogger(CmsTemplateService.class);

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Autowired
    private CmsPagePostLogRepository cmsPagePostLogRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private CmsPageService cmsPageService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public QueryResult<CmsTemplate> findAll() {
        List<CmsTemplate> all = cmsTemplateRepository.findAll();
        QueryResult<CmsTemplate> result = new QueryResult<CmsTemplate>();
        result.setList(all);
        return result;
    }

    public QueryResponseResult findList(int page, int size, QueryTemplateRequest queryTemplateRequest) {
        if (queryTemplateRequest == null) {
            queryTemplateRequest = new QueryTemplateRequest();
        }
        //自定义条件查询
        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("templateName", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsTemplate cmsTemplate = new CmsTemplate();
        if (StringUtils.isNotEmpty(queryTemplateRequest.getSiteId())) {
            cmsTemplate.setSiteId(queryTemplateRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryTemplateRequest.getTemplateName())) {
            cmsTemplate.setTemplateName(queryTemplateRequest.getTemplateName());
        }
        //定义条件对象Example
        Example<CmsTemplate> example = Example.of(cmsTemplate, exampleMatcher);
        //分页参数
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsTemplate> all = cmsTemplateRepository.findAll(example, pageable);//实现自定义条件查询并且分页查询
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }

    //新增页面
    public CmsTemplateResult add(CmsTemplate cmsTemplate) {
        if (cmsTemplate == null) {
            //抛出异常，非法参数异常..指定异常信息的内容
            ExceptionCast.cast(CmsCode.CMS_PARAMS_IS_NULL);
        }
        CmsTemplate cmsTemplate1 = cmsTemplateRepository.findBySiteIdAndTemplateName(
                cmsTemplate.getSiteId(), cmsTemplate.getTemplateName());
        if (cmsTemplate1 != null) {
            //页面已经存在
            ExceptionCast.cast(CmsCode.CMS_TEMPLATE_EXISTS);
        }

        //调用dao新增页面
        cmsTemplate.setTemplateId(null);
        cmsTemplateRepository.save(cmsTemplate);
        return new CmsTemplateResult(CommonCode.SUCCESS, cmsTemplate);

    }

    //根据页面id查询页面
    public CmsTemplateResult getById(String id) {
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_TEMPLATE_NOT_EXISTS);
        }
        CmsTemplate cmsTemplate = optional.get();
        return new CmsTemplateResult(CommonCode.SUCCESS, cmsTemplate);
    }

    //修改页面
    public CmsTemplateResult update(String id, CmsTemplate cmsTemplate) {
        //根据id从数据库查询页面信息
        CmsTemplate one = this.getById(id).getCmsTemplate();
        one.setTemplateId(cmsTemplate.getTemplateId());
        one.setSiteId(cmsTemplate.getSiteId());
        one.setTemplateName(cmsTemplate.getTemplateName());
        one.setTemplateParameter(cmsTemplate.getTemplateParameter());
        one.setTemplateFileId(cmsTemplate.getTemplateFileId());
        //提交修改
        cmsTemplateRepository.save(one);
        return new CmsTemplateResult(CommonCode.SUCCESS, one);
    }

    //根据id删除页面
    public ResponseResult delete(String id) {
        //先查询一下
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(id);
        if (optional.isPresent()) {
            cmsTemplateRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    public ResponseResult uploadTemplate(String id, MultipartFile file) {
        CmsTemplate one = this.getById(id).getCmsTemplate();
        String templateFileId = one.getTemplateFileId();
        try (InputStream is = file.getInputStream()) {
            // 先删除后重新绑定id
            if (StringUtils.isNotEmpty(templateFileId)) {
                gridFsTemplate.delete(Query.query(Criteria.where("_id").is(templateFileId)));
            }
            ObjectId storeId = gridFsTemplate.store(is, one.getTemplateName(), "");
            one.setTemplateFileId(storeId.toString());
            cmsTemplateRepository.save(one);
            return new ResponseResult(CommonCode.SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_TEMPLATE_FILE_UPLOAD_FAILD);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    public ResponseResult uploadTemplate(String id, InputStream is) {
        CmsTemplate one = this.getById(id).getCmsTemplate();
        String templateFileId = one.getTemplateFileId();
        // 先删除后重新绑定id
        if (StringUtils.isNotEmpty(templateFileId)) {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(templateFileId)));
        }
        ObjectId storeId = gridFsTemplate.store(is, one.getTemplateName(), "");
        one.setTemplateFileId(storeId.toString());
        cmsTemplateRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public String queryTemplate(String templateId) {
        CmsTemplate one = this.getById(templateId).getCmsTemplate();
        if (StringUtils.isNotEmpty(one.getTemplateFileId())) {
            //根据id查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(one.getTemplateFileId())));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream =
                    gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建gridFsResource，用于获取流对象
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
            try (InputStream inputStream = gridFsResource.getInputStream()) {
                //获取流中的数据
                String fileData = IOUtils.toString(inputStream, "UTF-8");
                return fileData;
            } catch (Throwable e) {
                e.printStackTrace();
                ExceptionCast.cast(CmsCode.CMS_TEMPLATE_FILE_LOAD_FAILD);
            }
        }
        return null;
    }

    public String previewPage(String pageId) {
        // freemarker 生成内容需要模板和数据
        // 根据pageId获取到模板id和dataUrl
        CmsPage cmsPage = cmsPageService.getById(pageId).getCmsPage();
        // 1.根据模板id 获取模板字符串
        String templateStr = this.queryTemplate(cmsPage.getTemplateId());
        if (StringUtils.isEmpty(templateStr)) {
            ExceptionCast.cast(CmsCode.CMS_TEMPLATE_FILE_NOT_EXISTS);
        }
        // 2.根据dataUrl 获取数据
        Map dataModel = loadDataModel(cmsPage.getDataUrl());
        return generateHTML(templateStr, dataModel);
    }

    private Map loadDataModel(String dataUrl) {
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        if (HttpStatus.OK != entity.getStatusCode()) {
            logger.error(String.format("request url[%s] error!\ndetails:\n\t%s", dataUrl, entity.getStatusCode().toString()));
            ExceptionCast.cast(CommonCode.NET_ERROR);
        }
        return entity.getBody();
    }

    private String generateHTML(String templateStr, Map model) {
        // 获取配置对象
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        // 获取模板内容
        // 使用StringTemplateLoader 构建Template对象
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateStr);
        // 将模板加载器绑定到配置对象上
        configuration.setTemplateLoader(stringTemplateLoader);
        try {
            // 获取模板
            Template template = configuration.getTemplate("template");
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_TEMPLATE_FILE_CONVERT_FAIL);
        }
        return null;
    }

    // 发布页面
    public ResponseResult postPage(String pageId) {
        // 1.获取预览html
        String htmlContent = this.previewPage(pageId);
        // 2.查找cmsPage信息
        CmsPage cmsPage = cmsPageService.getById(pageId).getCmsPage();
        String newHtmlFileId = "";
        try (InputStream is = IOUtils.toInputStream(htmlContent, "utf-8")) {
            // 3..将预览的html存储到gridFS中
            ObjectId store = gridFsTemplate.store(is, cmsPage.getPageName());
            newHtmlFileId = store.toString();
        } catch (IOException e) {
            ExceptionCast.cast(CmsCode.CMS_TEMPLATE_FILE_UPLOAD_FAILD);
        }
        String htmlFileId = cmsPage.getHtmlFileId();
        if (StringUtils.isNotEmpty(htmlFileId)) {
            // 删除旧的文件信息
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        // 4.更新page中的htmlFileId字段
        cmsPage.setHtmlFileId(newHtmlFileId);
        cmsPageRepository.save(cmsPage);
        // 5. 清空上一次的发布日志
        cmsPagePostLogRepository.deleteByPageId(pageId);
        // 6.发送消息提醒客户端程序响应
        Map map = new HashMap();
        map.put("pageId", pageId);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_CMS_PAGE_POST, cmsPage.getSiteId(),
                JSON.toJSONString(map));
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
