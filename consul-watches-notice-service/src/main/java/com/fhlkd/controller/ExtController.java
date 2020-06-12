package com.fhlkd.controller;

import com.fhlkd.entity.ServiceInfoEntity;
import com.fhlkd.service.NoticeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yanghaiyang on 2020/6/11 16:31
 */
@RestController
@RequestMapping("/ext/cancellation")
public class ExtController {

    @GetMapping("/offline")
    public String offline(String key){
        if(NoticeService.offline.containsKey(key)){
            ServiceInfoEntity res = NoticeService.offline.remove(key);
            if(res != null){
                return "del success";
            }else{
                return "del fail!";
            }
        }else{
            return "key does not exist!";
        }
    }
    @GetMapping("/sos")
    public String sos(String key){
        if(NoticeService.sos.containsKey(key)){
            ServiceInfoEntity res = NoticeService.sos.remove(key);
            if(res != null){
                return "del success";
            }else{
                return "del fail!";
            }
        }else{
            return "key does not exist!";
        }
    }
}
