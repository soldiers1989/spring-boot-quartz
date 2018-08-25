package com.pgy.ginko.quartz.controller;

import com.pgy.ginko.quartz.service.biz.utils.HttpApiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by ginko
 * 2018-8-24 10:29:05
 */
@RestController
public class HttpClientController {

    @Resource
    private HttpApiService httpApiService;

    @RequestMapping("httpclient")
    public String test() throws Exception {
        String str = httpApiService.doGet("http://www.baidu.com");
        System.out.println(str);
        return "hello";
    }
}
