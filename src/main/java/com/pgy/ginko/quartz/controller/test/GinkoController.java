package com.pgy.ginko.quartz.controller.test;

import com.pgy.ginko.quartz.service.biz.impl.HttpApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ginko
 * @description 测试Controller
 * @date 2018/8/22 20:53
 */
@Slf4j
@RestController
@RequestMapping("/test")
@Api(tags = "1.0", description = "接口测试", value = "接口测试")
public class GinkoController {

    @Resource
    private HttpApiService httpApiService;

    @RequestMapping("httpclient")
    @ApiOperation(value = "测试HttpApiService")
    public String test() throws Exception {
        String str = httpApiService.doGet("http://www.baidu.com");
        System.out.println(str);
        return "hello";
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "测试条件查询")
    public void test(@PathVariable("id") Long jobId) {
        log.info(jobId + "");
    }
}
