package com.neo.springboot.task.job;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: neo
 * @description 定时任务
 * @date: 2023/6/28 15:22
 */
@Slf4j
@Component
public class Task {

    /**
     * 按照标准时间来算，每隔 10s 执行一次
     */
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void job1() {
//        log.info("【job1】开始执行：{}", DateUtil.formatDateTime(new Date()));
//    }
//
//    /**
//     * 从启动时间开始，间隔 2s 执行
//     * 每次任务执行的间隔为2s，即使第一个任务花了十秒，第二个任务也会在第一个任务的执行期间的第二秒执行
//     */
//    @Scheduled(fixedRate = 2000)
//    public void job2() {
//        log.info("【job2】开始执行：{}", DateUtil.formatDateTime(new Date()));
//    }

    /**
     * 从启动时间开始，延迟 5s 后间隔 4s 执行
     * 固定等待时间
     * fixedDelay：第一次执行开始需要等待的时间
     * initialDelay：第一次执行完后，之后每间隔四秒执行
     */
    @Scheduled(fixedDelay = 4000, initialDelay = 5000)
    public void job3() {
        log.info("【job3】开始执行：{}", DateUtil.formatDateTime(new Date()));
    }
}
