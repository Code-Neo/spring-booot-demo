package com.neo.springboot.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: neo
 * @description 秒杀模块
 * @date: 2023/7/16 15:41
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
public class SeckillMain {
    public static void main(String[] args){
        SpringApplication.run(SeckillMain.class,args);
    }
}
