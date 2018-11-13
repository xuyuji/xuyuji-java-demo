package xuyuji.tools.MySpider.task;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import xuyuji.tools.MySpider.spider.MebookSpider;

@Service
public class MebookEmailTask {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MebookSpider mebookSpider;

    @Value("${spring.mail.username}")
    private String username;
    
    @Value("${receiver}")
    private String receiver;

    @Scheduled(cron = "${task.spider.mebook.corn}")
    public void sendMail() throws Exception {
        String todayStr = DateFormatUtils.format(new Date(), "yyyy/MM/dd");
        LOG.info("开始抓取小书屋[{}]更新", todayStr);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username);
        message.setTo("xuyuji1987@163.com");
        message.setSubject("小书屋[" + todayStr + "]更新");
        message.setText(mebookSpider.fetch());
        mailSender.send(message);
        LOG.info("小书屋更新抓取完成");
    }
}
