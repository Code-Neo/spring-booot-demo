package com.neo.springboot.websocket.chat.bean;

import lombok.Data;

/**
 * @author: neo
 * @description 消息类
 * @date: 2023/7/15 14:58
 */
@Data
public class Message {
    // 消息id
    private String id;

    // 发出者用户名
    private String sendUsername;

    // 目标用户user
    private String targetUser;

    // 消息
    private String msg;
}
