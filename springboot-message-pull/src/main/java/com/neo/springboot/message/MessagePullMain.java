package com.neo.springboot.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: neo
 * @description 消息接收方式之-拉模式
 * @date: 2023/6/25 9:39
 */
@SpringBootApplication
public class MessagePullMain {
    public static void main(String[] args) {
        SpringApplication.run(MessagePullMain.class, args);
    }
}
