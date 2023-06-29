package com.neo.sppringboot.xxljob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: neo
 * @description xx-job任务调度启动器
 *
 * 使用前先启动xxl-job-admin项目，访问http://localhost:8080/xxl-job-admin/
 *
 *
 * @date: 2023/6/29 17:41
 */
@SpringBootApplication
public class XXLJobMain {
    public static void main(String[] args){
        SpringApplication.run(XXLJobMain.class,args);
    }
}
