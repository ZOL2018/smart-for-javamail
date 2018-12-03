package com.example.javamail.controller;

import com.sun.mail.imap.IMAPMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.Properties;

@RestController
public class MailController {
    @Autowired
    private JavaMailSender mailSender;
    @RequestMapping(value = "/send")
    public String send(){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("binbin163mail@163.com");
        mailMessage.setTo("binteam2018@163.com");
        mailMessage.setSubject("测试主题");
        mailMessage.setText("测试");
        return "success";
    }
    @RequestMapping(value = "/sendMessage")
    public String sendMessage(){
        Properties prop = new Properties();
        prop.setProperty("mail.debug", "true");
        prop.setProperty("mail.store.protocol","pop3");
        prop.setProperty("mail.pop3.host","110");
        prop.setProperty("mail.pop3.starttls.enable","true");
        prop.setProperty("mail.smtp.auth","true");
        Session session = Session.getInstance(prop);
        try {
            Store store = session.getStore();
            store.connect("pop.163.com", "binbin163mail@163.com", "BINBIN163MAIL");
            Folder folder = store.getFolder("inbox");
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.getMessages();
            for(int i = 0; i < messages.length; i++){
                String sub = messages[i].getSubject();
                String from = (messages[i].getFrom()[0]).toString();
                String from0 = messages[i].getFrom().toString();
                String getFolder = messages[i].getFolder().toString();
                String getFlags = messages[i].getFlags().toString();
                String getContent = messages[i].getContent().toString();
                String getSentDate = messages[i].getSentDate().toString();
                String getSession = messages[i].getSession().toString();
                System.out.println( "第" + i + 1 + "封邮件，" +
                        "主题: " + sub +
                        ";from: " + from +
                        ";from0: " + from0 +
                        ";getFolder: " + getFolder +
                        ";getContent: " + getContent +
                        ";getSentDate: " + getSentDate +
                        ";getSession: " + getSession +
                        ";getFlags: " + getFlags);
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }
    @RequestMapping(value = "/sendEcloud")
    public String sendEcloud(){
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imap");
        props.setProperty("mail.imap.host", "imap.163.com");
        props.setProperty("mail.imap.port", "143");
        Session session = Session.getInstance(props,null);
        try {
            Store store = session.getStore("imap");
            store.connect("binbin163mail@163.com", "BINBIN163MAIL");
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();

            System.out.println("收件箱中共" + messages.length + "封邮件!");
            System.out.println("收件箱中共" + folder.getUnreadMessageCount() + "封未读邮件!");
            System.out.println("收件箱中共" + folder.getNewMessageCount() + "封新邮件!");
            System.out.println("收件箱中共" + folder.getDeletedMessageCount() + "封已删除邮件!");

            for (Message message : messages) {
                MimeMessage msg = (MimeMessage) message;
                System.out.println("------------------解析第" + msg.getMessageNumber() + "封邮件-------------------- ");
                System.out.println("主题: " + msg.getSubject());
                System.out.println("发件人: " + msg.getFrom());
                System.out.println("收件人：" + msg.getReplyTo());
                System.out.println("发送时间：" + msg.getSentDate());
                System.out.println("是否已读：" + msg.getFlags().contains(Flags.Flag.SEEN));
                System.out.println("邮件描述：" + msg.getDescription());
                System.out.println("邮件大小：" + msg.getSize() * 1024 + "kb");
                boolean isJPG = msg.isMimeType("jpg");
                boolean isPNG = msg.isMimeType("png");
                System.out.println("是否包含图片jpg：" + isJPG);
                System.out.println("是否包含图片png：" + isPNG);
                System.out.println(msg.getContent().toString());
                System.out.println("------------------第" + msg.getMessageNumber() + "封邮件解析结束-------------------- ");
                System.out.println();
            }
            folder.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }
}
