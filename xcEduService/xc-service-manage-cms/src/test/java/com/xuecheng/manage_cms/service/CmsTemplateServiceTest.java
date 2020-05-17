package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.model.response.QueryResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsTemplateServiceTest {

    @Autowired
    private CmsTemplateService cmsTemplateService;

    // 测试上传
    @Test
    public void testUpload() throws FileNotFoundException {
        String templateId = "5eb816b2d80a7d2b485c9166";
        String fileName = "C:\\workspace\\xcEduService\\xc-service-manage-cms\\src\\test\\resources\\templates\\index_banner.ftl";
        FileInputStream fis = new FileInputStream(new File(fileName));
        cmsTemplateService.uploadTemplate(templateId,fis);
    }

    @Test
    public void testDownload(){
        String templateId = "5eb816b2d80a7d2b485c9166";
        System.out.println(cmsTemplateService.queryTemplate(templateId));
    }
}
