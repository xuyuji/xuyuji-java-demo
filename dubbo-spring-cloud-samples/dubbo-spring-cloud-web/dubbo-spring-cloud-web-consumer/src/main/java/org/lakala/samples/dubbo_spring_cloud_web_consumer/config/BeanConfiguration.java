package org.lakala.samples.dubbo_spring_cloud_web_consumer.config;

import java.util.Random;
import org.lakala.samples.dubbo_spring_cloud_web_consumer.service.DubboFeignRestEchoService;
import org.lakala.samples.dubbo_spring_cloud_web_consumer.service.FeignRestEchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;
import com.alibaba.cloud.dubbo.annotation.DubboTransported;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BeanConfiguration {

    @Autowired
    @Lazy
    private FeignRestEchoService feignRestEchoService;

    @Autowired
    @Lazy
    private DubboFeignRestEchoService dubboFeignRestEchoService;

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    @Value("${provider.application.name}")
    private String providerApplicationName;

    @Bean
    public ApplicationRunner echoRunner() {
        return args -> {
            new Thread(() -> {
                while (true) {
                    try {
                        log.info("■feign rest response:{}■", feignRestEchoService.echo("hello"));
                        log.info("■dubbo feign rest response:{}■",
                                dubboFeignRestEchoService.echo("are you ok?"));
                        log.info("■restTemplate.getForEntity response:{}■",
                                restTemplate.getForEntity("http://{p1}//echo?msg=666666", String.class,
                                        providerApplicationName).getBody());
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();
            log.info("■feign rest response:{}■", feignRestEchoService.echo("hello"));
            log.info("■dubbo feign rest response:{}■",
                    dubboFeignRestEchoService.echo("are you ok?"));
            log.info("■restTemplate.getForEntity response:{}■",
                    restTemplate.getForEntity("http://{p1}//echo?msg=666666", String.class,
                            providerApplicationName).getBody());
        };
    }

    @Bean
    @LoadBalanced
    @DubboTransported
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
