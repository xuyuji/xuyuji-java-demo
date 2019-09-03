package xuyuji.scaffold.aop.expand.program;

import java.lang.annotation.Annotation;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.util.Assert;


public class LogPointcutAdvisor extends AbstractPointcutAdvisor {

    private static final long serialVersionUID = -3480389512517460077L;

    private Advice advice;
    private Pointcut pointcut;

    public LogPointcutAdvisor(Class<? extends Annotation> annotationType,
            Advice advice) {
        this.advice = advice;
        this.pointcut = buildPointcut(annotationType);
    }

    @Override
    public Pointcut getPointcut() {
        Assert.notNull(this.pointcut, "'pointcut' must not be null");
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        Assert.notNull(this.advice, "'advice' must not be null");
        return this.advice;
    }

    private Pointcut buildPointcut(Class<? extends Annotation> annotationType) {
        Assert.notNull(annotationType, "'annotationTypes' must not be null");
        ComposablePointcut pointcut = null;
        // 类级别
        Pointcut cpc = new AnnotationMatchingPointcut(annotationType, true);
        // 方法级别
        Pointcut mpc = AnnotationMatchingPointcut.forMethodAnnotation(annotationType);
        // 对于类和方法上都可以添加注解的情况
        // 类上的注解,最终会将注解绑定到每个方法上
        if (pointcut == null) {
            pointcut = new ComposablePointcut(cpc);
        }
        return pointcut.union(mpc);
    }

}
