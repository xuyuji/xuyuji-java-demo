package xuyuji.scaffold.aop.expand.program;

import org.aopalliance.aop.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xuyuji.scaffold.aop.expand.anno.ProgramLog;

@Configuration
public class LogConfiguration {

    @Bean
    @Autowired
    public LogPointcutAdvisor getLogPointcutAdvisor(Advice advice) {
        return new LogPointcutAdvisor(ProgramLog.class, advice);
    }

}
