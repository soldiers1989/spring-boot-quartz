package com.pgy.ginko.quartz.controller.test;

import com.pgy.ginko.quartz.controller.BaseMockTest;
import lombok.extern.slf4j.Slf4j;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ginko
 * @description 测试单一接口时 ，也可利用注解@WebMvcTest 进行单一测试
 * 使用 WebMvcTest 时使用@autowired mockMvc 是可自动注入的。
 * 当直接使用SpringBootTest 会提示 注入失败  这里直接示例利用 MockMvcBuilders工具创建
 * @date 2018/8/28 20:04
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class MultiControllerTest extends BaseMockTest {

    @Rule
    public ContiPerfRule contiPerfRule = new ContiPerfRule();

    @Test
    @PerfTest(invocations = 100, threads = 10)
    //10个线程 执行10次
    public void mockTest() throws Exception {

        String msg = "this is a mock test";

        MvcResult result = this.mockMvc
                .perform(get("/test/mock").param("msg", msg))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        //断言 是否和预期相等
        Assert.assertEquals(msg, result.getResponse().getContentAsString());

    }

}