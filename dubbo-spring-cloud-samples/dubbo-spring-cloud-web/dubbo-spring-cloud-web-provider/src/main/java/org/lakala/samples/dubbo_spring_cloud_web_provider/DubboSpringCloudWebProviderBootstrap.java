package org.lakala.samples.dubbo_spring_cloud_web_provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DubboSpringCloudWebProviderBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(DubboSpringCloudWebProviderBootstrap.class, args);
    }
}