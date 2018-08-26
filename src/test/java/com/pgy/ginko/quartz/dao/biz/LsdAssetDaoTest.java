package com.pgy.ginko.quartz.dao.biz;

import com.pgy.ginko.quartz.model.biz.LsdAssetDo;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by Wangmx on 2018/8/26 .
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LsdAssetDaoTest {
    @Resource
    private LsdAssetDao lsdAssetDao;

    @Test
    public void getAssetByBorrowIdTypeNoFinished() throws Exception {
        LsdAssetDo lsdAssetDo = lsdAssetDao.getAssetByBorrowIdTypeNoFinished(605540L, 1);

        System.out.println(lsdAssetDo.toString());

    }

}