package com.xuecheng.govern.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer //开启EurekaServer支持
@SpringBootApplication
public class GovernApp {

    public static void main(String[] args) {
        SpringApplication.run(GovernApp.class,args);
    }
}
