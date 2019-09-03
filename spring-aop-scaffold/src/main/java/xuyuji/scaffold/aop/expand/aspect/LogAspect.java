package xuyuji.scaffold.aop.expand.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("@within(xuyuji.scaffold.aop.expand.anno.AspectLog)")
    public void classLogPointCut() {}
    
    @Pointcut("@annotation(xuyuji.scaffold.aop.expand.anno.AspectLog)")
    public void methodLogPointCut() {}

    @Around("classLogPointCut() || methodLogPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("[LogAspect]{} begin", point.getSignature().toShortString());
        Object result = point.proceed();
        log.info("[LogAspect]{} end", point.getSignature().toShortString());
        return result;
    }

}
