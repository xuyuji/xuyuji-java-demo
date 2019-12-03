package org.lakala.samples.dubbo_spring_cloud_anno_provider.service;

import org.apache.dubbo.config.annotation.Service;
import org.lakala.samples.dubbo_spring_cloud_anno_api.service.EchoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EchoServiceImpl implements EchoService {

    @Override
    public String echo(String message) {
        log.info("echo服务接收到请求：{}", message);
        return "[echo]" + message;
    }

}
