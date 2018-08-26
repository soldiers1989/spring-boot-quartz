package com.pgy.ginko.quartz.service.biz.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pgy.ginko.quartz.common.enums.TransOverdueBorrowCashType;
import com.pgy.ginko.quartz.common.http.HttpResult;
import com.pgy.ginko.quartz.model.biz.LsdBorrowCashOverdueDo;
import com.pgy.ginko.quartz.model.biz.LsdRenewalDetailDo;
import com.pgy.ginko.quartz.model.biz.dto.LsdBorrowCashDto;
import com.pgy.ginko.quartz.model.collection.Bo.CollectionSystemDataBo;
import com.pgy.ginko.quartz.model.collection.Bo.CollectionSystemReqRespBo;
import com.pgy.ginko.quartz.service.biz.LsdBorrowCashOverdueService;
import com.pgy.ginko.quartz.service.biz.LsdBorrowCashService;
import com.pgy.ginko.quartz.service.biz.LsdRenewalDetailService;
import com.pgy.ginko.quartz.utils.*;
import com.pgy.ginko.quartz.utils.biz.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author ginko
 * @description CollectionSystemUtil
 * @date 2018-8-23 21:17:54
 */
@Slf4j
@Component("collectionSystemUtil")
public class CollectionSystemUtil {

    @Resource
    private LsdRenewalDetailService lsdRenewalDetailService;

    @Resource
    private LsdBorrowCashOverdueService lsdBorrowCashOverdueService;

    @Resource
    private LsdBorrowCashService lsdBorrowCashService;

    @Resource
    private HttpApiService httpApiService;

    @Value("${pgy.collection.url}")
    private static String collectionUrl;

    /**
     * 美期同步 新增数据入催 新方法
     *
     * @param borrowIds 需要同步的 borrowId 组合
     * @return
     */
    public CollectionSystemReqRespBo syncAddDataToCollection(List<Long> borrowIds) throws ServiceException {
        Date currDate = new Date();
        String dataType = TransOverdueBorrowCashType.ADD.getCode();
        List<Map<String, String>> borrowCashDoList = new ArrayList<>();
        // 根据borrowIds 获取 需要同步到催收的数据 (因为这些Id 本就做过时间筛选 ,所以本次查询 只需要校验状态即可)
        List<LsdBorrowCashDto> batchRecords = lsdBorrowCashService.getBorrowListByBorrowIds(borrowIds);

        for (LsdBorrowCashDto borrowCash : batchRecords) {
            Map<String, String> reqBo = new HashMap<>();
            //用户ID
            reqBo.put("consumer_no", borrowCash.getUserId() + "");
            //借款No
            reqBo.put("borrow_no", borrowCash.getBorrowNo());
            //身份证姓名
            reqBo.put("card_name", borrowCash.getCardName());
            //身份证
            reqBo.put("card_number", borrowCash.getCardNumber());
            //打款时间
            reqBo.put("gmt_arrival", DateUtil.formatWithDateTimeShort(borrowCash.getGmtArrival()));
            //借款申请时间
            reqBo.put("gmt_create", DateUtil.formatWithDateTimeShort(borrowCash.getGmtCreate()));
            //借款金额
            reqBo.put("amount", borrowCash.getAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
            //借款天数
            reqBo.put("type", String.valueOf(borrowCash.getBorrowDays()));
            //央行基准利率对应利息
            reqBo.put("rate_amount", borrowCash.getRateAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
            //总逾期天数
            reqBo.put("overdue_day", DateUtil.daysBetween(borrowCash.getGmtPlanRepayment(), DateUtil.getNow()) + "");
            //借款单据总需要 还款金额
            reqBo.put("repay_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
            //剩余还款金额
            reqBo.put("rest_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).subtract(borrowCash.getRepayAmount()).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
            //当前逾期费
            reqBo.put("overdue_amount", borrowCash.getOverdueAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
            //已还款金额
            reqBo.put("repay_amount_sum", borrowCash.getRepayAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
            //计划还款日期
            reqBo.put("gmt_plan_repayment", DateUtil.formatWithDateTimeShort(borrowCash.getGmtPlanRepayment()));
            //续借次数
            reqBo.put("renewal_num", (borrowCash.getRenewalNum() == null ? 0 : borrowCash.getRenewalNum()) + "");
            //银行预留号码
            reqBo.put("bank_phone", borrowCash.getMobile());
            //累计已还滞纳金
            reqBo.put("sumOverdue", borrowCash.getSumOverdue().multiply(BigDecimalUtil.ONE_HUNDRED).toString());

            BigDecimal sumPoundage = BigDecimal.ZERO;
            LsdRenewalDetailDo lsdRenewalDetailDoQuery = new LsdRenewalDetailDo();
            lsdRenewalDetailDoQuery.setBorrowId(borrowCash.getRid());
            List<LsdRenewalDetailDo> listRenewalByBorrowId = lsdRenewalDetailService.select(lsdRenewalDetailDoQuery);
            for (LsdRenewalDetailDo lsdRenewalDetailDo : listRenewalByBorrowId) {
                sumPoundage = sumPoundage.add(lsdRenewalDetailDo.getCurrentPoundage());
            }
            reqBo.put("poundage_sum", sumPoundage.multiply(BigDecimalUtil.ONE_HUNDRED).toString());

            //定位地址及身份证地址处理
            String locationAddr = borrowCash.getAddress();
            if (StringUtils.isBlank(locationAddr)) {
                locationAddr = StringUtil.appendStrs(borrowCash.getProvince(), borrowCash.getCity(), borrowCash.getCounty());
            }
            try {
                reqBo.put("location_addr", URLEncoder.encode(locationAddr, "UTF-8"));
                reqBo.put("idcard_addr", URLEncoder.encode(borrowCash.getIdcardAddress(), "UTF-8"));
            } catch (UnsupportedEncodingException encodeEx) {
                log.error("新增数据入催location_addr、idcard_addr URLEncoder异常", encodeEx);
            }

            borrowCashDoList.add(reqBo);
        }

        HttpResult httpResult = new HttpResult();
        String json = JSONUtil.toJSONString(borrowCashDoList);
        CollectionSystemDataBo data = new CollectionSystemDataBo();
        String timestamp = DateUtil.formatWithDateTimeFullAll(currDate);
        data.setTimestamp(timestamp);
        data.setDataType(dataType);
        data.setSign(DigestUtil.MD5(json));
        data.setData(json);//数据集合

        try {
            httpResult = httpApiService.doPost(collectionUrl + "/api/getway/dataPool/dataReportNew", data);
            if (StringUtils.isBlank(httpResult.getBody())) {
                throw new ServiceException("introductionCollectionAddData fail , reqResult is null");
            } else {
                log.info("introductionCollectionAddData req success,reqResult" + httpResult.getBody());
            }
        } catch (Exception e) {
            log.error("introductionCollectionAddData post请求异常！ " + e);
        }

        CollectionSystemReqRespBo respInfo = JSONObject.parseObject(httpResult.getBody(), CollectionSystemReqRespBo.class);
        if (respInfo != null) {
            return respInfo;
        } else {
            throw new ServiceException("introductionCollectionAddData fail , respInfo info is null");
        }

    }

    /**
     * 美期同步 更新催收数据 新方法
     *
     * @param borrowIds
     * @return
     */
    public CollectionSystemReqRespBo syncUpdateDataToCollection(List<Long> borrowIds) throws ServiceException {
        Date currDate = new Date();
        String dataType = TransOverdueBorrowCashType.UPDATE.getCode();
        List<Map<String, String>> borrowCashDoList = new ArrayList<>();
        // 根据borrowIds 获取 需要同步到催收的数据 (因为这些Id 本就做过时间筛选 ,所以本次查询 只需要校验状态即可)
        List<LsdBorrowCashDto> batchRecords = lsdBorrowCashService.getBorrowUpdateListByBorrowIds(borrowIds);

        for (LsdBorrowCashDto borrowCash : batchRecords) {
            Map<String, String> reqBo = new HashMap<>();
            //借款号
            reqBo.put("borrow_no", borrowCash.getBorrowNo());
            //逾期费
            reqBo.put("overdue_amount", borrowCash.getOverdueAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
            //超期时间
            reqBo.put("overdue_day", DateUtil.daysBetween(borrowCash.getGmtPlanRepayment(), DateUtil.getNow()) + "");
            //单据  总还款金额
            //催收系统的 催收金额 =
            reqBo.put("repay_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
            //剩余未还金额 ,即催收金额
            reqBo.put("rest_amount", ((borrowCash.getAmount().add(borrowCash.getRateAmount().add(borrowCash.getOverdueAmount().add(borrowCash.getSumRate().add(borrowCash.getSumOverdue()))))).subtract(borrowCash.getRepayAmount()).setScale(2, RoundingMode.HALF_UP)).multiply(BigDecimalUtil.ONE_HUNDRED) + "");
            //总的还款金额
            reqBo.put("repay_amount_sum", borrowCash.getRepayAmount().multiply(BigDecimalUtil.ONE_HUNDRED) + "");

            reqBo.put("sumOverdue", borrowCash.getSumOverdue().multiply(BigDecimalUtil.ONE_HUNDRED).toString());

            LsdBorrowCashOverdueDo lsdBorrowCashOverdueDo = lsdBorrowCashOverdueService.queryOneByBorrowCashIdOrderByGmtCreateDesc(borrowCash.getRid());
            if (lsdBorrowCashOverdueDo != null) {
                reqBo.put("interest", lsdBorrowCashOverdueDo.getInterest().multiply(BigDecimalUtil.ONE_HUNDRED) + "");
            }

            borrowCashDoList.add(reqBo);
        }

        HttpResult httpResult = new HttpResult();
        String json = JSONArray.toJSONString(borrowCashDoList);
        CollectionSystemDataBo data = new CollectionSystemDataBo();
        String timestamp = DateUtil.formatWithDateTimeFullAll(currDate);
        data.setTimestamp(timestamp);
        data.setDataType(dataType);
        data.setSign(DigestUtil.MD5(json));
        data.setData(json);

        try {
            httpResult = httpApiService.doPost(collectionUrl + "/api/getway/dataPool/dataReportNew", data);
            if (StringUtils.isBlank(httpResult.getBody())) {
                throw new ServiceException("introductionCollectionUpdateData fail , reqResult is null");
            } else {
                log.info("introductionCollectionUpdateData req success,reqResult" + httpResult);
            }
        } catch (Exception e) {
            log.error("introductionCollectionUpdateData post请求异常！ " + e);
        }

        CollectionSystemReqRespBo respInfo = JSONObject.parseObject(httpResult.getBody(), CollectionSystemReqRespBo.class);
        if (respInfo != null) {
            return respInfo;
        } else {
            throw new ServiceException("introductionCollectionUpdateData fail , respInfo info is null");
        }
    }
}

