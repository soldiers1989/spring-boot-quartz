package com.pgy.ginko.quartz.controller;

import com.pgy.ginko.quartz.common.response.CommonResponse;
import com.pgy.ginko.quartz.common.response.ResponseUtil;
import com.pgy.ginko.quartz.service.biz.*;
import com.pgy.ginko.quartz.service.biz.utils.CollectionSystemUtil;
import com.pgy.ginko.quartz.utils.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author  Wangmx
 * @date  2018/8/25 0025.
 */

@Slf4j
@RestController
@RequestMapping("/sync")
@Api(tags = "1.5", description = "业务数据同步催收系统", value = "业务数据同步催收系统")
public class BorrowCashOverdueSyncCollectionController {

    @Resource
    private LsdBorrowCashService lsdBorrowCashService;

    @Resource
    private LsdResourceService lsdResourceService;

    @Resource
    private LsdBorrowCashOverdueService lsdBorrowCashOverdueService;

    @Resource
    private CollectionSystemUtil collectionSystemUtil;

    @Resource
    private LsdRepaymentBorrowCashService lsdRepaymentBorrowCashService;

    @Resource
    private LsdRenewalDetailService lsdRenewalDetailService;

    @Resource
    private LsdAssetService lsdAssetService;

    @GetMapping("/{id}")
    @ApiOperation(value = "条件查询产品")
    public CommonResponse syncExecute(@PathVariable("id") Long rid) throws ServiceException {

        return ResponseUtil.generateResponse(lsdResourceService.selectByPrimaryKey(rid));

    }

}
