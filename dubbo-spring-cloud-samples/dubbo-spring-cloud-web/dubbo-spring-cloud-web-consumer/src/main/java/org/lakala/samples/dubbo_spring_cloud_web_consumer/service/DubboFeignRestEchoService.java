package org.lakala.samples.dubbo_spring_cloud_web_consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("${provider.application.name}")
public interface DubboFeignRestEchoService {

    @GetMapping(value = "/echo")
    public String echo(@RequestParam("msg") String msg);

}
