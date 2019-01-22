# logback打印日志性能验证

## 描述

使用JMH测试Logback几种日志打印方法的性能

- ```java
  logger.debug("Concatenating strings " + x + y + z);
  ```

- ```java
  logger.debug("Variable arguments {} {} {}", x, y, z);
  ```

- ```java
  if (logger.isDebugEnabled()) {
  	logger.debug("If debug enabled {} {} {}", x, y, z);
  }
  ```




## 本地验证

### 本地环境

| 类目            | 配置                                |
| --------------- | :---------------------------------- |
| 操作系统        | win10 x64                           |
| CPU             | Intel(R) Core(TM) i5-6300HQ 2.30GHz |
| RAM             | 8G                                  |
| JDK             | 1.8.0_171                           |
| logback日志级别 | INFO                                |



### 本地验证结果

```
Benchmark                               Mode  Cnt   Score   Error   Units
LogBenchmark.testConcatenatingStrings  thrpt   10  45.011 ± 1.862  ops/ms
LogBenchmark.testIfDebugEnabled        thrpt   10  86.600 ± 1.139  ops/ms
LogBenchmark.testVariableArguments     thrpt   10  84.781 ± 2.683  ops/ms
```



## 结果分析

从数据上可以看出来，通过String拼接参数的方式性能最差，几乎差了一倍，使用IfDebugEnabled模式性能最好，但是仅比VariableArguments模式性能好一点点，代价是代码复杂些。