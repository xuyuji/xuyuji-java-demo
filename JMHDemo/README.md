# JMH示例库

## 起因

在看[咖啡拿铁的掘金专栏](https://juejin.im/user/57e4a4e80e3dd9005809b6fb)时，了解到JMH，于是学习了下，整理些demo作为笔记。



## JMH工程创建

1. 使用maven archetype创建工程

   mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jmh -DarchetypeArtifactId=jmh-java-benchmark-archetype -DgroupId=org.xuyuji -DartifactId=LockBenchmark -Dversion=1.0

2. 添加要测试的代码(出自咖啡拿铁的文章)

   ```
   @BenchmarkMode({ Mode.SampleTime })
   @OutputTimeUnit(TimeUnit.MILLISECONDS)
   @Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.MILLISECONDS)
   @Measurement(iterations = 1, batchSize = 100000000)
   @Threads(2)
   @Fork(1)
   @State(Scope.Benchmark)
   public class LockBenchmark {
   	Lock lock = new ReentrantLock();
   	long i = 0;
   	AtomicLong atomicLong = new AtomicLong(0);
   
   	@Benchmark
   	public void measureLock() {
   		lock.lock();
   		i++;
   		lock.unlock();
   	}
   
   	@Benchmark
   	public void measureCAS() {
   		atomicLong.incrementAndGet();
   	}
   
   	@Benchmark
   	public void measureNoLock() {
   		i++;
   	}
   }
   ```

3. 打包测试

   ```
   mvn clean package
   java -jar target/benchmarks.jar
   ```



## 目录

logback打印日志性能验证

加锁与CAS性能验证

随机函数性能验证



## 参考资料

[你应该知道的高性能无锁队列Disruptor](https://juejin.im/post/5b5f10d65188251ad06b78e3)

[揭秘Java高效随机数生成器](https://juejin.im/post/5b8742eb6fb9a019ba68480f)

[使用JMH进行微基准测试：不要猜，要测试！](http://www.importnew.com/18084.html)

[JMH简介](http://www.importnew.com/12548.html)