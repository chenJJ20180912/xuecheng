package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
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
public class SysServiceTest {

    @Autowired
    private SysDictionaryService sysDictionaryService;

    // 测试上传
    @Test
    public void testGetByType() {
        SysDictionary byType = sysDictionaryService.getByType("200");
        System.out.println(byType);
    }

}
