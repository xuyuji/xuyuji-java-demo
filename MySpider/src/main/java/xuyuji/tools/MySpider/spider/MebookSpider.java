package xuyuji.tools.MySpider.spider;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MebookSpider {

    private final Logger LOG = LoggerFactory.getLogger(getClass());
    
    @Value("${mebook.url}")
    private String mebookUrl;
    
    public String fetch() {
        LOG.info("开始抓取Mebook");
        
        Date date = new Date();
        int retryTimes = 24;
        String content = fetch(date);
        while(retryTimes > 0 && StringUtils.isBlank(content)) {
            try {
                Thread.sleep(30 * 60 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOG.info("抓取Mebook重试");
            content = fetch(date);
            retryTimes--;
        }
        
        return content;
    }
    
    private String fetch(Date date) {
        StringBuilder sb = new StringBuilder();

        try {
            Document indexDoc = getDoc(mebookUrl);
            Pattern p = Pattern.compile("\\d{4}.\\d{2}.\\d{2}");
            for (Element e : indexDoc.select("#primary .list li")) {
                Matcher m = p.matcher(e.select(".info").text());
                if (m.find() && DateFormatUtils.format(date, "yyyy.MM.dd").equals(m.group())) {
                    String detailUrl = e.select(".content h2 a").attr("href");
                    Document detailDoc = getDoc(detailUrl);
                    Element detail = detailDoc.select("a[class='downbtn']").first();
                    String title = detail.attr("title");

                    sb.append(title).append(System.lineSeparator());

                    String intro = e.select(".content p").first().text();

                    sb.append(intro).append(System.lineSeparator());

                    String downloadUrl = detail.attr("href");
                    Document downloadDoc = getDoc(downloadUrl);
                    Elements pList = downloadDoc.select(".desc").first().select("p");
                    String code = pList.get(pList.size() - 2).text();

                    sb.append(code).append(System.lineSeparator());

                    for (Element download : downloadDoc.select(".list a")) {
                        sb.append(download.text()).append(":").append(download.attr("href")).append(System.lineSeparator());

                    }
                    sb.append(System.lineSeparator());
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }

        return sb.toString();
    }
    
    private Document getDoc(String url) throws IOException {
        Connection con = Jsoup.connect(url);
        con.header("referer", "http://mebook.cc/");
        con.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:47.0) Gecko/20100101 Firefox/47.0");
        Response rs = con.execute();
        return Jsoup.parse(rs.body());
    }
}
