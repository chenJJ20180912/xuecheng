package com.xuecheng.manager_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manager_cms_client.dao.CmsPageRepository;
import com.xuecheng.manager_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class CmsPageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;


    /**
     * 保存文件到站点服务器
     *
     * @param pageId
     */
    public void savePageToServerPath(String pageId) {
        // 根据页面Id查找页面信息
        CmsPage cmsPage = findCmsPageById(pageId);
        //根据页面信息获取站点信息
        CmsSite cmsSite = findCmsSiteById(cmsPage.getSiteId());
        //根据页面信息和站点信息拼接出文件应该下载到服务器的哪个位置
        String filePath = cmsSite.getSitePhysicalPath() + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        //根据页面信息内的静态化界面Id 从gridFS中下载内容
        InputStream is = downHtmlContent(cmsPage.getHtmlFileId());
        //检查文件是否存在，如果不存在级联创建文件和上级文件夹
        FileOutputStream fos = null;
        try {
            checkFileExits(filePath,true);
            fos = new FileOutputStream(new File(filePath));
            IOUtils.copy(is,fos);
            fos.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkFileExits(String filePath,boolean isFile) throws IOException {
        File file = new File(filePath);
        if(!file.exists()){
            // 检查父级路径是否存在
            checkFileExits(file.getParent(),false);
            if(isFile){
                file.createNewFile();
            }else{
                file.mkdir();
            }
        }
    }

    // 根据页面id查找页面信息
    private CmsPage findCmsPageById(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOT_EXISTS);
        }
        return optional.get();
    }

    // 根据站点id查找站点信息
    private CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_SITE_NOT_EXISTS);
        }
        return optional.get();
    }

    // 获取文件的下载流
    private InputStream downHtmlContent(String htmlFileId) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ExceptionCast.cast(CmsCode.CMS_TEMPLATE_FILE_DOWN_FAILD);
        return null;
    }
}
