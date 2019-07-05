package xuyuji.scaffold.springrabbit;

import java.io.IOException;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.rabbitmq.client.Channel;

public class ManualConsumer implements ChannelAwareMessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultConsumer.class);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        LOG.info("收到消息:{}", new String(message.getBody(), Charset.forName("UTF-8")));

        // 手动确定，自行决定消息是否消费完成。
        // 如果不处理，该消息在rabbitmq中状态会变为Unacked，一定时间内没有响应的话会重发。
        // 参数：消息唯一标识, 批处理(当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息)
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        // 拒绝消息
        // 参数：消息唯一标识, 是否重新加入队列(当该参数为false时，这条消息即丢弃了)
        // channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("consumer-manual.xml");
        context.start();
    }

}
