package com.neo.springboot.websocket.chat.server;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: neo
 * @description websocket功能实现类
 * @date: 2023/7/15 14:57
 */
@Component
@Slf4j
@ServerEndpoint("/wb/{username}") //暴露的ws应用的路径
public class Websocket {

    /**
     * 大致说明：
     *  服务端开启websocket服务，客户端通过ServerEndpoint连接到服务端
     *  服务端存储并转发客户端发送过来的消息
     *  流程：某一个客户端想发送数据给另一个客户端
     *  1. 发送消息的客户端发送消息到服务端
     *  2. 服务端根据客户端提供的目标用户找到接收消息的客户端
     *  3. 将消息转发给目标客户端
     */

    // 当前在线用户存储
    private final static Map<String,Session> onlineUser = new ConcurrentHashMap<>();

    /**
     * 客户端与服务端连接成功
     * @param session
     * @param username
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username){
        /*
            do something for onOpen
            与当前客户端连接成功时
         */
        // 连接成功就将用户添加进在线集合
        onlineUser.put(username,session);
        log.info("用户 {} 登录成功",username);
        // 更新在线用户
        Set<String> onlineUsername = onlineUser.keySet();
        HashMap<String, Object> onlineMap = new HashMap<>();
        onlineMap.put("onlineUsername",onlineUsername);
        onlineMap.put("sys",true);
        log.info(JSONUtil.toJsonStr(onlineMap));
        sendBroadcasting(JSONUtil.toJsonStr(onlineMap));

    }

    /**
     * 客户端与服务端连接关闭
     * @param session
     * @param username
     */
    @OnClose
    public void onClose(Session session,@PathParam("username") String username){
        /*
            do something for onClose
            与当前客户端连接关闭时
         */

        // 连接断开就将用户移除进在线集合
        onlineUser.remove(username);
        log.info("用户 {} 退出成功",username);
        // 更新在线用户
        Set<String> onlineUsername = onlineUser.keySet();
        HashMap<String, Object> onlineMap = new HashMap<>();
        onlineMap.put("onlineUsername",onlineUsername);
        onlineMap.put("sys",true);
        sendBroadcasting(JSONUtil.toJsonStr(onlineMap));
    }

    /**
     * 客户端与服务端连接异常
     * @param error
     * @param session
     * @param username
     */
    @OnError
    public void onError(Throwable error,Session session,@PathParam("username") String username) {
        log.debug(error.getMessage());
    }

    /**
     * 客户端向服务端发送消息
     * @param message
     * @param username
     * @throws IOException
     */
    @OnMessage
    public void onMsg(Session session, String message, @PathParam("username") String username) throws IOException {
        /*
            do something for onMessage
            收到来自当前客户端的消息时
         */
        JSONObject parseObj = JSONUtil.parseObj(message);
        String targetUsername = (String) parseObj.get("targetUsername");
        log.info(message);
        // 寻找目标用户
        Session targetUserSession = onlineUser.get(targetUsername.trim());
        if (targetUserSession == null){
            session.getBasicRemote().sendText("用户不在线，或者不在线");
            return;
        }
        // 转发给目标用户
        targetUserSession.getBasicRemote().sendText(JSONUtil.toJsonStr(message));
    }

    private void sendBroadcasting(String message){
        onlineUser.forEach((s, session) -> {
            if (session!=null && session.isOpen()){
                session.getAsyncRemote().sendText(message);
            }
        });
    }

}
