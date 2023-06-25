package com.neo.springboot.aoplog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: neo
 * @description 基于注解的Aop日志收集
 * @date: 2023/6/24 19:58
 */
@SpringBootApplication
public class AppLogAnnotationMain {
    public static void main(String[] args){
        SpringApplication.run(AppLogAnnotationMain.class,args);
    }
}
