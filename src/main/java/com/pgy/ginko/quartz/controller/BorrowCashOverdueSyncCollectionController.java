package com.pgy.ginko.quartz.controller;

import com.pgy.ginko.quartz.common.constant.BizConstants;
import com.pgy.ginko.quartz.common.enums.*;
import com.pgy.ginko.quartz.common.http.HttpResult;
import com.pgy.ginko.quartz.common.response.CommonResponse;
import com.pgy.ginko.quartz.common.response.ResponseUtil;
import com.pgy.ginko.quartz.model.biz.*;
import com.pgy.ginko.quartz.model.collection.Bo.CollectionSystemReqRespBo;
import com.pgy.ginko.quartz.service.biz.*;
import com.pgy.ginko.quartz.service.biz.utils.CollectionService;
import com.pgy.ginko.quartz.service.biz.impl.HttpApiService;
import com.pgy.ginko.quartz.service.biz.impl.RedisService;
import com.pgy.ginko.quartz.service.biz.utils.SmsUtil;
import com.pgy.ginko.quartz.utils.BigDecimalUtil;
import com.pgy.ginko.quartz.utils.DateUtil;
import com.pgy.ginko.quartz.utils.NumberUtil;
import com.pgy.ginko.quartz.utils.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private HttpApiService httpApiService;

    @Resource
    private CollectionService collectionService;

    @Resource
    private RedisService redisService;

    @Resource
    private SmsUtil smsUtil;

    @Value("${pgy.collection.url}")
    private static String collectionUrl;

    private ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    @GetMapping("/borrowCashOverdue/execute")
    @ApiOperation(value = "业务数据同步催收系统")
    public CommonResponse syncExecute() throws ServiceException {

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
            log.info("borrowCashOverdueJob calculateOverdueRecords start,time=" + new Date());

            // 保障线程执行完逾期利息计算后才给催收平台同步逾期信息;
            CountDownLatch latch = new CountDownLatch(totalPageNum);

            for (int i = 0; i < totalPageNum; i++) {// 分页查询操作
                log.info("borrowCashOverdueJob calculateOverdueRecords beginId=" + i * pageSize + ",endId=" + (i + 1) * pageSize);
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

            if (!CollectionUtils.isEmpty(borrowIdList)) {
                //每100 条执行一次
                int count = borrowIdList.size();
                int pageSize = 100;
                int pageSum = (count - 1) / 100 + 1;
                CountDownLatch countDown = new CountDownLatch(pageSum);

                for (int i = 0; i < pageSum; i++) {
                    threadPool.execute(new SyncDataToCollection(borrowIdList.subList(i * pageSize, (i + 1) * pageSize >= count ? count : (i + 1) * pageSize), TransOverdueBorrowCashType.ADD.getCode(), countDown));
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
                    threadPool.execute(new SyncDataToCollection(borrowIdList.subList(i * pageSize, (i + 1) * pageSize >= count ? count : (i + 1) * pageSize), TransOverdueBorrowCashType.UPDATE.getCode(), countDown));
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
                    calculateOverdueRecords(borrowList);
                }
            } catch (Exception e) {
                log.error("逾期费计算异常! errorMsg:" + e);
            } finally {
                latch.countDown();
            }
        }
    }

    /**
     * 计算处理本平台逾期借款数据,返回待传输数据
     *
     * @param cashOverdueList
     */
    private void calculateOverdueRecords(List<LsdBorrowCashDo> cashOverdueList) {
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 已逾期账单
        if (cashOverdueList != null) {
            log.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calculateOverdueRecords size is " + cashOverdueList.size());
        } else {
            log.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calculateOverdueRecords cashOverdueList is null ");
        }

        LsdResourceDo overdueConfig = lsdResourceService.getResourceByTypeAndSecType(ResourceType.OVERDUE_CONFIG.getCode(), ResourceSecType.OVERDUE_CONFIG.getCode());
        for (LsdBorrowCashDo cashDo : cashOverdueList) {
            //重新检查借款订单的最新值，避免覆盖期间还款等信息
            try {
                cashDo = lsdBorrowCashService.getBorrowCashById(cashDo.getRid());

                if (null == cashDo.getGmtArrival()) {
                    log.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calculateOverdueRecords " + cashDo.getBorrowNo() + " 该用户没有到账。。。");
                    continue;
                }
                LsdRepaymentBorrowCashDo afRepaymentBorrowCashDo = lsdRepaymentBorrowCashService.getProcessingRepaymentByBorrowId(cashDo.getRid());
                // 当前逾期费用 = 借款金额 + 累计已还逾期费 + 累计已还利息 - 还款金额
                BigDecimal currentAmount = BigDecimalUtil.add(cashDo.getAmount(), cashDo.getSumRate(), cashDo.getSumOverdue()).subtract(cashDo.getRepayAmount());// 当前本金
                if (afRepaymentBorrowCashDo != null) {// 过滤掉还款中的金额
                    log.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calculateOverdueRecords " + cashDo.getBorrowNo() + " 该用户正在还款中。。。");
                    currentAmount = BigDecimalUtil.add(cashDo.getAmount(), cashDo.getOverdueAmount(), cashDo.getRateAmount(), cashDo.getSumRate(), cashDo.getSumOverdue()).subtract(cashDo.getRepayAmount()).subtract(afRepaymentBorrowCashDo.getRepaymentAmount());// 当前本金
                }
                if (currentAmount.compareTo(BigDecimal.ZERO) == 0) {
                    log.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calculateOverdueRecords " + cashDo.getBorrowNo() + " 该用户已经扣除逾期费用。。。");
                    continue;
                }

                // 过滤掉续借中的数据
                LsdRenewalDetailDo afRenewalDetailDo = lsdRenewalDetailService.getProcessingRenewalByBorrowId(cashDo.getRid());
                if (afRenewalDetailDo != null) {
                    log.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calculateOverdueRecords " + cashDo.getBorrowNo() + " 该用户正在续借。。。");
                    continue;
                }

                String lockKey = BizConstants.CACHE_KEY_APPLY_RENEWAL_LOCK + cashDo.getUserId();
                boolean getLock = redisService.set(lockKey, "1", 30L);
                try {
                    if (getLock) {

                        BigDecimal oldOverdueAmount = cashDo.getOverdueAmount();// 当前逾期
                        // 当前逾期费用 = 借款金额 + 累计已还逾期费 + 累计已还利息 - 还款金额
                        // 逾期费 = 当前逾期金额 *（ 央行基准费率 （默认为0 ） * 借钱最高倍数（ 默认 为 4 ） + 借钱手续费率（日） + 借钱逾期手续费率（日） ）
                        LsdResourceDo lsdResourceDo = lsdResourceService.getResourceByTypeAndSecType(BizConstants.NEW_BORROW_OVERDUE_POUNDAGE_CONFIG, BizConstants.NEW_BORROW_OVERDUE_POUNDAGE_VALUE);
                        BigDecimal poundage = new BigDecimal(0.02);
                        if (lsdResourceDo != null) {
                            poundage = new BigDecimal(lsdResourceDo.getValue());
                        }
                        BigDecimal newOverdueAmount = currentAmount.multiply(poundage).setScale(6, BigDecimal.ROUND_HALF_UP);
                        BigDecimal realInterest = newOverdueAmount; //增加逾期费增长限制后用户每天实际增加的应还逾期费
                        if (newOverdueAmount.compareTo(BigDecimal.ZERO) > 0) {
                            if (NumberUtil.strToInt(overdueConfig.getValue()) == 1) {
                                // 如果当前逾期超出本金的配置比例
                                if (cashDo.getAmount().multiply(new BigDecimal(overdueConfig.getValue1())).compareTo(cashDo.getOverdueAmount().add(newOverdueAmount).add(cashDo.getSumOverdue())) < 0) {
                                    BigDecimal oldOverdue = cashDo.getOverdueAmount();
                                    cashDo.setOverdueAmount((cashDo.getAmount().multiply(new BigDecimal(overdueConfig.getValue1())).subtract(cashDo.getSumOverdue())).compareTo(BigDecimal.ZERO) < 0
                                            ? BigDecimal.ZERO : cashDo.getAmount().multiply(new BigDecimal(overdueConfig.getValue1())).subtract(cashDo.getSumOverdue()));
                                    cashDo.setRealOverdue(cashDo.getRealOverdue().add(newOverdueAmount));
                                    realInterest = (cashDo.getAmount().multiply(new BigDecimal(overdueConfig.getValue1())).subtract(oldOverdue).subtract(cashDo.getSumOverdue()).compareTo(BigDecimal.ZERO) < 0
                                            ? BigDecimal.ZERO : cashDo.getAmount().multiply(new BigDecimal(overdueConfig.getValue1())).subtract(oldOverdue).subtract(cashDo.getSumOverdue()));
                                } else {
                                    cashDo.setOverdueAmount(oldOverdueAmount.add(newOverdueAmount));
                                    cashDo.setRealOverdue(cashDo.getRealOverdue().add(newOverdueAmount));
                                }
                            } else {
                                cashDo.setOverdueAmount(oldOverdueAmount.add(newOverdueAmount));
                            }
                        }
                        cashDo.setOverdueDay(cashDo.getOverdueDay() + 1);
                        if (cashDo.getOverdueDay() == BizConstants.OVERDUE_30_DAYS) {
                            Map<String, Object> params = new HashMap<>();
                            params.put("consumerNo", cashDo.getUserId() + "");
                            params.put("category", 1 + "");
                            params.put("remark", "逾期三十天用户");
                            params.put("source", "fx");
                            HttpResult reqResult = httpApiService.doPost(collectionUrl + "/modules/api/user/action/addUserBlack.htm", params);
                            log.info("borrowCashOverdueSyncCollection addUserBlack result:", reqResult);
                        }
                        cashDo.setOverdueStatus(YesNoStatus.YES.getCode());
                        cashDo.setCurrentOverdueStatus(YesNoStatus.YES.getCode());
                        lsdBorrowCashService.updateByPrimaryKeySelective(cashDo);
                        // 新增逾期记录
                        lsdBorrowCashOverdueService.insert(buildCashOverdue(cashDo.getRid(), currentAmount, newOverdueAmount, realInterest, cashDo.getUserId()));
                        log.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calculateOverdueRecords " + cashDo.getBorrowNo() + "  添加逾期记录。。。");
                        // 更新资产表
                        LsdAssetDo lsdAssetDo = lsdAssetService.findAssetByBorrowIdTypeNoFinished(cashDo.getRid(), cashDo.getBorrowType());
                        if (lsdAssetDo != null) {
                            log.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calculateOverdueRecords " + cashDo.getBorrowNo() + " start update lsd_asset table ,lsd_asset id is " + lsdAssetDo.getId());
                            lsdAssetDo.setOverdueStatus(OverdueStatus.OVERDUE.getCode());
                            lsdAssetDo.setOverdueDay(BigDecimalUtil.removeNull(lsdAssetDo.getOverdueDay()) + 1);
                            lsdAssetDo.setOverdueAmount(cashDo.getOverdueAmount());
                            lsdAssetDo.setRealOverdue(lsdAssetDo.getRealOverdue().add(newOverdueAmount));

                            if (DateUtil.compare(new Date(), lsdAssetDo.getGmtPlanAssetrepay())) {
                                lsdAssetDo.setOverdueAssetStatus(OverdueStatus.OVERDUE.getCode());
                                lsdAssetDo.setOverdueAssetAmount(lsdAssetDo.getOverdueAssetAmount().add(newOverdueAmount));
                                lsdAssetDo.setOverdueAssetDay(lsdAssetDo.getOverdueAssetDay() + 1);
                            }
                            log.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calculateOverdueRecords " + cashDo.getBorrowNo() + " update lsdasset id " + lsdAssetDo.getId() + " lsdasset is " + lsdAssetDo.getBorrowNo());
                            lsdAssetService.updateByPrimaryKeySelective(lsdAssetDo);
                        }
                    }
                } finally {
                    if (getLock) {
                        redisService.del(lockKey);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("borrowCashOverdueSyncCollection calculateOverdueRecords error borrowCashId=" + cashDo.getRid() + ",error=", e);
            }
        }
    }

    class SyncDataToCollection implements Runnable {
        List<Long> borrowIds;
        String dataType;
        CountDownLatch count;

        public SyncDataToCollection(List<Long> borrowIds, String dataType, CountDownLatch count) {
            this.borrowIds = borrowIds;
            this.dataType = dataType;
            this.count = count;
        }

        @Override
        public void run() {

            //线程运行时的异常必须捕捉  因为异常可能会造成countDown 无法结束
            try {
                if (borrowIds != null && borrowIds.size() > 0) {
                    //将数据PUSH 到催收系统  逾期超过一天的数据进行修改 逾期一天的数据 新增
                    if (TransOverdueBorrowCashType.ADD.getCode().equals(dataType)) {
                        CollectionSystemReqRespBo respBo = collectionService.syncAddDataToCollection(borrowIds);
                        log.info("borrowCashOverdueSyncCollection sync collection add data borrowIds:(" + borrowIds.toString() + ") result:" + respBo.getMsg());
                    } else {
                        CollectionSystemReqRespBo respBo = collectionService.syncUpdateDataToCollection(borrowIds);
                        log.info("borrowCashOverdueSyncCollection sync collection update data borrowIds:(" + borrowIds.toString() + ") result:" + respBo.getMsg());
                    }
                }
            } catch (Exception e) {
                try {
                    Long time = System.currentTimeMillis();
                    log.error(time + "borrowCashOverdueSyncCollection sync collection data borrowIds:(" + borrowIds.toString() + ")failed,errorMsg:" + e.getMessage());
                    List<LsdResourceDo> sendMailMobiles = lsdResourceService.getResourceByType(ResourceType.SYNC_COLLECTION_FAILED_SEND_OPERATOR.getCode());
                    for (LsdResourceDo sendMailMobile : sendMailMobiles) {
                        smsUtil.sendZhPushException(sendMailMobile.getValue(), "执行第一天逾期入催任务失败！请及时查看。Time:" + time);
                    }
                } catch (Exception exp) {
                    log.error("执行第一天逾期入催任务失败！发送短信异常,errorMsg:" + e);
                }
            } finally {
                count.countDown();
            }

        }
    }

    /**
     * 构建 LsdBorrowCashOverdueDo
     *
     * @param borrowCashId
     * @param currentAmount
     * @param interest
     * @param realInterest
     * @param userId
     * @return
     */
    private LsdBorrowCashOverdueDo buildCashOverdue(Long borrowCashId, BigDecimal currentAmount, BigDecimal interest, BigDecimal realInterest, Long userId) {
        LsdBorrowCashOverdueDo overdue = new LsdBorrowCashOverdueDo();
        overdue.setBorrowCashId(borrowCashId);
        overdue.setCurrentAmount(currentAmount);
        overdue.setInterest(interest);
        overdue.setUserId(userId);
        overdue.setRealInterest(realInterest);
        return overdue;
    }

}
