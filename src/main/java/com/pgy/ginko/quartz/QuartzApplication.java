package com.pgy.ginko.quartz;

import com.battcn.swagger.annotation.EnableSwagger2Doc;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ginko
 * @description 定时任务
 * @date 2018-8-22 13:25:56
 */

@EnableScheduling
@EnableSwagger2Doc
@SpringBootApplication
@DisallowConcurrentExecution
public class QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
    }


}
