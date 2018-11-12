package xuyuji.tools.MySpider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import xuyuji.tools.MySpider.spider.MebookSpider;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MebookSpiderTest {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MebookSpider mebookSpider;
    
    @Value("${spring.mail.username}")
    private String sender;
	
	@Value("${receiver}")
    private String receiver;

    @Test
    public void testSpider() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject("小书屋test");
        message.setText(mebookSpider.fetch());
        mailSender.send(message);
    }
    
    @Test
    public void testSendMail() throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(receiver);
        message.setSubject("小书屋test1");
        message.setText("测试邮件");
        mailSender.send(message);
    }

}
