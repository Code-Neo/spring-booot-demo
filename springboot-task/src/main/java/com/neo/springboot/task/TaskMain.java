package com.neo.springboot.task;

import cn.hutool.core.date.DateUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

/**
 * @author: neo
 * @description 测试springboot的定时任务功能
 * @date: 2023/6/28 15:19
 */

@SpringBootApplication
public class TaskMain {
    public static void main(String[] args){
        SpringApplication.run(TaskMain.class,args);
        System.out.println(DateUtil.formatDateTime(new Date()));
    }
}
