package com.fhlkd.service;


import com.fhlkd.entity.EmailConfig;
import com.fhlkd.entity.ServiceInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by yanghaiyang on 2020/6/11 10:26
 * 邮件发送相关
 */
@Slf4j
@Service
public class EmailService {

    @Resource
    private EmailConfig emailConfig;

    /**
     * @param text
     * @param title
     */
    /* 发送验证信息的邮件 */
    public boolean sendMail(String text, String title) {
        Properties props = new Properties();
        // 设置发送邮件的邮件服务器的属性
        props.put("mail.smtp.host", emailConfig.getMailHost());
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");
            // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);
        //调试
        //session.setDebug(true);
        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            message.setFrom(new InternetAddress(emailConfig.getFrom()));
            // 加载收件人地址
            Address[] tos = new InternetAddress[emailConfig.getNoticeEmail().size()];
            for (int i = 0; i < emailConfig.getNoticeEmail().size() ;i++){
                tos[i] = new InternetAddress(emailConfig.getNoticeEmail().get(i));
            }
            message.addRecipients(Message.RecipientType.TO, tos);
            // 标题
            message.setSubject(title);
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(text, "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            message.saveChanges(); // 保存变化
            // 连接服务器的邮箱
            Transport transport = session.getTransport("smtp");
            // 把邮件发送出去
            transport.connect(emailConfig.getMailHost(), emailConfig.getUser(), emailConfig.getAuthorizationCode());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            log.info("send email success text info :{} tos :{}",text,tos);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 封装发送邮件
     * @param serviceInfoEntity
     */
    public void transationEmailExt(ServiceInfoEntity serviceInfoEntity) {
        StringBuffer text = new StringBuffer();
        String title = "作业报警！";
        if(serviceInfoEntity == null)
            return;
        if(serviceInfoEntity.getAvailableMember() > 0){
            //有可用实例
            text.append("<p>current service info：</p>");
            text.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;current environmental ：");
            text.append(serviceInfoEntity.getBelonger()).append("</p>");
            text.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;service name：");
            text.append(serviceInfoEntity.getName()).append("</p>");
            text.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            text.append("<span style=\"color: rgb(255, 0, 0);\">&nbsp;&nbsp; <strong>available member：</strong>");
            text.append(serviceInfoEntity.getAvailableMember()).append("</span></p>");
            text.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;total member：");
            text.append(serviceInfoEntity.getTotalMember()).append("</p>");
            text.append("<p>").append(serviceInfoEntity.getName()).append("   服务已经宕机了").append("<span style=\"color: rgb(255, 0, 0);\">").append(serviceInfoEntity.getTotalMember()-serviceInfoEntity.getAvailableMember()).append("</span>").append("台实例，请紧急处理！！！<br/></p>");
        }else{
            //sos  无可用实例
            title = "SOS作业报警！";
            text.append("<p style=\"color: rgb(255, 0, 0);\">current service info ：</p>");
            text.append("<p style=\"color: rgb(255, 0, 0);\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;current environmental：");
            text.append(serviceInfoEntity.getBelonger()).append("</p>");
            text.append("<p style=\"color: rgb(255, 0, 0);\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;service name：");
            text.append(serviceInfoEntity.getName()).append("</p>");
            text.append("<p style=\"color: rgb(255, 0, 0);font-size: 26px;\">").
                    append(serviceInfoEntity.getName()).append("   服务实例全部宕机！！！！！</p>");
        }
        sendMail(text.toString(),title);
    }

}
