package com.pgy.ginko.quartz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
    @ApiOperation(value = "测试条件查询")
    public void test(@PathVariable("id") Long jobId) {
        log.info(jobId + "");
    }
}
