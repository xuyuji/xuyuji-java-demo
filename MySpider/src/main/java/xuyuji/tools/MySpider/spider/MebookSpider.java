package xuyuji.tools.MySpider.spider;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class MebookSpider {

    public String fetch() {
        StringBuilder sb = new StringBuilder();

        try {
            Document indexDoc = getDoc("http://mebook.cc/");
            Pattern p = Pattern.compile("\\d{4}.\\d{2}.\\d{2}");
            for (Element e : indexDoc.select("#primary .list li")) {
                Matcher m = p.matcher(e.select(".info").text());
                if (m.find() && DateFormatUtils.format(new Date(), "yyyy.MM.dd").equals(m.group())) {
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
