package com.pgy.ginko.quartz.controller;

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
public class GinkoController {

    @GetMapping("/{id}")
    @ApiOperation(value = "条件查询（DONE）")
    public void test(@PathVariable("id") Long jobId) {
        log.info(jobId + "");
    }
}
