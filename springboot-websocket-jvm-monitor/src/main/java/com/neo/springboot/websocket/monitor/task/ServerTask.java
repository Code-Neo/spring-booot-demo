package com.neo.springboot.websocket.monitor.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONUtil;
import com.neo.springboot.websocket.monitor.bean.Server;
import com.neo.springboot.websocket.monitor.commons.WebSocketConstants;
import com.neo.springboot.websocket.monitor.payload.ServerVO;
import com.neo.springboot.websocket.monitor.utils.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: neo
 * @description 定时刷新服务器数据
 * @date: 2023/7/15 11:41
 */
@Slf4j
@Component
public class ServerTask {

    @Autowired
    private SimpMessagingTemplate wsTemplate;

    /**
     * 按照标准时间来算，每隔 2s 执行一次
     */
    @Scheduled(cron = "0/2 * * * * ?")
    public void websocket() throws Exception {
        log.info("【推送消息】开始执行：{}", DateUtil.formatDateTime(new Date()));
        // 查询服务器状态
        Server server = new Server();
        server.copyTo();
        ServerVO serverVO = ServerUtil.wrapServerVO(server);
        Dict dict = ServerUtil.wrapServerDict(serverVO);
        wsTemplate.convertAndSend(WebSocketConstants.PUSH_SERVER, JSONUtil.toJsonStr(dict));
        log.info("【推送消息】执行结束：{}", DateUtil.formatDateTime(new Date()));
    }
}

