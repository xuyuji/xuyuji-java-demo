package xuyuji.scaffold.aop.service;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import xuyuji.scaffold.aop.expand.anno.AspectLog;
import xuyuji.scaffold.aop.expand.anno.ProgramLog;

@Slf4j
@AspectLog
@ProgramLog
@Service
public class OkService {

    public void ok1() {
        log.info("ok1");
    }
    
    public void ok2() {
        log.info("ok2");
    }
}
