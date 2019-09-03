package xuyuji.scaffold.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import xuyuji.scaffold.aop.service.HelloService;
import xuyuji.scaffold.aop.service.OkService;

@SpringBootApplication
public class BootstrapApplication {

    @Autowired
    @Lazy
    private HelloService helloService;

    @Autowired
    @Lazy
    private OkService okService;

    public static void main(String[] args) {
        SpringApplication.run(BootstrapApplication.class, args);
    }

    @Bean
    public ApplicationRunner initRunner() {
        return args -> {
            helloService.hello1();
            helloService.hello2();
            okService.ok1();
            okService.ok2();
        };
    }
}
