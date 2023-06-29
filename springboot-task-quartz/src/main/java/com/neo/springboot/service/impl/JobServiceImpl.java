package com.neo.springboot.service.impl;

import com.neo.springboot.bean.JobForm;
import com.neo.springboot.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: neo
 * @description 定时任务实现类
 * @date: 2023/6/29 1:17
 */

@Slf4j
@Service
public class JobServiceImpl implements JobService {

    @Resource
    private Scheduler scheduler;

    /**
     * 添加并启动定时任务
     *
     * @param form 表单参数 {@link JobForm}
     * @throws Exception 异常
     */
    @Override
    public void addJob(JobForm form) throws Exception {
        // 启动任务调度器
        scheduler.start();

        // 根据全类名获取类对象，并创建类实例
        Class<?> clazz = Class.forName(form.getJobClassName());
        Job job = (Job) clazz.getDeclaredConstructor().newInstance();

        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(job.getClass())
                // 1. 任务名称 2. 任务所在的组
                .withIdentity(form.getJobClassName(), form.getJobGroupName())
                .build();

        // Cron表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(form.getCronExpression());


        //根据Cron表达式构建一个Trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(form.getJobClassName(), form.getJobGroupName())
                .withSchedule(cronScheduleBuilder)
                .build();

        try {
            // 创建定时任务
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("【定时任务】创建失败！", e);
            throw new Exception("【定时任务】创建失败！");
        }
    }

    /**
     * 删除定时任务
     *
     * @param form 表单参数 {@link JobForm}
     * @throws SchedulerException 异常
     */
    @Override
    public void deleteJob(JobForm form) throws SchedulerException {
        // 暂停正在运行的任务的触发器
        scheduler.pauseTrigger(TriggerKey.triggerKey(form.getJobClassName(), form.getJobGroupName()));

        // 从调度器中移除任务（job）
        scheduler.unscheduleJob(TriggerKey.triggerKey(form.getJobClassName(), form.getJobGroupName()));

        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(form.getJobClassName(), form.getJobGroupName()));
    }

    /**
     * 暂停定时任务
     *
     * @param form 表单参数 {@link JobForm}
     * @throws SchedulerException 异常
     */
    @Override
    public void pauseJob(JobForm form) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(form.getJobClassName(), form.getJobGroupName()));
    }

    /**
     * 恢复定时任务
     *
     * @param form 表单参数 {@link JobForm}
     * @throws SchedulerException 异常
     */
    @Override
    public void resumeJob(JobForm form) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(form.getJobClassName(), form.getJobGroupName()));
    }

    /**
     * 重新配置定时任务
     *
     * @param form 表单参数 {@link JobForm}
     * @throws Exception 异常
     */
    @Override
    public void cronJob(JobForm form) throws Exception {
        try {
            // 创建新的cron定时任务
            CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(form.getCronExpression());

            // 需要重新配置的TriggerKey
            TriggerKey triggerKey = TriggerKey.triggerKey(form.getJobClassName(), form.getJobGroupName());

            // 获取已存在的触发器（trigger）
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            // 重新配置触发器
            CronTrigger newCronTrigger = trigger.getTriggerBuilder()
                    .withIdentity(triggerKey)
                    .withSchedule(cron)
                    .build();
            // 按新的trigger重新设置job执行
            scheduler.rescheduleJob(triggerKey, newCronTrigger);

        } catch (SchedulerException e) {
            log.error("【定时任务】更新失败！", e);
            throw new Exception("【定时任务】创建失败！");
        }

    }
}
