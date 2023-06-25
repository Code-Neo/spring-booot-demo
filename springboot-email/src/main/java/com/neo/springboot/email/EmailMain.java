package com.neo.springboot.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: neo
 * @description 使用Java发送邮件，包括文本和HTML
 * @date: 2023/6/25 14:38
 */
@SpringBootApplication
public class EmailMain {
    public static void main(String[] args){
        SpringApplication.run(EmailMain.class,args);
    }

}
