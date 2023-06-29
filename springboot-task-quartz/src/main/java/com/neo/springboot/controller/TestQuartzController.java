package com.neo.springboot.controller;

import com.neo.springboot.bean.JobForm;
import com.neo.springboot.service.JobService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: neo
 * @description 测试定时任务
 * @date: 2023/6/29 1:50
 */

@RestController
public class TestQuartzController {


    @Resource
    private JobService jobService;

    @GetMapping("add")
    public String addTask() throws Exception {
        // 构建任务参数
        JobForm jobForm = new JobForm();
        jobForm.setJobClassName("com.neo.springboot.job.HelloJob")
                .setJobGroupName("testGroup")
                // 每五秒执行一次 https://cron.qqe2.com/
                .setCronExpression("0/5 * * * * ?");
        jobService.addJob(jobForm);
        return "ok";
    }

    @GetMapping("delete")
    public String deleteJob() throws Exception {
        // 构建任务参数
        JobForm jobForm = new JobForm();
        jobForm.setJobClassName("com.neo.springboot.job.HelloJob")
                .setJobGroupName("testGroup")
                // 每五秒执行一次 https://cron.qqe2.com/
                .setCronExpression("0/5 * * * * ?");
        jobService.deleteJob(jobForm);
        return "ok";
    }

    @GetMapping("pause")
    public String pauseJob() throws Exception {
        // 构建任务参数
        JobForm jobForm = new JobForm();
        jobForm.setJobClassName("com.neo.springboot.job.HelloJob")
                .setJobGroupName("testGroup")
                // 每五秒执行一次 https://cron.qqe2.com/
                .setCronExpression("0/5 * * * * ?");
        jobService.pauseJob(jobForm);
        return "ok";
    }

    @GetMapping("resume")
    public String resumeJob() throws Exception {
        // 构建任务参数
        JobForm jobForm = new JobForm();
        jobForm.setJobClassName("com.neo.springboot.job.HelloJob")
                .setJobGroupName("testGroup")
                // 每五秒执行一次 https://cron.qqe2.com/
                .setCronExpression("0/5 * * * * ?");
        jobService.resumeJob(jobForm);
        return "ok";
    }

    @GetMapping("cronJob")
    public String cronJob() throws Exception {
        // 构建任务参数
        JobForm jobForm = new JobForm();
        jobForm.setJobClassName("com.neo.springboot.job.HelloJob")
                .setJobGroupName("testGroup")
                // 每2秒执行一次 https://cron.qqe2.com/
                .setCronExpression("0/2 * * * * ?");
        jobService.cronJob(jobForm);
        return "ok";
    }


}
