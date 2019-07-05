package xuyuji.scaffold.springrabbit;

import java.io.IOException;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DefaultConsumer implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultConsumer.class);

    @Override
    public void onMessage(Message message) {
        LOG.info("收到消息:{}", new String(message.getBody(), Charset.forName("UTF-8")));
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer-default.xml");
        context.start();
    }
}
