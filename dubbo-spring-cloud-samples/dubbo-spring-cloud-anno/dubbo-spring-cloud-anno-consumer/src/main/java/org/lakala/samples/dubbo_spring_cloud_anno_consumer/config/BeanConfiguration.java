package org.lakala.samples.dubbo_spring_cloud_anno_consumer.config;

import org.apache.dubbo.config.annotation.Reference;
import org.lakala.samples.dubbo_spring_cloud_anno_api.service.EchoService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BeanConfiguration {

    @Reference
    private EchoService echoService;

    @Bean
    public ApplicationRunner echoRunner() {
        return args -> {
            log.info("■echo response:{}■", echoService.echo("hello"));
        };
    }
    
}
