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
// * syncAddDataToCollection
// * syncUpdateDataToCollection
// *
// *
// * @date 2018-8-23 21:17:54
// */
//@Component("collectionSystemUtil")
//public class CollectionSystemUtil extends AbstractThird {
//
//    @Resource
//    private LsdCommitRecordService lsdCommitRecordService;
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
