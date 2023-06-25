package com.neo.springboot.message.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.neo.springboot.message.data.Message;
import com.neo.springboot.message.utils.PullModelUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.neo.springboot.message.data.MockData.USER_DATA;

/**
 * @author: neo
 * @description 测试拉模式
 * @date: 2023/6/25 10:52
 */

@RestController
public class TestController {


    @GetMapping("send")
    public String sendMessage(String userId,String msg){
        String username = USER_DATA.get(userId);
        if (StrUtil.isBlank(username)){
            return "错误的userId";
        }

        Message message = new Message();
        message.setMessage(msg)
                .setName(username)
                .setMessageId(UUID.fastUUID().toString(true));

        PullModelUtils.sendMessage(userId,message);
        return "ok";
    }

    @GetMapping("show")
    public String showMessage(String userId){
        return PullModelUtils.showMessageInBox(userId);
    }


    @GetMapping("pull")
    public String pullMessage(String userId){
        return PullModelUtils.pullMessage(userId);
    }

}
