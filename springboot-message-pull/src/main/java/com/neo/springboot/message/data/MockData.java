package com.neo.springboot.message.data;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;

/**
 * @author: neo
 * @description 模拟用户关注或者好友信息
 * @date: 2023/6/25 9:54
 */

@Data
@Accessors(chain = true)
public class MockData {

    // 用户数据
    public final static Map<String,String> USER_DATA = new HashMap<>();

    // 用户关注数据
    public final static Map<String, Set<String>> USER_RELATIONS = new HashMap<>();

    // 用户发件箱（用来存放发出的消息，每个用户一个）
    public final static Map<String, List<Message>> USER_SEND_BOX = new HashMap<>();

    // 用户收件箱（用来存放收到的消息，每个用户一个）
    public final static Map<String, List<Message>> USER_IN_BOX = new HashMap<>();

    static {
        // 初始化数据
        USER_DATA.put("1","张三");
        USER_DATA.put("2","李四");
        USER_DATA.put("3","王五");

        // 初始化关系
        HashSet<String> relations = new HashSet<>();
        relations.add("2");
        relations.add("3");
        USER_RELATIONS.put("1",relations);

        // 分配箱子
        USER_DATA.forEach((key, value)->{
            USER_SEND_BOX.put(key,new ArrayList<>());
            USER_IN_BOX.put(key,new ArrayList<>());
        });
    }

}
