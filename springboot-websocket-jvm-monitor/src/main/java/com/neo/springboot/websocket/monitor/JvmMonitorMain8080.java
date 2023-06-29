package com.neo.springboot.websocket.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: neo
 * @description Java内存情况监视器
 * @date: 2023/7/15 10:37
 */
@SpringBootApplication
@EnableScheduling
public class JvmMonitorMain8080 {
    public static void main(String[] args) {
        SpringApplication.run(JvmMonitorMain8080.class, args);
    }
}
