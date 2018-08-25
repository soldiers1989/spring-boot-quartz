//package com.pgy.ginko.quartz.service.collection;
//
//import com.alibaba.fastjson.JSONObject;
//import com.pgy.ginko.quartz.model.biz.LsdBorrowCashDo;
//import com.pgy.ginko.quartz.model.biz.LsdResourceDo;
//import com.pgy.ginko.quartz.service.biz.*;
//import com.pgy.ginko.quartz.service.biz.SmsUtil;
//import jdk.management.resource.ResourceType;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@Component("borrowCashOverdueSyncCollection")
//public class BorrowCashOverdueSyncCollection extends AbstractCronJob {
//
//    @Resource
//    private LsdBorrowCashService borrowCashService;
//
//    @Resource
//    private LsdResourceService afResourceService;
//
//    @Resource
//    private LsdBorrowCashOverdueService afBorrowCashOverdueService;
//
//    @Resource
//    private CollectionSystemUtil collectionSystemUtil;
//
//    @Resource
//    private LsdRepaymentBorrowCashService afRepaymentBorrowCashService;
//
//    @Resource
//    private LsdRenewalDetailService afRenewalDetailService;
//
//    @Resource
//    private LsdAssetService lsdAssetService;
//
//    @Resource
//    BizCacheUtil bizCacheUtil;
//
//    @Resource
//    SmsUtil smsUtil;
//
//    private ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
//
////    public ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
////            10,//核心线程数
////        20,//最大线程数
////          60L,//保持时间
////            TimeUnit.SECONDS,//时间单位
////            new LinkedBlockingQueue<>(102400),//阻塞队列
////            new ThreadFactoryBuilder().setNameFormat("borrowCashOverdueSyncCollection").build(),//线程工厂
////            new ThreadPoolExecutor.AbortPolicy());//异常捕获器;
//
//    @Override
//    public JobResult run() {
//        JobResult jobResult = new JobResult();
//        jobResult.setSuccess(true);
//        jobResult.setMsg("success");
//        logger.info("Start sync collection data,Starttime=" + new Date());
//
//        LsdResourceDo lsdResourceDo = afResourceService.getResourceByTypeAndSecType(ResourceType.COLLECTION_DATE_CONFIGURATION.getCode(), ResourceSecType.COLLECTION_DATE_CONFIGURATION.getCode());
//        Date currDate = new Date();
//
//        Date yesterdayDate = DateUtil.addDays(currDate, NumberUtil.strToIntWithDefault("-" + lsdResourceDo.getValue(), -1));
//        try {
//            final int pageSize = 5000;
//            Long totalRecord = borrowCashService.getBorrowCashOverdueMaxCount();
//
//            if (totalRecord == null || totalRecord == 0) {
//                logger.info("borrowCashOverdueJob run finished,cashOverdueList size is 0.time=" + new Date());
//                return jobResult;
//            }
//            final int totalPageNum = (int) ((totalRecord + pageSize - 1) / pageSize);
//            logger.info("borrowCashOverdueJob totalPageNum=" + totalPageNum);
//
//            // 1、统计本平台逾期信息
//            logger.info("borrowCashOverdueJob calcuOverdueRecords start,time=" + new Date());
//
//            // 保障线程执行完逾期利息计算后才给催收平台同步逾期信息;
//            CountDownLatch latch = new CountDownLatch(totalPageNum);
//
//            for (int i = 0; i < totalPageNum; i++) {// 分页查询操作
//                logger.info("borrowCashOverdueJob calcuOverdueRecords beginId=" + i * pageSize + ",endId=" + (i + 1) * pageSize);
//                List<LsdBorrowCashDo> cashOverdueList = borrowCashService.getBorrowCashOverdueByBorrowId(i * pageSize + 1, (i + 1) * pageSize);
//                threadPool.execute(new OverdueTask(cashOverdueList, latch));
//            }
//
//            latch.await();
//        } catch (Exception e) {
//            logger.error("Job:borrowCashOverdueSyncCollection 执行计算逾期费时异常!errorMsg:" + e.getMessage());
//            jobResult.setSuccess(false);
//            jobResult.setMsg("error");
//            return jobResult;
//        }
//
//        //启用线程池  每10条数据发送一次  线程池 最大容量为20  阻塞队列极大 执行完新增之后 再去执行  update 数据 避免 update 执行影响到 第一天逾期入催
//
//        // 第一天逾期入催 数据是新增
//        // 另外 对于同步状态 为 1 的 数据 是没有新增成功的 虽然日期 大于1天 但也需要新增,有另外的定时任务执行更新
//        try {
//            List<Long> borrowidList = borrowCashService.getBorrowOverdueFirstDayCount(yesterdayDate, NumberUtil.strToIntWithDefault(lsdResourceDo.getValue(), 1));
//
//            List<Long> borrowIdTmep = new ArrayList<>();
//            borrowIdTmep.addAll(borrowidList);
//            if (borrowidList != null && borrowidList.size() > 0) {
//                //每100 条执行一次
//                int count = borrowidList.size();
//                int pageSize = 100;
//                int pageSum = (count - 1) / 100 + 1;
//                CountDownLatch countDown = new CountDownLatch(pageSum);
//
//                for (int i = 0; i < pageSum; i++) {
//                    threadPool.execute(new SyncDataToCollection(borrowidList.subList(i * pageSize, (i + 1) * pageSize >= count ? count : (i + 1) * pageSize), TransOverdueBorrowCashType.ADD.getCode(), countDown));
//                }
//                countDown.await();
//            }
//        } catch (Exception e) {
//            Long time = System.currentTimeMillis();
//            logger.error(time + "job:borrowCashOverdueSyncCollection 第一天入催任务执行失败!errorMsg:" + e.getMessage());
//
//            List<LsdResourceDo> sendMailmobiles = afResourceService.getResourceByType(ResourceType.SYNC_COLLECTION_FAILED_SEND_OPERATOR.getCode());
//            for (LsdResourceDo sendMailmobile : sendMailmobiles) {
//                smsUtil.sendZhPushException(sendMailmobile.getValue(), "执行第一天逾期入催任务失败！请及时查看。Time:" + time);
//            }
//        }
//
//        try {
//            List<Long> borrowidList = borrowCashService.getBorrowOverdueAnotherDayCount(yesterdayDate);
//            if (borrowidList != null && borrowidList.size() > 0) {
//                //每100 条执行一次
//                int count = borrowidList.size();
//                int pageSize = 100;
//                int pageSum = (count - 1) / 100 + 1;
//                CountDownLatch countDown = new CountDownLatch(pageSum);
//
//                for (int i = 0; i < pageSum; i++) {
//                    threadPool.execute(new SyncDataToCollection(borrowidList.subList(i * pageSize, (i + 1) * pageSize >= count ? count : (i + 1) * pageSize), TransOverdueBorrowCashType.UPDATE.getCode(), countDown));
//                }
//
//                countDown.await();
//            }
//        } catch (Exception e) {
//            Long time = (new Date()).getTime();
//            logger.error(time + "job:borrowCashOverdueSyncCollection 执行逾期数据更新催收系统案件数据任务失败!errorMsg:" + e.getMessage());
//            List<LsdResourceDo> sendMailmobiles = afResourceService.getResourceByType(ResourceType.SYNC_COLLECTION_FAILED_SEND_OPERATOR.getCode());
//
//            for (LsdResourceDo sendMailmobile : sendMailmobiles) {
//                smsUtil.sendZhPushException(sendMailmobile.getValue(), "执行逾期数据更新催收系统案件数据任务失败！请及时查看。Time:" + time);
//            }
//
//        }
//
//        logger.info("End sync collection data,Endtime=" + new Date());
//
//        return jobResult;
//    }
//
//    class OverdueTask implements Runnable {
//
//        private List<LsdBorrowCashDo> borrowList;
//        CountDownLatch latch;
//
//        public OverdueTask(List<LsdBorrowCashDo> borrowList, CountDownLatch latch) {
//            this.borrowList = borrowList;
//            this.latch = latch;
//        }
//
//        @Override
//        public void run() {
//            try {
//                if (borrowList.size() > 0) {
//                    calcuOverdueRecords(borrowList);
//                }
//            } catch (Exception e) {
//                logger.error("逾期费计算异常!errorMsg:" + e);
//            } finally {
//                latch.countDown();
//            }
//        }
//
//    }
//
//
//    /**
//     * 计算处理本平台逾期借款数据,返回待传输数据
//     *
//     * @param cashOverdueList
//     */
//    public void calcuOverdueRecords(List<LsdBorrowCashDo> cashOverdueList) {
//        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        // 已逾期账单
//        if (cashOverdueList != null) {
//            logger.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calcuOverdueRecords size is " + cashOverdueList.size());
//        } else {
//            logger.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calcuOverdueRecords cashOverdueList is null ");
//        }
//
//        LsdResourceDo overdueConfig = afResourceService.getResourceByTypeAndSecType(ResourceType.OVERDUE_CONFIG.getCode(), ResourceSecType.OVERDUE_CONFIG.getCode());
//        for (int i = 0; i < cashOverdueList.size(); i++) {
//            LsdBorrowCashDo cashDo = cashOverdueList.get(i);
//
//            //重新检查借款订单的最新值，避免覆盖期间还款等信息
//            try {
//                cashDo = borrowCashService.getBorrowCashById(cashDo.getRid());
//
//                if (null == cashDo.getGmtArrival()) {
//                    logger.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calcuOverdueRecords " + cashDo.getBorrowNo() + " 该用户没有到账。。。");
//                    continue;
//                }
//                LsdRepaymentBorrowCashDo afRepaymentBorrowCashDo = afRepaymentBorrowCashService.getProcessingRepaymentByBorrowId(cashDo.getRid());
//                // 当前逾期费用 = 借款金额 + 累计已还逾期费 + 累计已还利息 - 还款金额
//                BigDecimal currentAmount = BigDecimalUtil.add(cashDo.getAmount(), cashDo.getSumRate(), cashDo.getSumOverdue()).subtract(cashDo.getRepayAmount());// 当前本金
//                if (afRepaymentBorrowCashDo != null) {// 过滤掉还款中的金额
//                    logger.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calcuOverdueRecords " + cashDo.getBorrowNo() + " 该用户正在还款中。。。");
//                    currentAmount = BigDecimalUtil.add(cashDo.getAmount(), cashDo.getOverdueAmount(), cashDo.getRateAmount(), cashDo.getSumRate(), cashDo.getSumOverdue()).subtract(cashDo.getRepayAmount()).subtract(afRepaymentBorrowCashDo.getRepaymentAmount());// 当前本金
//                }
//                if (currentAmount.compareTo(BigDecimal.ZERO) == 0) {
//                    logger.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calcuOverdueRecords " + cashDo.getBorrowNo() + " 该用户已经扣除逾期费用。。。");
//                    continue;
//                }
//
//                // 过滤掉续借中的数据
//                LsdRenewalDetailDo afRenewalDetailDo = afRenewalDetailService.getProcessingRenewalByBorrowId(cashDo.getRid());
//                if (afRenewalDetailDo != null) {
//                    logger.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calcuOverdueRecords " + cashDo.getBorrowNo() + " 该用户正在续借。。。");
//                    continue;
//                }
//
//                String lockKey = BizConstants.CACHEKEY_APPLY_RENEWAL_LOCK + cashDo.getUserId();
//                boolean getLock = bizCacheUtil.getLock30Second(lockKey, "1");
//                try {
//                    if (getLock) {
//
//                        BigDecimal oldOverdueAmount = cashDo.getOverdueAmount();// 当前逾期
//                        // 当前逾期费用 = 借款金额 + 累计已还逾期费 + 累计已还利息 - 还款金额
//                        // 逾期费 = 当前逾期金额 *（ 央行基准费率 （默认为0 ） * 借钱最高倍数（ 默认 为 4 ） + 借钱手续费率（日） + 借钱逾期手续费率（日） ）
//                        LsdResourceDo lsdResourceDo = afResourceService.getResourceByTypeAndSecType(BizConstants.NEW_BORROW_OVERDUE_POUNDAGE_CONFIG, BizConstants.NEW_BORROW_OVERDUE_POUNDAGE_VALUE);
//                        BigDecimal poundage = new BigDecimal(0.02);
//                        if (lsdResourceDo != null) {
//                            poundage = new BigDecimal(lsdResourceDo.getValue());
//                        }
//                        BigDecimal newOverdueAmount = currentAmount.multiply(poundage).setScale(6, BigDecimal.ROUND_HALF_UP);
//                        BigDecimal realInterest = newOverdueAmount; //增加逾期费增长限制后用户每天实际增加的应还逾期费
//                        if (newOverdueAmount.compareTo(BigDecimal.ZERO) > 0) {
//                            if (NumberUtil.strToInt(overdueConfig.getValue()) == 1) {
//                                // 如果当前逾期超出本金的配置比例
//                                if (cashDo.getAmount().multiply(new BigDecimal(overdueConfig.getValue1())).compareTo(cashDo.getOverdueAmount().add(newOverdueAmount).add(cashDo.getSumOverdue())) < 0) {
//                                    BigDecimal oldOverdue = cashDo.getOverdueAmount();
//                                    cashDo.setOverdueAmount((cashDo.getAmount().multiply(new BigDecimal(overdueConfig.getValue1())).subtract(cashDo.getSumOverdue())).compareTo(BigDecimal.ZERO) < 0
//                                            ? BigDecimal.ZERO : cashDo.getAmount().multiply(new BigDecimal(overdueConfig.getValue1())).subtract(cashDo.getSumOverdue()));
//                                    cashDo.setRealOverdue(cashDo.getRealOverdue().add(newOverdueAmount));
//                                    realInterest = (cashDo.getAmount().multiply(new BigDecimal(overdueConfig.getValue1())).subtract(oldOverdue).subtract(cashDo.getSumOverdue()).compareTo(BigDecimal.ZERO) < 0
//                                            ? BigDecimal.ZERO : cashDo.getAmount().multiply(new BigDecimal(overdueConfig.getValue1())).subtract(oldOverdue).subtract(cashDo.getSumOverdue()));
//                                } else {
//                                    cashDo.setOverdueAmount(oldOverdueAmount.add(newOverdueAmount));
//                                    cashDo.setRealOverdue(cashDo.getRealOverdue().add(newOverdueAmount));
//                                }
//                            } else {
//                                cashDo.setOverdueAmount(oldOverdueAmount.add(newOverdueAmount));
//                            }
//
//                        }
//                        cashDo.setOverdueDay(cashDo.getOverdueDay() + 1);
//                        if (cashDo.getOverdueDay() == BizConstants.OVERDUE_DAYS) {
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("consumerNo", cashDo.getUserId() + "");
//                            params.put("category", 1 + "");
//                            params.put("remark", "逾期三十天用户");
//                            params.put("source", "fx");
//                            String reqResult = HttpUtil.post(getUrl() + "/modules/api/user/action/addUserBlack.htm", params);
//                            logger.info("borrowCashOverdueSyncCollection addUserBlack result:", JSONObject.parseObject(reqResult, RiskRespBo.class));
//                        }
//                        cashDo.setOverdueStatus(YesNoStatus.YES.getCode());
//                        cashDo.setCurrentOverdueStatus(YesNoStatus.YES.getCode());
//                        borrowCashService.updateBorrowCash(cashDo);
//                        // 新增逾期记录
//                        afBorrowCashOverdueService.addBorrowCashOverdue(buildCashOverdue(cashDo.getRid(), currentAmount, newOverdueAmount, realInterest, cashDo.getUserId()));
//                        logger.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calcuOverdueRecords " + cashDo.getBorrowNo() + "  添加逾期记录。。。");
//                        // 更新资产表
//                        LsdAssetDo lsdAssetDo = lsdAssetService.findAssetByBorrowIdTypeNoFinished(cashDo.getRid(), cashDo.getBorrowType());
//                        if (lsdAssetDo != null) {
//                            logger.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calcuOverdueRecords " + cashDo.getBorrowNo() + " start update lsd_asset table ,lsd_asset id is " + lsdAssetDo.getId());
//                            lsdAssetDo.setOverdueStatus(OverdueStatus.OVERDUE.getCode());
//                            lsdAssetDo.setOverdueDay(BigDecimalUtil.removeNull(lsdAssetDo.getOverdueDay()) + 1);
//                            lsdAssetDo.setOverdueAmount(cashDo.getOverdueAmount());
//                            lsdAssetDo.setRealOverdue(lsdAssetDo.getRealOverdue().add(newOverdueAmount));
//
//                            if (DateUtil.compare(new Date(), lsdAssetDo.getGmtPlanAssetrepay())) {
//                                lsdAssetDo.setOverdueAssetStatus(OverdueStatus.OVERDUE.getCode());
//                                lsdAssetDo.setOverdueAssetAmount(lsdAssetDo.getOverdueAssetAmount().add(newOverdueAmount));
//                                lsdAssetDo.setOverdueAssetDay(lsdAssetDo.getOverdueAssetDay() + 1);
//                            }
//                            logger.info("build time is " + myFmt2.format(new Date()) + " borrowCashOverdueSyncCollection calcuOverdueRecords " + cashDo.getBorrowNo() + " update lsdasset id " + lsdAssetDo.getId() + " lsdasset is " + lsdAssetDo.getBorrowNo());
//                            lsdAssetService.update(lsdAssetDo);
//                        }
//                    } else {
//                        continue;
//                    }
//                } finally {
//                    if (getLock) {
//                        bizCacheUtil.delCache(lockKey);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                logger.error("borrowCashOverdueSyncCollection calcuOverdueRecords error borrowCashId=" + cashDo.getRid() + ",error=", e);
//            }
//        }
//    }
//
//    private static String getUrl() {
//        // 临时解决方案 riskUrl中的http->https
//        String riskUrl = ConfigProperties.get("lsdadmin.risk.url", "");
//        String type = ConfigProperties.get("fbadmin.inveloment.type", "");
//
//        return riskUrl;
//    }
//
//    private LsdBorrowCashOverdueDo buildCashOverdue(Long borrowCashId, BigDecimal currentAmount, BigDecimal interest, BigDecimal realInterest, Long userId) {
//        LsdBorrowCashOverdueDo overdue = new LsdBorrowCashOverdueDo();
//        overdue.setBorrowCashId(borrowCashId);
//        overdue.setCurrentAmount(currentAmount);
//        overdue.setInterest(interest);
//        overdue.setUserId(userId);
//        overdue.setRealInterest(realInterest);
//        return overdue;
//    }
//
//
//    class SyncDataToCollection implements Runnable {
//        List<Long> borrowIds;
//        String dataType;
//        CountDownLatch count;
//
//        public SyncDataToCollection(List<Long> borrowIds, String dataType, CountDownLatch count) {
//            this.borrowIds = borrowIds;
//            this.dataType = dataType;
//            this.count = count;
//        }
//
//        @Override
//        public void run() {
//
//            //线程运行时的异常必须捕捉  因为异常可能会造成countDown 无法结束
//            try {
//                if (borrowIds != null && borrowIds.size() > 0) {
//                    //将数据PUSH 到催收系统  逾期超过一天的数据进行修改 逾期一天的数据 新增
//                    if (TransOverdueBorrowCashType.ADD.getCode().equals(dataType)) {
//                        CollectionSystemReqRespBo respBo = collectionSystemUtil.syncAddDataToCollection(borrowIds);
//                        logger.info("borrowCashOverdueSyncCollection sync collection add data borrowIds:(" + borrowIds.toString() + ") result:" + respBo.getMsg());
//
//                    } else {
//                        CollectionSystemReqRespBo respBo = collectionSystemUtil.syncUpdateDataToCollection(borrowIds);
//                        logger.info("borrowCashOverdueSyncCollection sync collection update data borrowIds:(" + borrowIds.toString() + ") result:" + respBo.getMsg());
//                    }
//                }
//
//            } catch (Exception e) {
//                try {
//                    Long time = System.currentTimeMillis();
//                    logger.error(time + "borrowCashOverdueSyncCollection sync collection data borrowIds:(" + borrowIds.toString() + ")failed,errorMsg:" + e.getMessage());
//                    List<LsdResourceDo> sendMailmobiles = afResourceService.getResourceByType(ResourceType.SYNC_COLLECTION_FAILED_SEND_OPERATOR.getCode());
//                    for (LsdResourceDo sendMailmobile : sendMailmobiles) {
//                        smsUtil.sendZhPushException(sendMailmobile.getValue(), "执行第一天逾期入催任务失败！请及时查看。Time:" + time);
//                    }
//                } catch (Exception exp) {
//                    logger.error("执行第一天逾期入催任务失败！发送短信异常,errorMsg:" + e);
//                }
//            } finally {
//                count.countDown();
//            }
//
//        }
//    }
//
//
//
//
//}
