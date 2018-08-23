package com.pgy.ginko.quartz.controller;

import com.pgy.ginko.quartz.common.ResponseUtil;
import com.pgy.ginko.quartz.service.ProductService;
import com.pgy.ginko.quartz.service.UserService;
import com.pgy.ginko.quartz.utils.ServiceException;
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
@RequestMapping("/multi")
@Api(tags = "1.3", description = "接口联合测试", value = "接口联合测试")
public class MultiController {

    @Resource
    private ProductService productService;

    @Resource
    private UserService userService;

    @GetMapping("/product")
    @ApiOperation(value = "产品列表")
    public void testProducts() {
        ResponseUtil.generateResponse(productService.getAllProduct());
    }

    @GetMapping("/product/{id}")
    @ApiOperation(value = "条件查询产品")
    public void testProduct(@PathVariable("id") Long productId) throws ServiceException {
        ResponseUtil.generateResponse(productService.select(productId));
    }

    @GetMapping("/user")
    @ApiOperation(value = "用户列表")
    public void testUsers() {

        ResponseUtil.generateResponse(userService.getAllUser());
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "条件查询用户")
    public void testUser(@PathVariable("id") Long userId) throws ServiceException {

        ResponseUtil.generateResponse(userService.select(userId));
    }
}
