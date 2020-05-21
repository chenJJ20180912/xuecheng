package com.xuecheng.manage_course.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@MapperScan("com.xuecheng.manage_course.dao")
@Configuration
public class DaoConfig {
}
