# dubbo-spring-cloud示例

> 示例很简单，只提供了一个echo服务(请求什么就回应什么)，用最简单的模型展示使用方法。
>
> 鉴于dubbo官方发展规划是与spring cloud融合，以后使用建议采用spring cloud方式。

- [注解接口调用](dubbo-spring-cloud-anno)

  与原有dubbo使用方式类似，注入接口代理实现RPC调用，区别是不用配置麻烦的xml，加上注解使用包扫描。

- [spring cloud方式调用](dubbo-spring-cloud-web)

  采用spring cloud模型，每个接口都会对应一个uri。
  
- TODO spring cloud与dubbo互相调用模型
