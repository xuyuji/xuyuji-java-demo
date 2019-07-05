package xuyuji.scaffold.springrabbit;

import java.io.IOException;
import java.util.Calendar;
import javax.annotation.Resource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {

    @Resource(name = "amqpTemplate")
    private AmqpTemplate amqpTemplate;

    public void sendMsg() {
        amqpTemplate.convertAndSend(
                "现在时间:" + DateFormatUtils.format(Calendar.getInstance(), "yyyy-MM-dd hh:mm:ss"));
    }

    @SuppressWarnings("resource")
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("provider.xml");
        context.start();

        Provider provider = context.getBean(Provider.class);
        provider.sendMsg();
    }
}
