package com.pgy.ginko.quartz.controller;

import com.pgy.ginko.quartz.common.response.CommonResponse;
import com.pgy.ginko.quartz.common.response.ResponseUtil;
import com.pgy.ginko.quartz.model.test.Product;
import com.pgy.ginko.quartz.model.test.ScheduleJob;
import com.pgy.ginko.quartz.model.test.User;
import com.pgy.ginko.quartz.service.test.JobService;
import com.pgy.ginko.quartz.service.test.ProductService;
import com.pgy.ginko.quartz.service.test.UserService;
import com.pgy.ginko.quartz.utils.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ginko
 * @description 测试Controller
 * @date 2018/8/22 20:53
 */
@Slf4j
@RestController
@RequestMapping("/multi")
@Api(tags = "1.3", description = "接口联合测试", value = "接口联合测试")
public class MultiController {

    @Resource
    private ProductService productService;

    @Resource
    private UserService userService;

    @Resource
    private JobService jobService;

    @GetMapping("/product")
    @ApiOperation(value = "产品列表")
    public CommonResponse testProducts() {
        List<Product> list = productService.getAllProduct();
        return ResponseUtil.generateResponse(list);
    }

    @GetMapping("/product/{id}")
    @ApiOperation(value = "条件查询产品")
    public CommonResponse testProduct(@PathVariable("id") Long productId) throws ServiceException {
        return ResponseUtil.generateResponse(productService.select(productId));
    }

    @GetMapping("/user")
    @ApiOperation(value = "用户列表")
    public CommonResponse testUsers() {
        List<User> list = userService.getAllUser();
        return ResponseUtil.generateResponse(list);
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "条件查询用户")
    public CommonResponse testUser(@PathVariable("id") Long userId) throws ServiceException {

        return ResponseUtil.generateResponse(userService.select(userId));
    }

    @GetMapping("/job")
    @ApiOperation(value = "定时任务列表")
    public CommonResponse getAllJob() {
        List<ScheduleJob> list = jobService.getAllJob();
        return ResponseUtil.generateResponse(list);
    }

    @GetMapping("/job/{id}")
    @ApiOperation(value = "条件查询定时任务")
    public CommonResponse getJob(@PathVariable("id") Long id) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.select(id));
    }
}
