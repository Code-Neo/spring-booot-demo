package com.neo.springboot.email.controller;

import cn.hutool.core.io.resource.ResourceUtil;
import com.neo.springboot.email.Utils.EmailUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.net.URL;

/**
 * @author: neo
 * @description 测试发送邮件
 * @date: 2023/6/25 16:50
 */

@RestController
public class TestController {
//    1131881548@qq.com

    @Resource
    private EmailUtils emailUtils;

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 发送文本内容
     * @param to
     * @param subject
     * @param content
     * @param cc
     * @return
     */
    @GetMapping("sendSimpleTextMail")
    public String sendSimpleTextMail(String to, String subject, String content, String... cc){
        emailUtils.sendSimpleMail(to,subject,content,cc);
        return "ok";
    }

    /**
     * 发送HTML邮件
     * @param to
     * @param subject
     * @param code
     * @param cc
     * @return
     * @throws MessagingException
     */
    @GetMapping("sendSimpleHtmlMail")
    public String sendSimpleHtmlMail(String to, String subject, String code, String... cc) throws MessagingException {

        Context context = new Context();
        context.setVariable("code",code);
        context.setVariable("subject",subject);


        String html = templateEngine.process("Mail", context);

        emailUtils.sendHtmlMail(to,subject,html,cc);
        return "ok";
    }


    /**
     * 发送自定义模板文件的邮件
     * @param to
     * @param subject
     * @param code
     * @param cc
     * @return
     * @throws MessagingException
     */
    @GetMapping("sendSimpleHtmlMailWithMyPath")
    public String sendSimpleHtmlMailWithMyPath(String to, String subject, String code, String... cc) throws MessagingException {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setCacheable(false);
        templateResolver.setPrefix("classpath:/myMailDir/");
        templateResolver.setSuffix(".html");

        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("code",code);
        context.setVariable("subject",subject);

        String html = templateEngine.process("Mail", context);

        emailUtils.sendHtmlMail(to,subject,html,cc);

        return "ok";
    }

    /**
     * 发送带附件的邮件
     * @param to
     * @param subject
     * @param content
     * @param cc
     * @throws MessagingException
     */
    @GetMapping("sendAttachmentsMail")
    public void sendAttachmentsMail(String to, String subject, String content, String... cc) throws MessagingException {
        URL url = ResourceUtil.getResource("static/icons.png");
        String filePath = url.getPath();
        emailUtils.sendAttachmentsMail(to,subject,content,filePath,cc);
    }


    /**
     * 发送静态资源在内容中的邮件
     * @param to
     * @param subject
     * @param cc
     * @throws MessagingException
     */
    @GetMapping("sendResourceMail")
    public void sendResourceMail(String to, String subject, String... cc) throws MessagingException {
        URL url = ResourceUtil.getResource("static/icons.png");
        String filePath = url.getPath();

        String rscId = "icons";
        String content = "<html><body>这是带静态资源的邮件<br/><img src=\'cid:" + rscId + "\' ></body></html>";

        emailUtils.sendResourceMail(to,subject,content,filePath,rscId,cc);
    }
}
