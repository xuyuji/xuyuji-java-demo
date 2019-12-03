package org.lakala.samples.dubbo_spring_cloud_anno_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DubboSpringCloudAnnoConsumerBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(DubboSpringCloudAnnoConsumerBootstrap.class, args);
    }
}