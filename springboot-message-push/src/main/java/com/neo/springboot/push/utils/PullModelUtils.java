package com.neo.springboot.push.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.neo.springboot.push.data.Message;

import java.util.*;

import static com.neo.springboot.push.data.MockData.*;


/**
 * @author: neo
 * @description
 * @date: 2023/6/25 10:09
 */

public class PullModelUtils {


    /**
     * 展示消息盒子的消息
     *
     * @param userId
     */
    public static String showMessageInBox(final String userId) {
        String userName = USER_DATA.get(userId);
        if (StrUtil.isNotBlank(userName)) {
            List<Message> messages = USER_IN_BOX.get(userId);
            messages.sort(Comparator.comparingLong(Message::getSendTime));
            Collections.reverse(messages);
            return messages.isEmpty() ? "暂无消息" : JSONUtil.toJsonStr(messages);
        } else {
            return "userId错误";
        }
    }

    /**
     * 推送消息
     * @param message
     * @return
     */
    public static String pushMessage(final String userId,final Message message) {
        // 发送时间
        long sendTime = System.currentTimeMillis();
        message.setSendTime(sendTime);

        String username = USER_DATA.get(userId);
        if (StrUtil.isNotBlank(username)){
            // 获取粉丝/关注者列表
            Set<String> relations = USER_FENS_RELATIONS.get(userId);
            if (!Objects.isNull(relations) && !relations.isEmpty()){
                relations.forEach(friendId ->{
                    // 推送消息到关注者邮箱
                    List<Message> messages = USER_IN_BOX.get(friendId);
                    messages.add(message);
                });
                return "ok";
            }else {
                return "没有关注者";
            }


        }else {
            return "userId错误";
        }
    }

}
