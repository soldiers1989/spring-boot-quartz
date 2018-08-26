package com.pgy.ginko.quartz.service.biz.impl;


import com.pgy.ginko.quartz.annotation.DataSource;
import com.pgy.ginko.quartz.common.enums.DataSourceKey;
import com.pgy.ginko.quartz.dao.biz.LsdRenewalDetailDao;
import com.pgy.ginko.quartz.model.biz.LsdRenewalDetailDo;
import com.pgy.ginko.quartz.service.biz.LsdRenewalDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.BaseMapper;

import javax.annotation.Resource;

/**
 * 续借记录ServiceImpl
 *
 * @author jiangtingdong
 * @version 1.0.0 初始化
 * @date 2017-07-24 21:25:45 Copyright 本内容仅限于杭州霖梓网络科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Slf4j
@DataSource(DataSourceKey.BIZ)
@Service("lsdRenewalDetailService")
public class LsdRenewalDetailServiceImpl extends BaseServiceImpl<LsdRenewalDetailDo> implements LsdRenewalDetailService {

    @Resource
    private LsdRenewalDetailDao lsdRenewalDetailDao;

    @Override
    public BaseMapper<LsdRenewalDetailDo> getMapper() {
        return lsdRenewalDetailDao;
    }

    @Override
    public LsdRenewalDetailDo getProcessingRenewalByBorrowId(Long borrowId) {
        return lsdRenewalDetailDao.getProcessingRenewalByBorrowId(borrowId);
    }



}