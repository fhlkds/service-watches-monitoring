package com.fhlkd;

import com.fhlkd.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
class ConsulDemo01ApplicationTests {

    @Resource
    private EmailService emailService;

    @Test
    void contextLoads() {
        String text = "你好,<a href='http://www.baidu.com'>激活</a>有惊喜噢";
        String title = "测试小通知";
        emailService.sendMail( text, title);

    }

}
