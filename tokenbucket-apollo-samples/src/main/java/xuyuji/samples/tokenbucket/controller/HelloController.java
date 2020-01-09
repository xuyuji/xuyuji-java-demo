package xuyuji.samples.tokenbucket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xuyuji.samples.tokenbucket.ratelimit.AccessRateLimit;

@Controller
public class HelloController {

    @Autowired
    private AccessRateLimit accessRateLimit;

    @ResponseBody
    @RequestMapping("/")
    public String hello() {
        if (accessRateLimit.visit()) {
            return "hello";
        } else {
            return "限制访问";
        }
    }
}
