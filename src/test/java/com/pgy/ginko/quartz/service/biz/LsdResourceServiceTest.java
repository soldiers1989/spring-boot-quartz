package com.pgy.ginko.quartz.service.biz;

import com.pgy.ginko.quartz.common.enums.ResourceSecType;
import com.pgy.ginko.quartz.common.enums.ResourceType;
import com.pgy.ginko.quartz.model.biz.LsdResourceDo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Wangmx on 2018/8/26 0026.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LsdResourceServiceTest {

    @Resource
    private LsdResourceService lsdResourceService;

    @Test
    public void getResourceByType() throws Exception {
        List<LsdResourceDo> lsdResourceDo = lsdResourceService.getResourceByType(ResourceType.COLLECTION_DATE_CONFIGURATION.getCode());
        System.out.println(lsdResourceDo);

    }

    @Test
    public void getResourceByTypeAndSecType() throws Exception {
        LsdResourceDo lsdResourceDo = lsdResourceService.getResourceByTypeAndSecType(ResourceType.COLLECTION_DATE_CONFIGURATION.getCode(), ResourceSecType.COLLECTION_DATE_CONFIGURATION.getCode());
        System.out.println(lsdResourceDo);

    }

}