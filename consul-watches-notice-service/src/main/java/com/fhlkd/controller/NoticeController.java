package com.fhlkd.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fhlkd.entity.EmailConfig;
import com.fhlkd.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

/**
 * Created by yanghaiyang on 2020/6/10 15:04
 */
@RestController
@RequestMapping("/notice/wechat")
@Slf4j
public class NoticeController {


    @Resource
    private NoticeService noticeService;

    @Resource
    private EmailConfig serviceEntity;

    @PostMapping("/services")
    public String geoWechat(HttpServletRequest request)throws IOException {
        return noticeService.geoWetch(request);

    }
    //@PostMapping("/checks")
    public String checks(HttpServletRequest request)throws IOException {
        log.info("\n\n"+"checks------------------------\n");
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()){
            String headerName = enumeration.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println("headerName："+headerName+"-----headerValue："+headerValue);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String tmpStr = "";
        String param = "";
        while ((tmpStr = reader.readLine()) != null) {
            if(tmpStr.equals("\n"))
                break;
            param += tmpStr;
        }
        log.info("nodes param   :{}",param);
        JSONArray jsonObject = new JSONArray();
        if(param.length() > 5)
            jsonObject = JSON.parseArray(param);
        System.out.println("checks:     "+jsonObject+"\n\n\n\n\n"+"checks--------");
        return "success";
    }

    //@PostMapping("/nodes")
    public String nodes(HttpServletRequest request)throws IOException {
        log.info("\n\n"+"nodes------------------------\n");

        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()){
            String headerName = enumeration.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println("headerName："+headerName+"-----headerValue："+headerValue);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String tmpStr = "";
        String param = "";
        while ((tmpStr = reader.readLine()) != null) {
            param += tmpStr;
        }
        JSONObject jsonObject = JSON.parseObject(param);
        System.out.println("nodes     "+jsonObject+"\n\n\n\n\n"+"nodes-------");
        return "success";
    }

}
