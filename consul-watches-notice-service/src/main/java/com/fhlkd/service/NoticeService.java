package com.fhlkd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fhlkd.entity.ServiceInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yanghaiyang on 2020/6/11 9:41
 * 通知业务相关
 *
 */
@Slf4j
@Service
public class NoticeService {


    @Resource
    private EmailService emailService;
    /**
     * 还有可用实例的服务
     * key=serviceName_environmental
     * value=ServiceInfoEntity.class
     */
    public static Map<String, ServiceInfoEntity> offline = new ConcurrentHashMap<>();

    /**
     *无可用实例的服务
     * key=serviceName_environmental
     * value=ServiceInfoEntity.class
     */
    public static Map<String,ServiceInfoEntity> sos = new ConcurrentHashMap<>();

    public String geoWetch(HttpServletRequest request)throws IOException {
        //服务名字
        String serviceName = "";
        //环境
        String environmental = "";
        //当前服务配置的实例数量
        String member = "";
        Enumeration<String> enumeration = request.getHeaderNames();
        //取出所有header值
        while (enumeration.hasMoreElements()){
            String headerName = enumeration.nextElement();
            String headerValue = request.getHeader(headerName);
            log.info("headerName：{}-----headerValue：{}",headerName,headerValue);
            if(headerName.equals("name")){
                serviceName = headerValue;
            }
            if(headerName.equals("environmental")){
                environmental = headerValue != null && !headerValue.equals("") ?
                        new String(headerValue.getBytes("ISO-8859-1"),"UTF-8") : "";
            }
            if(headerName.equals("member")){
                member = headerValue;
            }
        }

        if(StringUtils.isEmpty(serviceName) || StringUtils.isEmpty(environmental) || StringUtils.isEmpty(member)){
            log.error("serviceName or environmental is not null");
            return "service error";
        }
        log.info("service name : {}，environmental：{}，member：{}",serviceName,environmental,member);
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String tmpStr = "";
        String param = "";
        while ((tmpStr = reader.readLine()) != null) {
            param += tmpStr;
        }
        log.info("service monitoring notice param :{}",param);
        //服务信息实体
        ServiceInfoEntity serviceInfoEntity = ServiceInfoEntity.builder()
                .belonger(environmental)
                .totalMember(Integer.valueOf(member))
                .name(serviceName)
                .build();
        String key = serviceName+"_"+environmental;
        if(param.length()>2){
            JSONArray services = JSON.parseArray(param);
            if(services.size() == Integer.parseInt(member)){
                log.info("service discovery status ：{}",services);
                sos.remove(key);
                offline.remove(key);
            }else{
                serviceInfoEntity.setAvailableMember(services.size());
                offline.put(key,serviceInfoEntity);
                sos.remove(key);
                //当前服务还有可用实例
                log.info("service offline ：{}，environmental：{}",serviceName,environmental);
                //发送邮件
                emailService.transationEmailExt(serviceInfoEntity);
            }
        }else{
            serviceInfoEntity.setAvailableMember(0);
            sos.put(key,serviceInfoEntity);
            //紧急---------当前服务没有可用实例
            log.error("service offline ：{}，environmental：{}",serviceName,environmental);
            //发送邮件
            emailService.transationEmailExt(serviceInfoEntity);
        }
        return "success";
    }

}
