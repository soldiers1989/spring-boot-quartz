//package com.pgy.ginko.quartz.service.biz.utils;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//
//import com.pgy.ginko.quartz.common.constant.BizConstants;
//import com.pgy.ginko.quartz.common.constant.CommonConstant;
//import com.pgy.ginko.quartz.common.enums.YesNoStatus;
//import com.pgy.ginko.quartz.model.biz.LsdBorrowCashOverdueDo;
//import com.pgy.ginko.quartz.model.biz.LsdCommitRecordDo;
//import com.pgy.ginko.quartz.model.biz.LsdRenewalDetailDo;
//import com.pgy.ginko.quartz.service.biz.LsdBorrowCashOverdueService;
//import com.pgy.ginko.quartz.service.biz.LsdBorrowCashService;
//import com.pgy.ginko.quartz.service.biz.LsdCommitRecordService;
//import com.pgy.ginko.quartz.service.biz.LsdRenewalDetailService;
//import com.pgy.ginko.quartz.utils.DateUtil;
//import com.pgy.ginko.quartz.utils.biz.DigestUtil;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.net.URLEncoder;
//import java.util.*;
//
///**
// * @author ginko
// * @description CollectionSystemUtil
// * @date 2018-8-23 21:17:54
// */
//@Component("collectionSystemUtil")
//public class CollectionSystemUtil extends AbstractThird {
//
//    @Resource
//    private LsdCommitRecordService lsdCommitRecordService;
//
//    @Resource
//    private BizCacheUtil bizCacheUtil;
//
//    @Resource
//    private LsdRenewalDetailService lsdRenewalDetailService;
//
//    @Resource
//    private LsdBorrowCashOverdueService lsdBorrowCashOverdueService;
//
//    @Resource
//    private LsdBorrowCashService borrowCashService;
//
//    private static String url = null;
//
//    /**
//     * 美期通过调用此接口向催收平台传入逾期借款数据
//     *
//     * @param batchRecords
//     * @return
//     */
//    public CollectionSystemReqRespBo introductionCollectionData(List<LsdBorrowCashDto> batchRecords, String dateType) {
//        //传入催收系统的数据
//        if (TransOverdueBorrowCashType.ADD.getCode().equals(dateType)) {
//            return introductionCollectionAddData(batchRecords);
//        } else if (TransOverdueBorrowCashType.UPDATE.getCode().equals(dateType)) {
//            return introductionCollectionUpdateData(batchRecords);
//        } else {
//            log.error("introductionCollectionData error,dateType is invalid");
//            throw new AdminException("introductionCollectionData error,dateType is invalid");
//        }
//    }
//
//
//    /**
//     * 美期通过调用此接口向催收平台传入逾期借款数据
//     *
//     * @param batchRecords
//     * @return
//     */
//    public CollectionSystemReqRespBo introductionCollectionDataV1(List<LsdStageBorrowDto> batchRecords, String dateType) {
//        if (TransOverdueBorrowCashType.ADD.getCode().equals(dateType)) {
//            return introductionCollectionAddDataV1(batchRecords);
//        } else if (TransOverdueBorrowCashType.UPDATE.getCode().equals(dateType)) {
//            return introductionCollectionUpdateDataV1(batchRecords);
//        } else {
//            log.error("introductionCollectionData error,dateType is invalid");
//            throw new AdminException("introductionCollectionData error,dateType is invalid");
//        }
//    }
//
//
//    /**
//     * 美期主动还款通知催收平台
//     *
//     * @param repayNo        --还款编号
//     * @param borrowNo       --借款单号
//     * @param cardNumber     --卡号
//     * @param cardName       --银行卡名称（支付方式）
//     * @param amount         --还款金额
//     * @param restAmount     --剩余未还金额
//     * @param repayAmount    --理论应还款金额
//     * @param overdueAmount  --逾期手续费
//     * @param repayAmountSum --已还总额
//     * @param rateAmount     --利息
//     * @param pingAccount
//     * @param reliefAmount
//     * @param status
//     * @return
//     */
//    public CollectionSystemReqRespBo consumerRepayment(String repayNo, String borrowNo, String cardNumber, String cardName,
//                                                       String repayTime, String tradeNo, BigDecimal amount, BigDecimal restAmount,
//                                                       BigDecimal repayAmount, BigDecimal overdueAmount, BigDecimal repayAmountSum,
//                                                       BigDecimal rateAmount, String repayType, BigDecimal repayCapitalAmount,
//                                                       BigDecimal restCapitalAmount, Boolean pingAccount, BigDecimal reliefAmount,
//                                                       String status, BigDecimal sumOverdue) {
//        CollectionDataBo data = new CollectionDataBo();
//        Map<String, String> reqBo = new HashMap<>();
//        reqBo.put("repay_no", repayNo);
//        reqBo.put("borrow_no", borrowNo);
//        reqBo.put("card_number", cardNumber);
//        reqBo.put("card_name", cardName);
//        reqBo.put("repay_time", repayTime);
//        reqBo.put("trade_no", tradeNo);
//        reqBo.put("amount", amount.multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//        reqBo.put("rest_amount", restAmount.multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//        reqBo.put("repay_amount", repayAmount.multiply(BigDecimalUtil.ONE_HUNDRED) + "");                   //需要修改
//        reqBo.put("overdue_amount", overdueAmount.multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//        reqBo.put("repay_amount_sum", repayAmountSum.multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//        reqBo.put("rate_amount", rateAmount.multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//        reqBo.put("repay_type", repayType);
//
//        reqBo.put("repay_capital_amount", repayCapitalAmount.multiply(BigDecimalUtil.ONE_HUNDRED) + "");    //还款本金
//        reqBo.put("rest_capital_amount", restCapitalAmount.multiply(BigDecimalUtil.ONE_HUNDRED) + "");      //待还本金
//
//        //        还款标记 1线下还款 2线下还款平账 3App还款
//        reqBo.put("repay_tag", pingAccount == Boolean.TRUE ? "2" : "1");
//
//        //        实际减免金额
//        reqBo.put("reduce_amount", reliefAmount.multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//        reqBo.put("status", status);
//        reqBo.put("sumOverdue", sumOverdue.multiply(BigDecimalUtil.ONE_HUNDRED).toString());
//
//        String json = JsonUtil.toJSONString(reqBo);
//        data.setData(json);// 数据集合
//        data.setSign(DigestUtil.MD5(json));
//        String timestamp = DateUtil.getDateTimeFullAll(new Date());
//        data.setTimestamp(timestamp);
//
//        String postUrl = getUrl() + "/api/getway/repayment/repaymentAchieve";
//        try {
//            String reqResult = HttpUtil.post(postUrl, data);
//            log.info("admin consumerRepayment req success, data is " + JSON.toJSONString(data) + "reqResult " + reqResult);
//            if (StringUtils.isBlank(reqResult)) {
//                lsdCommitRecordService.addRecord(CommitRecordType.REPAYMENT_COLLECTION.getCode(), borrowNo, json, postUrl, YesNoStatus.NO);
//                throw new AdminException("consumerRepayment fail , reqResult is null");
//            }
//            CollectionSystemReqRespBo respInfo = JSONObject.parseObject(reqResult, CollectionSystemReqRespBo.class);
//            if (respInfo != null) {
//                if (StringUtils.equals("200", respInfo.getCode())) {
//                    lsdCommitRecordService.addRecord(CommitRecordType.REPAYMENT_COLLECTION.getCode(), borrowNo, json, postUrl, YesNoStatus.YES);
//                    return respInfo;
//                } else {
//                    lsdCommitRecordService.addRecord(CommitRecordType.REPAYMENT_COLLECTION.getCode(), borrowNo, json, postUrl, YesNoStatus.NO);
//                    throw new AdminException("renewalNotify fail , respInfo info is " + JSONObject.toJSONString(respInfo));
//                }
//            }
//        } catch (Exception e) {
//            LsdCommitRecordDo lsdCommitRecordDo = lsdCommitRecordService.addRecord(CommitRecordType.REPAYMENT_COLLECTION.getCode(), borrowNo, json, postUrl, YesNoStatus.NO);
//            log.info("request collection is exception ,save lsd_commit_record is:" + JSON.toJSONString(lsdCommitRecordDo));
//            throw new AdminException("consumerRepayment fail Exception is " + e + ",consumerRepayment send again");
//        }
//        throw new AdminException("renewalNotify fail");
//    }
//
//    /**
//     * 美期通过调用此接口向催收平台传入逾期借款数据--对所有数据做新增处理
//     *
//     * @param batchRecords
//     * @return
//     */
//    private CollectionSystemReqRespBo introductionCollectionAddData(List<LsdBorrowCashDto> batchRecords) {
//        Date currDate = new Date();
//        String dataType = TransOverdueBorrowCashType.ADD.getCode();
//        List<Map<String, String>> borrowCashDoList = new ArrayList<>();
//
//        for (LsdBorrowCashDto borrowCash : batchRecords) {
//            Map<String, String> reqBo = new HashMap<String, String>();
//            //用户ID
//            reqBo.put("consumer_no", borrowCash.getUserId() + "");
//            //借款No
//            reqBo.put("borrow_no", borrowCash.getBorrowNo());
//            //身份证姓名
//            reqBo.put("card_name", borrowCash.getCardName());
//            //身份证
//            reqBo.put("card_number", borrowCash.getCardNumber());
//            //打款时间
//            reqBo.put("gmt_arrival", DateUtil.formatWithDateTimeShort(borrowCash.getGmtArrival()));
//            //借款申请时间
//            reqBo.put("gmt_create", DateUtil.formatWithDateTimeShort(borrowCash.getGmtCreate()));
//
//            //借款金额
//            reqBo.put("amount", borrowCash.getAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //借款天数
//            reqBo.put("type", String.valueOf(borrowCash.getBorrowDays()));
//            //央行基准利率对应利息
//            reqBo.put("rate_amount", borrowCash.getRateAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //总逾期天数
//            reqBo.put("overdue_day", DateUtil.daysBetween(borrowCash.getGmtPlanRepayment(), DateUtil.getNow()) + "");
//            //借款单据总需要 还款金额
//            reqBo.put("repay_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //剩余还款金额
//            reqBo.put("rest_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).subtract(borrowCash.getRepayAmount()).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //当前逾期费
//            reqBo.put("overdue_amount", borrowCash.getOverdueAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //已还款金额
//            reqBo.put("repay_amount_sum", borrowCash.getRepayAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//
//            reqBo.put("gmt_plan_repayment", DateUtil.formatWithDateTimeShort(borrowCash.getGmtPlanRepayment()));
//            reqBo.put("renewal_num", (borrowCash.getRenewalNum() == null ? 0 : borrowCash.getRenewalNum()) + "");
//            reqBo.put("bank_phone", borrowCash.getMobile());
//
//            reqBo.put("consigneeName", borrowCash.getConsigneeName());
//            reqBo.put("consigneeAddress", borrowCash.getConsigneeAddress());
//            reqBo.put("consigneeMobile", borrowCash.getConsigneeMobile());
//            reqBo.put("sumOverdue", borrowCash.getSumOverdue().multiply(BigDecimalUtil.ONE_HUNDRED).toString());
//
//            BigDecimal sumPoundage = BigDecimal.ZERO;
//            List<LsdRenewalDetailDo> listRenewalByBorrowId = lsdRenewalDetailService.getListRenewalByBorrowId(borrowCash.getRid());
//            for (LsdRenewalDetailDo lsdRenewalDetailDo : listRenewalByBorrowId) {
//                sumPoundage = sumPoundage.add(lsdRenewalDetailDo.getCurrentPoundage());
//            }
//            reqBo.put("poundage_sum", sumPoundage.multiply(BigDecimalUtil.ONE_HUNDRED).toString());
//
//            //定位地址及身份证地址处理
//            String locationAddr = borrowCash.getAddress();
//            if (StringUtils.isBlank(locationAddr)) {
//                locationAddr = StringUtils.appendStrs(borrowCash.getProvince(), borrowCash.getCity(), borrowCash.getCounty());
//            }
//
//            reqBo.put("location_addr", StringUtils.UrlEncoder(locationAddr));
//            reqBo.put("idcard_addr", StringUtils.UrlEncoder(borrowCash.getIdcardAddress()));
//            borrowCashDoList.add(reqBo);
//        }
//
//        String json = JsonUtil.toJSONString(borrowCashDoList);
//        CollectionSystemDataBo data = new CollectionSystemDataBo();
//        String timestamp = DateUtil.formatWithDateTimeFullAll(currDate);
//        data.setTimestamp(timestamp);
//        data.setDataType(dataType);
//        data.setSign(DigestUtil.MD5(json));
//        data.setData(json);//数据集合
//        log.info("---------------------url:-" + getUrl());
//        String reqResult = HttpUtil.post(getUrl() + "/api/getway/dataPool/dataReport", data);
//        if (StringUtils.isBlank(reqResult)) {
//            throw new AdminException("introductionCollectionAddData fail , reqResult is null");
//        } else {
//            log.info("introductionCollectionAddData req success,reqResult" + reqResult);
//        }
//        CollectionSystemReqRespBo respInfo = JSONObject.parseObject(reqResult, CollectionSystemReqRespBo.class);
//        if (respInfo != null) {
//            return respInfo;
//        } else {
//            throw new AdminException("introductionCollectionAddData fail , respInfo info is null");
//        }
//    }
//
//    /**
//     * 获取代扣忽略的订单编号
//     *
//     * @return
//     */
//    @SuppressWarnings("unchecked")
//    public List<String> getIgnoreBorrowNos() {
//        Map<String, String> data = new HashMap<>();
//        try {
//            String reqResult = HttpUtil.post(getUrl() + "/api/getway/repayment/getNoAuditRepaymentList", data);
//            if (StringUtils.isBlank(reqResult)) {
//                return (List<String>) bizCacheUtil.getObject(BizConstants.CACHEKEY_AUTO_REPAYMENT_IGNORE_NO);
//            }
//            CollectionSystemIgnoreNo resp = JSON.parseObject(reqResult, CollectionSystemIgnoreNo.class);
//            if ("200".equals(resp.getCode())) {
//                return resp.getData();
//            }
//            return (List<String>) bizCacheUtil.getObject(BizConstants.CACHEKEY_AUTO_REPAYMENT_IGNORE_NO);
//        } catch (Exception e) {
//            log.error("getIgnoreBorrowNos error,e:{}", e);
//            return (List<String>) bizCacheUtil.getObject(BizConstants.CACHEKEY_AUTO_REPAYMENT_IGNORE_NO);
//        }
//    }
//
//    /**
//     * 美期通过调用此接口向催收平台传入逾期借款数据--对所有数据做新增处理
//     *
//     * @param batchRecords
//     * @return
//     */
//    private CollectionSystemReqRespBo introductionCollectionAddDataV1(List<LsdStageBorrowDto> batchRecords) {
//        Date currDate = new Date();
//        String dataType = TransOverdueBorrowCashType.ADD.getCode();
//        List<Map<String, String>> borrowCashDoList = new ArrayList<Map<String, String>>();
//        for (LsdStageBorrowDto borrowCash : batchRecords) {
//            Map<String, String> reqBo = new HashMap<String, String>();
//            reqBo.put("consumer_no", borrowCash.getUserId() + "");
//            reqBo.put("borrow_no", borrowCash.getBorrowNo());
//            reqBo.put("card_name", borrowCash.getCardName());
//            reqBo.put("card_number", borrowCash.getCardNumber());
//            reqBo.put("gmt_arrival", DateUtil.formatWithDateTimeShort(borrowCash.getGmtArrival()));
//            reqBo.put("amount", borrowCash.getAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            reqBo.put("type", String.valueOf(borrowCash.getBorrowDays()));
//            reqBo.put("rate_amount", borrowCash.getRateAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            reqBo.put("overdue_day", DateUtil.daysBetween(borrowCash.getGmtPlanRepayment(), DateUtil.getNow()) + "");
//            reqBo.put("repay_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            reqBo.put("rest_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).subtract(borrowCash.getRepayAmount()).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            reqBo.put("overdue_amount", borrowCash.getOverdueAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            reqBo.put("repay_amount_sum", borrowCash.getRepayAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            reqBo.put("gmt_plan_repayment", DateUtil.formatWithDateTimeShort(borrowCash.getGmtPlanRepayment()));
//            reqBo.put("renewal_num", "1");
//            reqBo.put("bank_phone", borrowCash.getMobile());
//
//            //定位地址及身份证地址处理
//            String locationAddr = borrowCash.getAddress();
//            if (StringUtils.isBlank(locationAddr)) {
//                locationAddr = StringUtils.appendStrs(borrowCash.getProvince(), borrowCash.getCity(), borrowCash.getCounty());
//            }
//            reqBo.put("location_addr", org.apache.catalina.util.URLEncoder.DEFAULT.encode(locationAddr));
//            reqBo.put("idcard_addr", URLEncoder.UrlEncoder(borrowCash.getIdcardAddress()));
//            borrowCashDoList.add(reqBo);
//        }
//        String json = JsonUtil.toJSONString(borrowCashDoList);
//        CollectionSystemDataBo data = new CollectionSystemDataBo();
//        String timestamp = DateUtil.formatWithDateTimeFullAll(currDate);
//        data.setTimestamp(timestamp);
//        data.setDataType(dataType);
//        data.setSign(DigestUtil.MD5(json));
//        data.setData(json);//数据集合
//        String reqResult = HttpUtil.post(getUrl() + "/api/getway/dataPool/dataReport", data);
//        if (StringUtils.isBlank(reqResult)) {
//            throw new AdminException("introductionCollectionAddData fail , reqResult is null");
//        } else {
//            log.info("introductionCollectionAddData req success,reqResult" + reqResult);
//        }
//        CollectionSystemReqRespBo respInfo = JSONObject.parseObject(reqResult, CollectionSystemReqRespBo.class);
//        if (respInfo != null) {
//            return respInfo;
//        } else {
//            throw new AdminException("introductionCollectionAddData fail , respInfo info is null");
//        }
//    }
//
//
//    /**
//     * 美期通过调用此接口向催收平台传入逾期借款数据--对所有数据做更新处理
//     *
//     * @param batchRecords
//     * @return
//     */
//    private CollectionSystemReqRespBo introductionCollectionUpdateData(List<LsdBorrowCashDto> batchRecords) {
//        Date currDate = new Date();
//        String dataType = TransOverdueBorrowCashType.UPDATE.getCode();
//        List<Map<String, String>> borrowCashDoList = new ArrayList<Map<String, String>>();
//
//        for (LsdBorrowCashDto borrowCash : batchRecords) {
//            Map<String, String> reqBo = new HashMap<String, String>();
//            //借款号
//            reqBo.put("borrow_no", borrowCash.getBorrowNo());
//            //逾期费
//            reqBo.put("overdue_amount", borrowCash.getOverdueAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //超期时间
//            reqBo.put("overdue_day", DateUtil.daysBetween(borrowCash.getGmtPlanRepayment(), DateUtil.getNow()) + "");
//            //单据  总还款金额
//            //催收系统的 催收金额 =
//            reqBo.put("repay_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //剩余未还金额 ,即催收金额
//            reqBo.put("rest_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).subtract(borrowCash.getRepayAmount()).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //总的还款金额
//            reqBo.put("repay_amount_sum", borrowCash.getRepayAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//
//            reqBo.put("consigneeName", borrowCash.getConsigneeName());
//            reqBo.put("consigneeAddress", borrowCash.getConsigneeAddress());
//            reqBo.put("consigneeMobile", borrowCash.getConsigneeMobile());
//            reqBo.put("sumOverdue", borrowCash.getSumOverdue().multiply(BigDecimalUtil.ONE_HUNDRED).toString());
//
//            LsdBorrowCashOverdueDo lsdBorrowCashOverdueDo = lsdBorrowCashOverdueService.queryOneByBorrowCashIdOrderByGmtCreateDesc(borrowCash.getRid());
//            if (lsdBorrowCashOverdueDo != null) {
//                reqBo.put("interest", lsdBorrowCashOverdueDo.getInterest().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            }
//
//            borrowCashDoList.add(reqBo);
//        }
//
//        String json = JsonUtil.toJSONString(borrowCashDoList);
//        CollectionSystemDataBo data = new CollectionSystemDataBo();
//        String timestamp = DateUtil.formatWithDateTimeFullAll(currDate);
//        data.setTimestamp(timestamp);
//        data.setDataType(dataType);
//        data.setSign(DigestUtil.MD5(json));
//        data.setData(json);//数据集合
//        String reqResult = HttpUtil.post(getUrl() + "/api/getway/dataPool/dataReport", data);
//        if (StringUtils.isBlank(reqResult)) {
//            throw new AdminException("introductionCollectionUpdateData fail , reqResult is null");
//        } else {
//            log.info("introductionCollectionUpdateData req success,reqResult" + reqResult);
//        }
//        CollectionSystemReqRespBo respInfo = JSONObject.parseObject(reqResult, CollectionSystemReqRespBo.class);
//        if (respInfo != null) {
//            return respInfo;
//        } else {
//            throw new AdminException("introductionCollectionUpdateData fail , respInfo info is null");
//        }
//    }
//
//
//    /**
//     * 美期通过调用此接口向催收平台传入逾期借款数据--对所有数据做更新处理
//     *
//     * @param batchRecords
//     * @return
//     */
//    private CollectionSystemReqRespBo introductionCollectionUpdateDataV1(List<LsdStageBorrowDto> batchRecords) {
//        Date currDate = new Date();
//        String dataType = TransOverdueBorrowCashType.UPDATE.getCode();
//        List<Map<String, String>> borrowCashDoList = new ArrayList<Map<String, String>>();
//
//        for (LsdStageBorrowDto lsdStageBorrowDto : batchRecords) {
//            Map<String, String> reqBo = new HashMap<String, String>();
//            reqBo.put("borrow_no", lsdStageBorrowDto.getBorrowNo());
//            reqBo.put("overdue_amount", lsdStageBorrowDto.getOverdueAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            reqBo.put("overdue_day", DateUtil.daysBetween(lsdStageBorrowDto.getGmtPlanRepayment(), DateUtil.getNow()) + "");
//            reqBo.put("repay_amount", ((lsdStageBorrowDto.getAmount().add(lsdStageBorrowDto.getRateAmount().add(lsdStageBorrowDto.getOverdueAmount().add(lsdStageBorrowDto.getSumRate().add(lsdStageBorrowDto.getSumOverdue()))))).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            reqBo.put("rest_amount", ((lsdStageBorrowDto.getAmount().add(lsdStageBorrowDto.getRateAmount().add(lsdStageBorrowDto.getOverdueAmount().add(lsdStageBorrowDto.getSumRate().add(lsdStageBorrowDto.getSumOverdue()))))).subtract(lsdStageBorrowDto.getRepayAmount()).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            reqBo.put("repay_amount_sum", lsdStageBorrowDto.getRepayAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            reqBo.put("borrow_type", BorrowTypeEnum.STAGE_BORROW.getName());
//            borrowCashDoList.add(reqBo);
//        }
//
//        String json = JsonUtil.toJSONString(borrowCashDoList);
//        CollectionSystemDataBo data = new CollectionSystemDataBo();
//        String timestamp = DateUtil.formatWithDateTimeFullAll(currDate);
//        data.setTimestamp(timestamp);
//        data.setDataType(dataType);
//        data.setSign(DigestUtil.MD5(json));
//        data.setData(json);//数据集合
//        String reqResult = HttpUtil.post(getUrl() + "/api/getway/dataPool/dataReport", data);
//        if (StringUtils.isBlank(reqResult)) {
//            throw new AdminException("introductionCollectionUpdateData fail , reqResult is null");
//        } else {
//            log.info("introductionCollectionUpdateData req success,reqResult" + reqResult);
//        }
//        CollectionSystemReqRespBo respInfo = JSONObject.parseObject(reqResult, CollectionSystemReqRespBo.class);
//        if (respInfo != null) {
//            return respInfo;
//        } else {
//            throw new AdminException("introductionCollectionUpdateData fail , respInfo info is null");
//        }
//    }
//
//    /**
//     * 平账通知
//     */
//    public CollectionSystemReqRespBo balanceResultNotify(String borrowNo, Integer code, String msg) {
//        CollectionDataBo data = new CollectionDataBo();
//        Map<String, String> reqBo = new HashMap<>();
//        reqBo.put("borrow_no", borrowNo);
//        reqBo.put("code", String.valueOf(code));
//        reqBo.put("msg", msg);
//
//        String json = JsonUtil.toJSONString(reqBo);
//        data.setData(json);// 数据集合
//        data.setSign(DigestUtil.MD5(json));
//        String timestamp = DateUtil.getDateTimeFullAll(new Date());
//        data.setTimestamp(timestamp);
//        String reqResult = HttpUtil.post(getUrl() + "/api/getway/repayment/getHumanReviewResult", data);
//        if (StringUtils.isBlank(reqResult)) {
//            throw new AdminException("balanceResultNotify fail , reqResult is null");
//        } else {
//            log.info("balanceResultNotify req success,reqResult" + reqResult);
//        }
//        CollectionSystemReqRespBo respInfo = JSONObject.parseObject(reqResult, CollectionSystemReqRespBo.class);
//        if (respInfo != null && StringUtils.equals("200", respInfo.getCode())) {
//            return respInfo;
//        } else {
//            throw new AdminException("balanceResultNotify fail , respInfo info is " + JSONObject.toJSONString(respInfo));
//        }
//    }
//
//    /**
//     * 推送 交易流水 审核失败的消息到催收
//     *
//     * @param tradeNo  流水号
//     * @param borrowNo 借款No
//     * @return
//     */
//    public boolean sendCheckRepayOrderToCollection(String tradeNo, String borrowNo) {
//        CollectionDataBo data = new CollectionDataBo();
//        Map<String, Object> param = new HashMap<>();
//        param.put("tradeNo", tradeNo);
//        param.put("borrowNo", borrowNo);
//        String json = JsonUtil.toJSONString(param);
//        data.setData(json);
//        String timestamp = DateUtil.getDateTimeFullAll(new Date());
//        data.setTimestamp(timestamp);
//        String postUrl = getUrl() + Constants.POST_COLLECTION_REPAYMENT_CHECK_FALIED;
//        data.setSign(DigestUtil.MD5(json));
//        Boolean isFlag = Boolean.FALSE;
//        try {
//            String reqResult = HttpUtil.post(postUrl, data);
//
//            log.info("admin consumerRepayment req success, data is " + JSON.toJSONString(data) + "reqResult " + reqResult);
//            if (!StringUtils.isEmpty(reqResult)) {
//                CollectionSystemReqRespBo respInfo = JSONObject.parseObject(reqResult, CollectionSystemReqRespBo.class);
//                if (respInfo != null && "200".equals(respInfo.getCode())) {
//                    isFlag = Boolean.TRUE;
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("request fail:" + postUrl);
//        }
//        if (!isFlag) {
//            LsdCommitRecordDo lsdCommitRecordDo = lsdCommitRecordService.addRecord(
//                    CommitRecordType.REPAYMENT_CHECK_FAILED.getCode(), borrowNo, json, postUrl, YesNoStatus.NO);
//            log.info("request collection is exception-repayment-Check(线下还款 财务审核不通过) ,save lsd_commit_record is:" + JSON.toJSONString(lsdCommitRecordDo));
//        }
//
//        return isFlag;
//    }
//
//    /**
//     * 美期同步 新增数据入催 新方法
//     *
//     * @param borrowIds 需要同步的 borrowId 组合
//     * @return
//     */
//    public CollectionSystemReqRespBo syncAddDataToCollection(List<Long> borrowIds) {
//        Date currDate = new Date();
//        String dataType = TransOverdueBorrowCashType.ADD.getCode();
//        List<Map<String, String>> borrowCashDoList = new ArrayList<>();
//        // 根据borrowIds 获取 需要同步到催收的数据 (因为这些Id 本就做过时间筛选 ,所以本次查询 只需要校验状态即可)
//        List<LsdBorrowCashDto> batchRecords = borrowCashService.getBorrowListByBorrowIds(borrowIds);
//
//        for (LsdBorrowCashDto borrowCash : batchRecords) {
//            Map<String, String> reqBo = new HashMap<String, String>();
//            //用户ID
//            reqBo.put("consumer_no", borrowCash.getUserId() + "");
//            //借款No
//            reqBo.put("borrow_no", borrowCash.getBorrowNo());
//            //身份证姓名
//            reqBo.put("card_name", borrowCash.getCardName());
//            //身份证
//            reqBo.put("card_number", borrowCash.getCardNumber());
//            //打款时间
//            reqBo.put("gmt_arrival", DateUtil.formatWithDateTimeShort(borrowCash.getGmtArrival()));
//            //借款申请时间
//            reqBo.put("gmt_create", DateUtil.formatWithDateTimeShort(borrowCash.getGmtCreate()));
//
//            //借款金额
//            reqBo.put("amount", borrowCash.getAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //借款天数
//            reqBo.put("type", String.valueOf(borrowCash.getBorrowDays()));
//            //央行基准利率对应利息
//            reqBo.put("rate_amount", borrowCash.getRateAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //总逾期天数
//            reqBo.put("overdue_day", DateUtil.daysBetween(borrowCash.getGmtPlanRepayment(), DateUtil.getNow()) + "");
//            //借款单据总需要 还款金额
//            reqBo.put("repay_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //剩余还款金额
//            reqBo.put("rest_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).subtract(borrowCash.getRepayAmount()).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //当前逾期费
//            reqBo.put("overdue_amount", borrowCash.getOverdueAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //已还款金额
//            reqBo.put("repay_amount_sum", borrowCash.getRepayAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//
//            reqBo.put("gmt_plan_repayment", DateUtil.formatWithDateTimeShort(borrowCash.getGmtPlanRepayment()));
//            reqBo.put("renewal_num", (borrowCash.getRenewalNum() == null ? 0 : borrowCash.getRenewalNum()) + "");
//            reqBo.put("bank_phone", borrowCash.getMobile());
//
//            reqBo.put("sumOverdue", borrowCash.getSumOverdue().multiply(BigDecimalUtil.ONE_HUNDRED).toString());
//
//            BigDecimal sumPoundage = BigDecimal.ZERO;
//            List<LsdRenewalDetailDo> listRenewalByBorrowId = lsdRenewalDetailService.getListRenewalByBorrowId(borrowCash.getRid());
//            for (LsdRenewalDetailDo lsdRenewalDetailDo : listRenewalByBorrowId) {
//                sumPoundage = sumPoundage.add(lsdRenewalDetailDo.getCurrentPoundage());
//            }
//            reqBo.put("poundage_sum", sumPoundage.multiply(BigDecimalUtil.ONE_HUNDRED).toString());
//
//            //定位地址及身份证地址处理
//            String locationAddr = borrowCash.getAddress();
//            if (StringUtils.isBlank(locationAddr)) {
//                locationAddr = StringUtils.appendStrs(borrowCash.getProvince(), borrowCash.getCity(), borrowCash.getCounty());
//            }
//
//            reqBo.put("location_addr", StringUtils.UrlEncoder(locationAddr));
//            reqBo.put("idcard_addr", StringUtils.UrlEncoder(borrowCash.getIdcardAddress()));
//            borrowCashDoList.add(reqBo);
//        }
//
//        String json = JsonUtil.toJSONString(borrowCashDoList);
//        CollectionSystemDataBo data = new CollectionSystemDataBo();
//        String timestamp = DateUtil.formatWithDateTimeFullAll(currDate);
//        data.setTimestamp(timestamp);
//        data.setDataType(dataType);
//        data.setSign(DigestUtil.MD5(json));
//        data.setData(json);//数据集合
//
//        log.info("---------------------url:-" + getUrl());
//        String reqResult = HttpUtil.post(getUrl() + "/api/getway/dataPool/dataReportNew", data);
//        if (StringUtils.isBlank(reqResult)) {
//            throw new AdminException("introductionCollectionAddData fail , reqResult is null");
//        } else {
//            log.info("introductionCollectionAddData req success,reqResult" + reqResult);
//        }
//        CollectionSystemReqRespBo respInfo = JSONObject.parseObject(reqResult, CollectionSystemReqRespBo.class);
//        if (respInfo != null) {
//            return respInfo;
//        } else {
//            throw new AdminException("introductionCollectionAddData fail , respInfo info is null");
//        }
//    }
//
//    /**
//     * 美期同步 更新催收数据 新方法
//     *
//     * @param borrowIds
//     * @return
//     */
//    public CollectionSystemReqRespBo syncUpdateDataToCollection(List<Long> borrowIds) {
//        Date currDate = new Date();
//        String dataType = TransOverdueBorrowCashType.UPDATE.getCode();
//        List<Map<String, String>> borrowCashDoList = new ArrayList<Map<String, String>>();
//        // 根据borrowIds 获取 需要同步到催收的数据 (因为这些Id 本就做过时间筛选 ,所以本次查询 只需要校验状态即可)
//        List<LsdBorrowCashDto> batchRecords = borrowCashService.getBorrowUpdateListByBorrowIds(borrowIds);
//
//        for (LsdBorrowCashDto borrowCash : batchRecords) {
//            Map<String, String> reqBo = new HashMap<String, String>();
//            //借款号
//            reqBo.put("borrow_no", borrowCash.getBorrowNo());
//            //逾期费
//            reqBo.put("overdue_amount", borrowCash.getOverdueAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //超期时间
//            reqBo.put("overdue_day", DateUtil.daysBetween(borrowCash.getGmtPlanRepayment(), DateUtil.getNow()) + "");
//            //单据  总还款金额
//            //催收系统的 催收金额 =
//            reqBo.put("repay_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //剩余未还金额 ,即催收金额
//            reqBo.put("rest_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).subtract(borrowCash.getRepayAmount()).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            //总的还款金额
//            reqBo.put("repay_amount_sum", borrowCash.getRepayAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//
//            reqBo.put("sumOverdue", borrowCash.getSumOverdue().multiply(BigDecimalUtil.ONE_HUNDRED).toString());
//
//            LsdBorrowCashOverdueDo lsdBorrowCashOverdueDo = lsdBorrowCashOverdueService.queryOneByBorrowCashIdOrderByGmtCreateDesc(borrowCash.getRid());
//            if (lsdBorrowCashOverdueDo != null) {
//                reqBo.put("interest", lsdBorrowCashOverdueDo.getInterest().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
//            }
//
//            borrowCashDoList.add(reqBo);
//        }
//
//        String json = JSONArray.toJSONString(borrowCashDoList);
//        CollectionSystemDataBo data = new CollectionSystemDataBo();
//        String timestamp = DateUtil.formatWithDateTimeFullAll(currDate);
//        data.setTimestamp(timestamp);
//        data.setDataType(dataType);
//        data.setSign(DigestUtil.MD5(json));
//        data.setData(json);//数据集合
//        String reqResult = HttpUtil.post(getUrl() + "/api/getway/dataPool/dataReportNew", data);
//        if (StringUtils.isBlank(reqResult)) {
//            throw new AdminException("introductionCollectionUpdateData fail , reqResult is null");
//        } else {
//            log.info("introductionCollectionUpdateData req success,reqResult" + reqResult);
//        }
//        CollectionSystemReqRespBo respInfo = JSONObject.parseObject(reqResult, CollectionSystemReqRespBo.class);
//        if (respInfo != null) {
//            return respInfo;
//        } else {
//            throw new AdminException("introductionCollectionUpdateData fail , respInfo info is null");
//        }
//    }
//
//}
//
