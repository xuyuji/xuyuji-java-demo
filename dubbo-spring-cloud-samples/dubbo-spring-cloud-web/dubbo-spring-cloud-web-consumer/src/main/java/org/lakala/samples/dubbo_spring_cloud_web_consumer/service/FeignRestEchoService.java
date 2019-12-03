package org.lakala.samples.dubbo_spring_cloud_web_consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.cloud.dubbo.annotation.DubboTransported;

@FeignClient("${provider.application.name}")
@DubboTransported(protocol = "dubbo")
public interface FeignRestEchoService {

    @GetMapping(value = "/echo")
    public String echo(@RequestParam("msg") String msg);

}
