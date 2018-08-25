package com.pgy.ginko.quartz.controller.test;

import com.pgy.ginko.quartz.common.response.CommonResponse;
import com.pgy.ginko.quartz.common.response.ResponseUtil;
import com.pgy.ginko.quartz.model.test.ScheduleJob;
import com.pgy.ginko.quartz.service.test.JobService;
import com.pgy.ginko.quartz.utils.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description 定时任务管理
 * @author ginko
 * @date 2018-8-22 14:01:16
 *
 */
@RestController
@RequestMapping("/job")
@Api(tags = "1.1", description = "定时任务管理", value = "定时任务管理")
public class JobController {

    @Resource
    private JobService jobService;

    @GetMapping
    @ApiOperation(value = "定时任务列表")
    public CommonResponse getAllJob() {
        return ResponseUtil.generateResponse(jobService.getAllJob());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "条件查询定时任务")
    public CommonResponse getJob(@PathVariable("id") Long id) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.select(id));
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "条件更新定时任务")
    public CommonResponse updateJob(@PathVariable("id") Long id, @RequestBody ScheduleJob newScheduleJob) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.update(id, newScheduleJob));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "条件删除定时任务")
    public CommonResponse deleteJob(@PathVariable("id") Long id) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.delete(id));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存定时任务记录")
    public CommonResponse saveJob(@RequestBody ScheduleJob newScheduleJob) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.add(newScheduleJob));
    }


    @PutMapping("/run/{id}")
    @ApiOperation(value = "运行条件定时任务")
    public CommonResponse runJob(@PathVariable("id") Long id) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.run(id));
    }


    @PutMapping("/pause/{id}")
    @ApiOperation(value = "暂停条件定时任务")
    public CommonResponse pauseJob(@PathVariable("id") Long id) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.pause(id));
    }

    @PutMapping("/resume/{id}")
    @ApiOperation(value = "恢复条件定时任务")
    public CommonResponse resumeJob(@PathVariable("id") Long id) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.resume(id));
    }

}
