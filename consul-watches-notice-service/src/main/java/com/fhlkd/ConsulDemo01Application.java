package com.fhlkd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
@EnableScheduling
public class ConsulDemo01Application {

    public static void main(String[] args) {
        SpringApplication.run(ConsulDemo01Application.class, args);
    }

}
