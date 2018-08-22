package com.pgy.ginko.quartz.controller;

import com.pgy.ginko.quartz.common.CommonResponse;
import com.pgy.ginko.quartz.common.ResponseUtil;
import com.pgy.ginko.quartz.model.ScheduleJob;
import com.pgy.ginko.quartz.service.JobService;
import com.pgy.ginko.quartz.utils.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "1.0", description = "定时任务管理", value = "定时任务管理")
public class JobController {

    @Resource
    private JobService jobService;

    @GetMapping
    public CommonResponse getAllJob() {
        return ResponseUtil.generateResponse(jobService.getAllJob());
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "条件查询（DONE）")
    public CommonResponse getJob(@PathVariable("id") Long jobId) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.select(jobId));
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "条件更新（DONE）")
    public CommonResponse updateJob(@PathVariable("id") Long jobId, @RequestBody ScheduleJob newScheduleJob) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.update(jobId, newScheduleJob));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "条件删除（DONE）")
    public CommonResponse deleteJob(@PathVariable("id") Long jobId) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.delete(jobId));
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存记录（DONE）")
    public CommonResponse saveJob(@RequestBody ScheduleJob newScheduleJob) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.add(newScheduleJob));
    }


    @PutMapping("/run/{id}")
    @ApiOperation(value = "运行条件定时任务（DONE）")
    public CommonResponse runJob(@PathVariable("id") Long jobId) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.run(jobId));
    }


    @PutMapping("/pause/{id}")
    @ApiOperation(value = "暂停条件定时任务（DONE）")
    public CommonResponse pauseJob(@PathVariable("id") Long jobId) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.pause(jobId));
    }

    @PutMapping("/resume/{id}")
    @ApiOperation(value = "恢复条件定时任务（DONE）")
    public CommonResponse resumeJob(@PathVariable("id") Long jobId) throws ServiceException {
        return ResponseUtil.generateResponse(jobService.resume(jobId));
    }

}
