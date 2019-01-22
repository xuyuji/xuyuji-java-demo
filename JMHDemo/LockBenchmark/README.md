# 加锁与CAS性能验证

## 描述

使用JMH测试java中加锁与CAS的性能差别

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
Benchmark                                            Mode  Cnt     Score      Error  Units
LockBenchmark.measureCAS                           sample    4  5020.582 ±  219.075  ms/op
LockBenchmark.measureCAS:measureCAS·p0.00          sample       4991.222             ms/op
LockBenchmark.measureCAS:measureCAS·p0.50          sample       5012.193             ms/op
LockBenchmark.measureCAS:measureCAS·p0.90          sample       5066.719             ms/op
LockBenchmark.measureCAS:measureCAS·p0.95          sample       5066.719             ms/op
LockBenchmark.measureCAS:measureCAS·p0.99          sample       5066.719             ms/op
LockBenchmark.measureCAS:measureCAS·p0.999         sample       5066.719             ms/op
LockBenchmark.measureCAS:measureCAS·p0.9999        sample       5066.719             ms/op
LockBenchmark.measureCAS:measureCAS·p1.00          sample       5066.719             ms/op
LockBenchmark.measureLock                          sample    5  5608.623 ± 7539.543  ms/op
LockBenchmark.measureLock:measureLock·p0.00        sample       2843.738             ms/op
LockBenchmark.measureLock:measureLock·p0.50        sample       5477.761             ms/op
LockBenchmark.measureLock:measureLock·p0.90        sample       8095.007             ms/op
LockBenchmark.measureLock:measureLock·p0.95        sample       8095.007             ms/op
LockBenchmark.measureLock:measureLock·p0.99        sample       8095.007             ms/op
LockBenchmark.measureLock:measureLock·p0.999       sample       8095.007             ms/op
LockBenchmark.measureLock:measureLock·p0.9999      sample       8095.007             ms/op
LockBenchmark.measureLock:measureLock·p1.00        sample       8095.007             ms/op
LockBenchmark.measureNoLock                        sample   62   327.029 ±    4.545  ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.00    sample        311.951             ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.50    sample        324.010             ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.90    sample        343.985             ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.95    sample        347.734             ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.99    sample        354.943             ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.999   sample        354.943             ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.9999  sample        354.943             ms/op
LockBenchmark.measureNoLock:measureNoLock·p1.00    sample        354.943             ms/op
```

## 结果分析

@Measurement中设置了batchSize=100000000，100000000次方法调用一起计时。

输出结果中p系列指取样结果分布，例如measureLock·p0.50指50%的加锁方法100000000次调用耗时在324.01ms内。



无锁自然是最快的，性能要高一个数量级。

Lock的性能确实比CAS差，耗时较长，稳定性也差，但是差距并不太大，没有传说中那么大的差距，这也印证了JDK后续优化了锁模型。

## 切换JDK1.7验证

jdk切换为jdk1.7.0_80测试

```
Benchmark                                            Mode  Cnt      Score       Error  Units
LockBenchmark.measureCAS                           sample    4   6004.670 ± 28196.178  ms/op
LockBenchmark.measureCAS:measureCAS·p0.00          sample        1801.454              ms/op
LockBenchmark.measureCAS:measureCAS·p0.50          sample        5605.687              ms/op
LockBenchmark.measureCAS:measureCAS·p0.90          sample       11005.854              ms/op
LockBenchmark.measureCAS:measureCAS·p0.95          sample       11005.854              ms/op
LockBenchmark.measureCAS:measureCAS·p0.99          sample       11005.854              ms/op
LockBenchmark.measureCAS:measureCAS·p0.999         sample       11005.854              ms/op
LockBenchmark.measureCAS:measureCAS·p0.9999        sample       11005.854              ms/op
LockBenchmark.measureCAS:measureCAS·p1.00          sample       11005.854              ms/op
LockBenchmark.measureLock                          sample    4   7960.789 ±  4384.412  ms/op
LockBenchmark.measureLock:measureLock·p0.00        sample        7189.037              ms/op
LockBenchmark.measureLock:measureLock·p0.50        sample        7939.817              ms/op
LockBenchmark.measureLock:measureLock·p0.90        sample        8774.484              ms/op
LockBenchmark.measureLock:measureLock·p0.95        sample        8774.484              ms/op
LockBenchmark.measureLock:measureLock·p0.99        sample        8774.484              ms/op
LockBenchmark.measureLock:measureLock·p0.999       sample        8774.484              ms/op
LockBenchmark.measureLock:measureLock·p0.9999      sample        8774.484              ms/op
LockBenchmark.measureLock:measureLock·p1.00        sample        8774.484              ms/op
LockBenchmark.measureNoLock                        sample   64    317.342 ±     2.358  ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.00    sample         305.660              ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.50    sample         317.194              ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.90    sample         323.486              ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.95    sample         327.942              ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.99    sample         333.971              ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.999   sample         333.971              ms/op
LockBenchmark.measureNoLock:measureNoLock·p0.9999  sample         333.971              ms/op
LockBenchmark.measureNoLock:measureNoLock·p1.00    sample         333.971              ms/op
```

可以看出JDK8并发性能提升了不少，并且Lock与CAS的性能差距减少了。