package com.atguigu.yygh.hosp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author dzwstart
 * @date 2021/8/25 - 16:08
 */

//添加启动类

    @SpringBootApplication
    @ComponentScan(basePackages = "com.atguigu")
    @EnableDiscoveryClient //此注解用来注册服务的
    @EnableFeignClients(basePackages = "com.atguigu")//启动服务调用
public class ServiceHospApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHospApplication.class,args);
    }

}
