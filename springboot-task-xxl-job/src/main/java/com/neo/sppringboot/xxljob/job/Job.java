package com.neo.sppringboot.xxljob.job;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: neo
 * @description 测试xxl-job任务
 * @date: 2023/6/29 21:48
 */
@Component
@Slf4j
public class Job {
    /**
     * 简单模式（Bean模式）
     *
     * @throws Exception
     */
    @XxlJob("sendMessage")
    public void myJobHandler() throws Exception {
        log.info("开始执行..........");
        log.info("发送短信，发送时间:" + DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        log.info("执行完成..........");
    }
}
