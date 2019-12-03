package org.lakala.samples.dubbo_spring_cloud_web_provider.service;

import org.apache.dubbo.config.annotation.Service;
import org.lakala.samples.dubbo_spring_cloud_web_api.service.EchoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RestController
public class EchoServiceImpl implements EchoService {

    @Override
    @GetMapping(value = "/echo")
    public String echo(@RequestParam("msg") String message) {
        log.info("echo服务接收到请求：{}", message);
        return "[echo]" + message;
    }
}
