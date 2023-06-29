package com.neo.sppringboot.xxljob.job;

import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author: neo
 * @description 测试继承类的方法常见任务
 * @date: 2023/6/29 23:05
 */
@Slf4j
public class TestExtentJob extends IJobHandler {

    /**
     * 任务体
     * @throws Exception
     */
    @Override
    public void execute() throws Exception {
        log.debug("now:{}", DateUtil.formatDate(new Date()));
        log.debug("这个一个使用继承实现的任务正在执行");
    }
}
