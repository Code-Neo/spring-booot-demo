package com.neo.springboot.job;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * @author: neo
 * @description 第一个任务
 * @date: 2023/6/29 1:05
 */
@Slf4j
public class HelloJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("我的第一个任务正在运行，线程名为：{}，时间：{}", Thread.currentThread(), DateUtil.formatDateTime(new Date()));
    }
}
