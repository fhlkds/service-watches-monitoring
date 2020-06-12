package com.fhlkd.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by yanghaiyang on 2020/6/10 21:40
 */
@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "email")
public class EmailConfig {

    /**
     * 收件人邮箱地址
     */
    private List<String> noticeEmail;

    /**
     * 发件人邮箱地址
     */
    private String from;

    /**
     * 发件人用户
     */
    private String user;

    /**
     * 发件人授权码
     */
    private String authorizationCode;

    /**
     * 发件人的邮箱服务器
     */
    private String mailHost;


}
