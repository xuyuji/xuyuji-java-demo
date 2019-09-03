package xuyuji.scaffold.aop.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import xuyuji.scaffold.aop.expand.anno.AspectLog;
import xuyuji.scaffold.aop.expand.anno.ProgramLog;

@Slf4j
@Service
public class HelloService {

    @ProgramLog
    public void hello1() {
        log.info("hello1");
    }

    @AspectLog
    public void hello2() {
        log.info("hello2");
    }
}
