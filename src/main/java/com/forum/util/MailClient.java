package com.forum.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailClient {

    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     *
     * 获取发件人
     *
     */
    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 发送邮件方法
     *
     * @param receiver : 接收者
     *
     * @param subject  : 标题
     *
     * @param content  : 内容
     *
     */
    public void sendMail(String receiver, String subject, String content){

        /**
         *
         * 创建要发送的邮件对象 MimeMessage
         *
         */
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        /**
         *
         * 使用 MimeMessageHelper 构建 MimeMessage
         *
         */
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try {

            mimeMessageHelper.setFrom(sender);

            mimeMessageHelper.setTo(receiver);

            mimeMessageHelper.setSubject(subject);

            mimeMessageHelper.setText(content, true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {

            logger.error("因为" + e + "异常 , " + "邮件发送失败");

        }

    }

}
