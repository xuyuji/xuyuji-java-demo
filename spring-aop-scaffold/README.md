# spring-aop脚手架

整理一个简洁的spring-aop工程，供需要时直接拿来使用。

## 内容

当我们需要拓展aop功能时，有几种选择：

1. 传统的xml配置
2. [注解式aop配置](src/main/java/xuyuji/scaffold/aop/expand/aspect)
3. [自行实现PointcutAdvisor](src/main/java/xuyuji/scaffold/aop/expand/program)
4. 自行实现BeanPostProcessor，创建自定义代理

其中1、2、3都是spring aop功能的不同拓展方式，1是比较老套的模式了，在springboot大行其道的当下已经不适用了；2是一种比较简单易用的注解模式，日常使用推荐；3这种模式实际上是spring aop底层实现类，1、2最终是会被解析成这种模式，这种模式比较适用于spring拓展。

4是另起炉灶的做法，实际上spring aop也是基于此来实现的，这种方式自由度较高，没有2易用。

