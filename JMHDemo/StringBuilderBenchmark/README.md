# String与StringBuilder字符拼接性能验证

## 描述

使用JMH测试String与StringBuilder字符串拼接性能



## 本地验证

### 本地环境

| 类目     | 配置                                |
| -------- | :---------------------------------- |
| 操作系统 | win10 x64                           |
| CPU      | Intel(R) Core(TM) i5-6300HQ 2.30GHz |
| RAM      | 8G                                  |
| JDK      | 1.8.0_171                           |



### 本地验证结果

```
Benchmark                                     Mode  Cnt      Score     Error   Units
StringBuilderBenchmark.testStringAdd         thrpt   20  10474.871 ± 506.806  ops/ms
StringBuilderBenchmark.testStringBuilderAdd  thrpt   20  40960.964 ± 921.859  ops/ms
```



## 结果分析

StringBuilder字符拼接性能几乎是String的4倍。