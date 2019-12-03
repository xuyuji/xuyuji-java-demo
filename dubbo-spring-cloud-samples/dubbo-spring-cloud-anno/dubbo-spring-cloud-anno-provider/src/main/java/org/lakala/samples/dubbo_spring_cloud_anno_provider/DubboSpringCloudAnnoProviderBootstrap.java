package org.lakala.samples.dubbo_spring_cloud_anno_provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DubboSpringCloudAnnoProviderBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(DubboSpringCloudAnnoProviderBootstrap.class, args);
    }
}