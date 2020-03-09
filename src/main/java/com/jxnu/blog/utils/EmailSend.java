package com.jxnu.blog.utils;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class  EmailSend {
    public void emailSend(JavaMailSenderImpl javaMailSender,String email,String code) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("2602112893@qq.com");
        helper.setSubject("【blog】绑定邮箱验证");
        helper.setText("尊敬的用户,您好:\n" +
                "    您正在blog进行邮箱绑定的操作，本次请求的邮件验证码是："+code+"(为了保证您账号的安全性，请您在10分钟内完成验证).\n" +
                "本验证码10分钟内有效，请及时输入。\n" +
                "\n" +
                "    为保证账号安全，请勿泄漏此验证码。\n" +
                "    祝在【blog】收获愉快！");
        helper.setTo(email);
        javaMailSender.send(mimeMessage);
    }
}
