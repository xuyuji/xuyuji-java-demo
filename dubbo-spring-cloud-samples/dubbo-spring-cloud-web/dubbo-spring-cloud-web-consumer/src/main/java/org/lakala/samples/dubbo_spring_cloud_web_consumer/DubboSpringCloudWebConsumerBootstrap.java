package org.lakala.samples.dubbo_spring_cloud_web_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class DubboSpringCloudWebConsumerBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(DubboSpringCloudWebConsumerBootstrap.class, args);
    }
    
}
