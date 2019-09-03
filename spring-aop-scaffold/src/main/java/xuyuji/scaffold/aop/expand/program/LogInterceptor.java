package xuyuji.scaffold.aop.expand.program;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        log.info("[LogInterceptor]{} begin", getSignature(invocation));
        Object result = invocation.proceed();
        log.info("[LogInterceptor]{} end", getSignature(invocation));
        return result;
    }

    private String getSignature(MethodInvocation invocation) {
        return String.format("%s.%s()", invocation.getMethod().getDeclaringClass().getSimpleName(),
                invocation.getMethod().getName());
    }

}
