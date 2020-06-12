package com.fhlkd.task;

import com.alibaba.fastjson.JSONArray;
import com.fhlkd.service.EmailService;
import com.fhlkd.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yanghaiyang on 2020/6/11 15:06
 * 任务调度
 */
@Component
@Slf4j
public class NoticeTask {

    @Resource
    private EmailService emailService;

    @Scheduled(cron = "1 * * * * ?")
    public void sosNotice(){
        List<String> key = NoticeService.sos.values().stream().map(
                notice -> {
                    emailService.transationEmailExt(notice);
                    return notice.getName()+notice.getBelonger();
                }
        ).collect(Collectors.toList());
        log.info("sos member ：{}，keys：{}",key.size(), JSONArray.toJSONString(key));
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void offlineNotice(){
        List<String> key = NoticeService.offline.values().stream().map(
                notice -> {
                    emailService.transationEmailExt(notice);
                    return notice.getName()+notice.getBelonger();
                }
        ).collect(Collectors.toList());
        log.info("offline member ：{}，keys：{}",key.size(), JSONArray.toJSONString(key));
    }



}
