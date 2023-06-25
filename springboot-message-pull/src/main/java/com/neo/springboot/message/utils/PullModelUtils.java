package com.neo.springboot.message.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.neo.springboot.message.data.Message;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.neo.springboot.message.data.MockData.*;

/**
 * @author: neo
 * @description
 * @date: 2023/6/25 10:09
 */

public class PullModelUtils {

    /**
     * 发送消息
     *
     * @param userId
     * @param message
     */
    public static void sendMessage(final String userId, final Message message) {
        // 发送时间
        long sendTime = System.currentTimeMillis();
        message.setSendTime(sendTime);

        // 存到自己的箱子
        List<Message> messageBox = USER_SEND_BOX.get(userId);
        messageBox.add(message);

    }

    /**
     * 展示消息盒子的消息
     *
     * @param userId
     */
    public static String showMessageInBox(final String userId) {
        String userName = USER_DATA.get(userId);
        if (StrUtil.isNotBlank(userName)) {
            List<Message> messages = USER_IN_BOX.get(userId);
            return messages.isEmpty() ? "暂无消息" : JSONUtil.toJsonStr(messages);
        } else {
            return "userId错误";
        }
    }

    /**
     * 拉取消息
     * @param userId
     * @return
     */
    public static String pullMessage(final String userId) {
        String userName = USER_DATA.get(userId);
        if (StrUtil.isNotBlank(userName)) {
            // 获取关注列表
            Set<String> relations = USER_RELATIONS.get(userId);
            if (relations.isEmpty()) {
                return "还没关注任何人";
            } else {
                List<Message> curUserBox = USER_IN_BOX.get(userId);
                // 根据关注列表获取消息
                relations.forEach(friendId -> {
                    List<Message> friendMessage = USER_SEND_BOX.get(friendId);
                    curUserBox.addAll(friendMessage);
                });
                // 根据发送时间排序(升序)
                curUserBox.sort(Comparator.comparingLong(Message::getSendTime));
                // 反转-》降序
                Collections.reverse(curUserBox);

                return "ok";
            }
        } else {
            return "userId错误";
        }
    }

}
