package com.pgy.ginko.quartz.controller.test;

import com.pgy.ginko.quartz.common.response.CommonResponse;
import com.pgy.ginko.quartz.common.response.ResponseUtil;
import com.pgy.ginko.quartz.model.test.Product;
import com.pgy.ginko.quartz.service.test.ProductService;
import com.pgy.ginko.quartz.utils.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ginko
 * @description ProductController
 * @date 2018/8/23 10:43
 */
@RestController
@RequestMapping("/product")
@Api(tags = "1.2", description = "定时任务管理", value = "定时任务管理")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/{id}")
    @ApiOperation(value = "条件查询产品")
    public CommonResponse getProduct(@PathVariable("id") Long productId) throws ServiceException {
        return ResponseUtil.generateResponse(productService.select(productId));
    }

    @GetMapping
    @ApiOperation(value = "产品列表")
    public CommonResponse getAllProduct() {
        return ResponseUtil.generateResponse(productService.getAllProduct());
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "条件更新产品")
    public CommonResponse updateProduct(@PathVariable("id") Long productId, @RequestBody Product newProduct) throws ServiceException {
        return ResponseUtil.generateResponse(productService.update(productId, newProduct));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "条件删除产品")
    public CommonResponse deleteProduct(@PathVariable("id") long productId) throws ServiceException {
        return ResponseUtil.generateResponse(productService.delete(productId));
    }

    @PostMapping
    @ApiOperation(value = "保存产品记录")
    public CommonResponse addProduct(@RequestBody Product newProduct) throws ServiceException {
        return ResponseUtil.generateResponse(productService.add(newProduct));
    }
}