# 基于apollo的令牌桶

整理一个简洁的令牌桶工程，供需要时直接拿来使用。

## 内容

- 自己实现的一个简易版令牌桶，令牌通过调用时计算来处理，可配置：`令牌桶容量`、`令牌恢复速度`、`令牌使用间隔`。

- 基于apollo实现动态控制

  | 配置项                                        | 配置说明                                              | 默认值          |
  | --------------------------------------------- | ----------------------------------------------------- | --------------- |
  | ratelimit.switch                              | Y(开启)/N(关闭)                                       | Y               |
  | ratelimit.ns.default.bucket.capacity          | 默认namespace令牌桶容量，单位：个                     | 5               |
  | ratelimit.ns.default.bucket.recoveryspeed     | 默认namespace令牌桶令牌恢复速度，单位：毫秒           | 17280000        |
  | ratelimit.ns.default.bucket.interval          | 默认namespace令牌桶令牌使用最小时间间隔，单位：毫秒   | 3600000         |
  | ratelimit.ns.{namespace}.bucket.capacity      | 自定义namespace令牌桶容量，单位：个                   | 同default域配置 |
  | ratelimit.ns.{namespace}.bucket.recoveryspeed | 自定义namespace令牌桶令牌恢复速度，单位：毫秒         | 同default域配置 |
  | ratelimit.ns.{namespace}.bucket.interval      | 自定义namespace令牌桶令牌使用最小时间间隔，单位：毫秒 | 同default域配置 |

  默认一个default令牌桶，如果配置了自定义namespace令牌桶配置，则创建对应的令牌桶。

  ```java
  accessRateLimit.visit()	//使用default域
  
  accessRateLimit.visit(namespace)	//使用自定义域，如果该域令牌桶不存在则使用default令牌桶
  ```

  借助apollo的配置动态更新功能，支持令牌桶的动态更新。