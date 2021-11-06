package com.atguigu.yygh.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author dzwstart
 * @date 2021/11/6 - 16:07
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu")
@EnableDiscoveryClient //此注解用来注册服务的
@EnableFeignClients(basePackages = "com.atguigu")//启动服务调用
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class,args);
    }
}
