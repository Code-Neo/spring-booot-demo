package com.neo.springboot.message.data;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: neo
 * @description 发送的消息类
 * @date: 2023/6/25 10:11
 */

@Data
@Accessors(chain = true)
public class Message {

    // 唯一标识
    private String messageId;

    // 发送人
    private String name;

    // 发送的消息
    private String message;

    // 发送的时间
    private Long sendTime;

}
