package com.pgy.ginko.quartz.controller;

import com.pgy.ginko.quartz.common.enums.ResourceSecType;
import com.pgy.ginko.quartz.common.enums.ResourceType;
import com.pgy.ginko.quartz.common.response.CommonResponse;
import com.pgy.ginko.quartz.common.response.ResponseUtil;
import com.pgy.ginko.quartz.model.biz.LsdBorrowCashDo;
import com.pgy.ginko.quartz.model.biz.LsdResourceDo;
import com.pgy.ginko.quartz.service.biz.*;
import com.pgy.ginko.quartz.service.biz.utils.CollectionSystemUtil;
import com.pgy.ginko.quartz.service.biz.utils.SmsUtil;
import com.pgy.ginko.quartz.utils.DateUtil;
import com.pgy.ginko.quartz.utils.NumberUtil;
import com.pgy.ginko.quartz.utils.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Wangmx
 * @date 2018/8/25 0025.
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
    private LsdRepaymentBorrowCashService lsdRepaymentBorrowCashService;

    @Resource
    private LsdRenewalDetailService lsdRenewalDetailService;

    @Resource
    private LsdAssetService lsdAssetService;

    @Resource
    private SmsUtil smsUtil;

    private ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    @GetMapping("/borrowCashOverdue/execute")
    @ApiOperation(value = "条件查询产品")
    public CommonResponse syncExecute(@PathVariable("id") Long rid) throws ServiceException {

        log.info("Start sync collection data, StartTime=" + new Date());

        LsdResourceDo lsdResourceDo = lsdResourceService.getResourceByTypeAndSecType(ResourceType.COLLECTION_DATE_CONFIGURATION.getCode(), ResourceSecType.COLLECTION_DATE_CONFIGURATION.getCode());
        Date currDate = new Date();

        Date yesterdayDate = DateUtil.addDays(currDate, NumberUtil.strToIntWithDefault("-" + lsdResourceDo.getValue(), -1));
        try {
            final int pageSize = 5000;
            Long totalRecord = lsdBorrowCashService.getBorrowCashOverdueMaxCount();

            if (totalRecord == null || totalRecord == 0) {
                log.info("borrowCashOverdueJob run finished, cashOverdueList size is 0. time=" + new Date());
                return ResponseUtil.generateResponse(Boolean.FALSE);
            }
            final int totalPageNum = (int) ((totalRecord + pageSize - 1) / pageSize);
            log.info("borrowCashOverdueJob totalPageNum=" + totalPageNum);

            // 1、统计本平台逾期信息
            log.info("borrowCashOverdueJob calcuOverdueRecords start,time=" + new Date());

            // 保障线程执行完逾期利息计算后才给催收平台同步逾期信息;
            CountDownLatch latch = new CountDownLatch(totalPageNum);

            for (int i = 0; i < totalPageNum; i++) {// 分页查询操作
                log.info("borrowCashOverdueJob calculate OverdueRecords beginId=" + i * pageSize + ",endId=" + (i + 1) * pageSize);
                List<LsdBorrowCashDo> cashOverdueList = lsdBorrowCashService.getBorrowCashOverdueByBorrowId(i * pageSize + 1, (i + 1) * pageSize);
                threadPool.execute(new OverdueTask(cashOverdueList, latch));
            }

            latch.await();
        } catch (Exception e) {
            log.error("Job:borrowCashOverdueSyncCollection 执行计算逾期费时异常!errorMsg:" + e.getMessage());
            return ResponseUtil.generateResponse(Boolean.FALSE);

        }

        //启用线程池  每10条数据发送一次  线程池 最大容量为20  阻塞队列极大 执行完新增之后 再去执行  update 数据 避免 update 执行影响到 第一天逾期入催
        // 第一天逾期入催 数据是新增
        // 另外 对于同步状态 为 1 的 数据 是没有新增成功的 虽然日期 大于1天 但也需要新增,有另外的定时任务执行更新
        try {
            List<Long> borrowIdList = lsdBorrowCashService.getBorrowOverdueFirstDayCount(yesterdayDate, NumberUtil.strToIntWithDefault(lsdResourceDo.getValue(), 1));

            List<Long> borrowIdTemp = new ArrayList<>();
            borrowIdTemp.addAll(borrowIdList);
            if (!CollectionUtils.isEmpty(borrowIdList)) {
                //每100 条执行一次
                int count = borrowIdList.size();
                int pageSize = 100;
                int pageSum = (count - 1) / 100 + 1;
                CountDownLatch countDown = new CountDownLatch(pageSum);

                for (int i = 0; i < pageSum; i++) {
                    threadPool.execute(new BorrowCashOverdueSyncCollection.SyncDataToCollection(borrowIdList.subList(i * pageSize, (i + 1) * pageSize >= count ? count : (i + 1) * pageSize), TransOverdueBorrowCashType.ADD.getCode(), countDown));
                }
                countDown.await();
            }
        } catch (Exception e) {
            Long time = System.currentTimeMillis();
            log.error(time + "job:borrowCashOverdueSyncCollection 第一天入催任务执行失败!errorMsg:" + e.getMessage());

            List<LsdResourceDo> sendMailMobiles = lsdResourceService.getResourceByType(ResourceType.SYNC_COLLECTION_FAILED_SEND_OPERATOR.getCode());
            for (LsdResourceDo sendMailMobile : sendMailMobiles) {
                try {
                    smsUtil.sendZhPushException(sendMailMobile.getValue(), "执行第一天逾期入催任务失败！请及时查看。Time:" + time);
                } catch (Exception smsException) {
                    log.error(time + " job:borrowCashOverdueSyncCollection 第一天入催任务执行失败发送短信异常! errorMsg:" + smsException.getMessage());
                }
            }
        }

        try {
            List<Long> borrowIdList = lsdBorrowCashService.getBorrowOverdueAnotherDayCount(yesterdayDate);
            if (borrowIdList != null && borrowIdList.size() > 0) {
                //每100 条执行一次
                int count = borrowIdList.size();
                int pageSize = 100;
                int pageSum = (count - 1) / 100 + 1;
                CountDownLatch countDown = new CountDownLatch(pageSum);

                for (int i = 0; i < pageSum; i++) {
                    threadPool.execute(new BorrowCashOverdueSyncCollection.SyncDataToCollection(borrowIdList.subList(i * pageSize, (i + 1) * pageSize >= count ? count : (i + 1) * pageSize), TransOverdueBorrowCashType.UPDATE.getCode(), countDown));
                }

                countDown.await();
            }
        } catch (Exception e) {
            Long time = (new Date()).getTime();
            log.error(time + "job:borrowCashOverdueSyncCollection 执行逾期数据更新催收系统案件数据任务失败!errorMsg:" + e.getMessage());
            List<LsdResourceDo> sendMailMobiles = lsdResourceService.getResourceByType(ResourceType.SYNC_COLLECTION_FAILED_SEND_OPERATOR.getCode());

            for (LsdResourceDo sendMailMobile : sendMailMobiles) {
                try {
                    smsUtil.sendZhPushException(sendMailMobile.getValue(), "执行逾期数据更新催收系统案件数据任务失败！请及时查看。Time:" + time);
                } catch (Exception e1) {
                    log.error(time + "job:borrowCashOverdueSyncCollection 执行逾期数据更新催收系统案件数据任务失败!发送短信异常! errorMsg:" + e.getMessage());
                }
            }
        }
        log.info("End sync collection data,EndTime=" + new Date());

        return ResponseUtil.generateResponse(Boolean.TRUE);
    }

    class OverdueTask implements Runnable {

        private List<LsdBorrowCashDo> borrowList;
        CountDownLatch latch;

        public OverdueTask(List<LsdBorrowCashDo> borrowList, CountDownLatch latch) {
            this.borrowList = borrowList;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                if (borrowList.size() > 0) {
                    // TODO
//                    calculateOverdueRecords(borrowList);
                }
            } catch (Exception e) {
                log.error("逾期费计算异常! errorMsg:" + e);
            } finally {
                latch.countDown();
            }
        }

    }

}
