# spring-rabbit脚手架

整理一个简洁的spring-rabbit工程，供需要时直接拿来使用。

## 列表

- Provider

  消息生产者

- DefaultConsumer

  消息消费者。采用默认配置，消息确认自动处理。

- ManualConsumer

  消息消费者。acknowledge设置为manual，消息确认手动处理。

## ACK

rabbit:listener-container标签的acknowledge属性有三个选项：

- NONE

  接收到消息后立马确认。当后续的消息处理有异常时消息就丢失了。

- AUTO(default)

  自动确认，有一定的处理逻辑。

  - 当消息处理正常时自动确认

  - 当消息处理抛出异常ImmediateAcknowledgeAmqpException时，消息被确认

  - 当消息处理抛出异常AmqpRejectAndDontRequeueException时，消息被拒绝并且不会重新加入队列，也即是被抛弃掉了

  - 当消息处理抛出异常MessageRejectedWhileStoppingException时，消息被拒绝并且重新加入队列

  - 当消息处理抛出其他异常时，消息被拒绝并根据requeue-rejected配置来决定消息是否重新加入队列，requeue-rejected默认为true，此时消息可以被重新消费。

    需要说明的是一些系统级异常会关闭消费者功能，当前消费者进程会处于假死状态，消息只能由其他消费者消费。这类异常有InterruptedException、FatalListenerStartupException、FatalListenerExecutionException、Error等

- MANUAL

  手动确认

### 源码为证

rabbit:listener-container对应的类是SimpleMessageListenerContainer，其有个内部类AsyncMessageProcessingConsumer，AsyncMessageProcessingConsumer又封装了BlockingQueueConsumer，BlockingQueueConsumer是实际通讯的类。AsyncMessageProcessingConsumer实现了接口Runnable，在SimpleMessageListenerContainer初始化时会将AsyncMessageProcessingConsumer实例装入线程池中运行。

如此可知消息消费入口在AsyncMessageProcessingConsumer的run方法中

```java
private final class AsyncMessageProcessingConsumer implements Runnable {
    private final BlockingQueueConsumer consumer;

    private final CountDownLatch start;

    private volatile FatalListenerStartupException startupException;

    private AsyncMessageProcessingConsumer(BlockingQueueConsumer consumer) {
        this.consumer = consumer;
        this.start = new CountDownLatch(1);
    }

    @Override
    public void run() {
        ......
        this.consumer.start();
        ......
        try{
            while (isActive(this.consumer) || this.consumer.hasDelivery() || !this.consumer.cancelled()) {
        		try {
                	boolean receivedOk = receiveAndExecute(this.consumer); // At least one message received
                    ......
                }
                catch (ListenerExecutionFailedException ex) {
                    // Continue to process, otherwise re-throw
                    if (ex.getCause() instanceof NoSuchMethodException) {
                        throw new FatalListenerExecutionException("Invalid listener", ex);
                    }
                }
                catch (AmqpRejectAndDontRequeueException rejectEx) {
                    /*
                     *  These will normally be wrapped by an LEFE if thrown by the
                     *  listener, but we will also honor it if thrown by an
                     *  error handler.
                     */
                } 
            }
        }
        catch (InterruptedException e) {
            logger.debug("Consumer thread interrupted, processing stopped.");
            Thread.currentThread().interrupt();
            aborted = true;
            publishConsumerFailedEvent("Consumer thread interrupted, processing stopped", true, e);
        }
        ......
        catch (Error e) { //NOSONAR
            // ok to catch Error - we're aborting so will stop
            logger.error("Consumer thread error, thread abort.", e);
            publishConsumerFailedEvent("Consumer threw an Error", true, e);
            aborted = true;
        }
        ......
        if (!isActive(this.consumer) || aborted) {
            ......
            this.consumer.stop();
            ......
        }
    }
}
```

摘取了相关代码，this.consumer.start()是获取消息，这其中会处理acknowledge==NONE的自动ACK。

receiveAndExecute方法是获取消息后的处理部分，从其所处范围的try-catch可以证实系统异常会关闭消费者服务，同时ListenerExecutionFailedException异常的捕获还能证实其余异常能重新消费，spring-rabbit会将MessageListener抛出的Exception异常封装进ListenerExecutionFailedException，后续我们会寻找出处。

接着进入receiveAndExecute方法

```java
public class SimpleMessageListenerContainer extends AbstractMessageListenerContainer
		implements ApplicationEventPublisherAware {
	private boolean receiveAndExecute(final BlockingQueueConsumer consumer) throws Throwable {
		......
		return doReceiveAndExecute(consumer);
		......
	}
	
	private boolean doReceiveAndExecute(BlockingQueueConsumer consumer) throws Throwable { //NOSONAR
		Channel channel = consumer.getChannel();
		for (int i = 0; i < this.txSize; i++) {
            try {
                executeListener(channel, message);
            }
            catch (ImmediateAcknowledgeAmqpException e) {
                if (this.logger.isDebugEnabled()) {
                    ......
                    break;
                }
                catch (Throwable ex) { //NOSONAR
                    if (causeChainHasImmediateAcknowledgeAmqpException(ex)) {
                        if (this.logger.isDebugEnabled()) {
                            this.logger.debug("User requested ack for failed delivery: "
                                    + message.getMessageProperties().getDeliveryTag());
                        }
                        break;
                    }
                    ......
                    consumer.rollbackOnExceptionIfNecessary(ex);
                    throw ex;
                }
            }
        }
        return consumer.commitIfNecessary(isChannelLocallyTransacted(channel));
	}
	
	protected boolean causeChainHasImmediateAcknowledgeAmqpException(Throwable ex) {
		if (ex instanceof Error) {
			return false;
		}
		Throwable cause = ex.getCause();
		while (cause != null) {
			if (cause instanceof ImmediateAcknowledgeAmqpException) {
				return true;
			}
			else if (cause instanceof AmqpRejectAndDontRequeueException || cause instanceof Error) {
				return false;
			}
			cause = cause.getCause();
		}
		return false;
	}
}

public class BlockingQueueConsumer implements RecoveryListener {
	......
	public boolean commitIfNecessary(boolean locallyTransacted) throws IOException {
		......
		boolean ackRequired = !this.acknowledgeMode.isAutoAck() && !this.acknowledgeMode.isManual();

        if (ackRequired) {
            if (!this.transactional || isLocallyTransacted) {
                long deliveryTag = new ArrayList<Long>(this.deliveryTags).get(this.deliveryTags.size() - 1);
                    this.channel.basicAck(deliveryTag, true);
            }
        }
        ......
	}
	......
}

public enum AcknowledgeMode {
	NONE,
	MANUAL,
	AUTO;
	
	public boolean isTransactionAllowed() {
		return this == AUTO || this == MANUAL;
	}

	public boolean isAutoAck() {
		return this == NONE;
	}

	public boolean isManual() {
		return this == MANUAL;
	}
}
```

这部分正常逻辑是将消息传入Listener执行，自行完成后确认消息，注意commitIfNecessary方法中的basicAck。

这里证实了AUTO模式会自动确认这一点。

再看下关于ImmediateAcknowledgeAmqpException的处理，直接跳出循环执行commitIfNecessary。

这里证实了抛出ImmediateAcknowledgeAmqpException异常时消息会被确认。

接着我们看下其他异常处理部分的rollbackOnExceptionIfNecessary方法

```java
public class BlockingQueueConsumer implements RecoveryListener {
    public void rollbackOnExceptionIfNecessary(Throwable ex) throws Exception {
        boolean ackRequired = !this.acknowledgeMode.isAutoAck() && !this.acknowledgeMode.isManual();
        ......
        if (ackRequired) {
            boolean shouldRequeue = RabbitUtils.shouldRequeue(this.defaultRequeuRejected, ex, logger);
            for (Long deliveryTag : this.deliveryTags) {
                // With newer RabbitMQ brokers could use basicNack here...
                this.channel.basicReject(deliveryTag, shouldRequeue);
            }
            ......
        }
        ......
    }
}

public abstract class RabbitUtils {
    public static boolean shouldRequeue(boolean defaultRequeueRejected, Throwable throwable, Log logger) {
		boolean shouldRequeue = defaultRequeueRejected ||
				throwable instanceof MessageRejectedWhileStoppingException;
		Throwable t = throwable;
		while (shouldRequeue && t != null) {
			if (t instanceof AmqpRejectAndDontRequeueException) {
				shouldRequeue = false;
			}
			t = t.getCause();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Rejecting messages (requeue=" + shouldRequeue + ")");
		}
		return shouldRequeue;
	}
}
```

从rollbackOnExceptionIfNecessary方法中的逻辑可以看出非ImmediateAcknowledgeAmqpException异常都会执行basicReject方法，也即是拒绝消息。这证实了非ImmediateAcknowledgeAmqpException异常都会拒绝消息。

接着看下shouldRequeue，只有AmqpRejectAndDontRequeueException异常会返回false，其他异常都返回true，这个值是传给basicReject方法的第二个参数requeue的，控制是否重新加入队列。

这里证实了AmqpRejectAndDontRequeueException异常会直接抛弃消息，MessageRejectedWhileStoppingException异常会将拒绝的消息重新加入队列，其他异常拒绝消息根据requeue-rejected配置来决定消息是否重新加入队列。

至此所有AUTO模式的处理都已证实，只是还缺之前关于ListenerExecutionFailedException论证，接着看executeListener方法

```java
public abstract class AbstractMessageListenerContainer extends RabbitAccessor
		implements MessageListenerContainer, ApplicationContextAware, BeanNameAware, DisposableBean, SmartLifecycle {
	protected void executeListener(Channel channel, Message messageIn) throws Throwable {
		......
		invokeListener(channel, message);
		......
	}
	
	protected void invokeListener(Channel channel, Message message) throws Exception {
		Object listener = getMessageListener();
		if (listener instanceof ChannelAwareMessageListener) {
			doInvokeListener((ChannelAwareMessageListener) listener, channel, message);
		}
		else if (listener instanceof MessageListener) {
			......
			doInvokeListener((MessageListener) listener, message);
			......
		}
		......
	}
	
	protected void doInvokeListener(ChannelAwareMessageListener listener, Channel channel, Message message)
			throws Exception {
		......
		try {
			listener.onMessage(message, channelToUse);
		}
		catch (Exception e) {
			throw wrapToListenerExecutionFailedExceptionIfNeeded(e, message);
		}
		......
	}
	
	protected void doInvokeListener(MessageListener listener, Message message) throws Exception {
		try {
			listener.onMessage(message);
		}
		catch (Exception e) {
			throw wrapToListenerExecutionFailedExceptionIfNeeded(e, message);
		}
	}
	
	protected Exception wrapToListenerExecutionFailedExceptionIfNeeded(Exception e, Message message) {
		if (!(e instanceof ListenerExecutionFailedException)) {
			// Wrap exception to ListenerExecutionFailedException.
			return new ListenerExecutionFailedException("Listener threw exception", e, message);
		}
		return e;
	}
}
```

在doInvokeListener方法中针对listener.onMessage捕获了所有的Exception，并将这些Exception传入wrapToListenerExecutionFailedExceptionIfNeeded方法中，封装进ListenerExecutionFailedException。之前的说法也得到了证实。

顺便这里还可以看到spring-rabbit支持两种listener，MessageListener和ChannelAwareMessageListener，然后找到了调用我们自己实现的listener的地方------listener.onMessage。