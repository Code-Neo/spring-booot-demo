package com.neo.springboot.email.Utils;


import cn.hutool.core.util.ArrayUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * @author: neo
 * @description springboot 发送邮件工具类
 * @date: 2023/6/25 16:16
 */
@Component
public class EmailUtils {

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送文本邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param cc      抄送地址
     */
    public void sendSimpleMail(String to, String subject, String content, String... cc) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        if (cc!=null && cc.length != 0){
            message.setCc(cc);
        }

        javaMailSender.send(message);
    }


    /**
     * 发送HTML邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param cc      抄送地址
     * @throws MessagingException 邮件发送异常
     */
    public void sendHtmlMail(String to, String subject, String content, String... cc) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true);
        mimeMessageHelper.setFrom(from);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content,true);

        if (cc!=null && cc.length != 0){
            mimeMessageHelper.setCc(cc);
        }

        javaMailSender.send(message);
    }

    /**
     * 发送带附件的邮件
     * @param to 接收方
     * @param subject 主题
     * @param content 内容
     * @param filePath 文件绝对路径
     * @param cc 抄送
     * @throws MessagingException
     */
    public void sendAttachmentsMail(String to, String subject, String content, String filePath, String... cc) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        if (ArrayUtil.isNotEmpty(cc)) {
            helper.setCc(cc);
        }

        FileSystemResource resource = new FileSystemResource(new File(filePath));
        String fileName = filePath.substring(filePath.lastIndexOf('/'));

        // 添加附件
        helper.addAttachment(fileName,resource);
        javaMailSender.send(message);
    }

    /**
     * 发送内容中包含静态文件的邮件
     * @param to 接收方
     * @param subject 主题
     * @param content 内容
     * @param filePath 文件绝对路径
     * @param rscId 插入内容的位置
     * @param cc 抄送
     * @throws MessagingException
     */
    public void sendResourceMail(String to, String subject, String content, String filePath,String rscId, String... cc) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);
        if (ArrayUtil.isNotEmpty(cc)) {
            helper.setCc(cc);
        }

        FileSystemResource resource = new FileSystemResource(new File(filePath));

        // 添加附件
        helper.addInline(rscId, resource);
        javaMailSender.send(message);
    }


}
