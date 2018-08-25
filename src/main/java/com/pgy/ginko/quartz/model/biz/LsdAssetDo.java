
package com.pgy.ginko.quartz.model.biz;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author ginko
 */
@Data
@ToString
@ApiModel
@Table(name = "lsd_asset")
public class LsdAssetDo implements Serializable {

    private static final long serialVersionUID = -1900096135724487823L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //是否删除
    private Long isDelete;
    //创建时间
    private Date gmtCreate;
    //修改时间
    private Date gmtModified;
    //最终还款时间(与还款表相对应)
    private Date gmtRepay;
    //借钱id
    private Long borrowId;
    //借款编号/续借编号
    private String borrowNo;
    //用户id
    private Long userId;
    //借款天数【7,14】
    private Integer borrowDays;
    //申请金额
    private BigDecimal amount;
    //借款状态【0:已经打款/待还款,1:已结清】
    private Integer status;
    //到账金额
    private BigDecimal arrivalAmount;
    //预计还款时间
    private Date gmtPlanRepayment;
    //手续费
    private BigDecimal poundage;
    //央行基准利率对应利息
    private BigDecimal rateAmount;
    //已还款金额
    private BigDecimal repayAmount;
    //当前逾期状态：0：未逾期; 1：已逾期
    private Integer overdueStatus;
    //逾期天数
    private Long overdueDay;
    //当前逾期费
    private BigDecimal overdueAmount;
    // 备注
    private String remark;
    //资产类型，0表示借款，1表示续借
    private Integer assetType;
    //资金渠道id
    private String assetChannelId;
    //上标时间
    private Date gmtTender;
    //对资金方预计还款时间
    private Date gmtPlanAssetrepay;
    //对资金方逾期费
    private BigDecimal overdueAssetAmount;
    //资金方结算周期
    private Integer assetDays;
    //资金方逾期状态：0：未逾期; 1：已逾期
    private Integer overdueAssetStatus;
    //资金方逾期天数
    private Long overdueAssetDay;
    //累计使用优惠券金额
    private BigDecimal sumCoupon;
    //累计已还滞纳金
    private BigDecimal sumOverdue;
    //累计已还资金方滞纳金
    private BigDecimal sumAssetOverdue;
    //累计使用账户余额
    private BigDecimal sumRebate;
    //减免逾期费
    private BigDecimal creditOverdue;
    //是否资金已划款【0:未划款,2:划款中,1:还款成功,-1:划款失败】
    private Integer transferStatus;
    //ups流水号
    private String upsNo;
    //划款成功时间
    private Date gmtTransfer;
    //卡号或者支付宝账号
    private String cardNumber;
    //卡名称/微信/支付宝
    private String cardName;
    //上标状态：0：未上标; 1：已上标
    private Integer tenderStatus;
    //划款ups号
    private String transferUpsNo;
    //借款用途【1消费;2旅游;3教育学习;4装修;5医疗健康;6其他】
    private Integer borrowUse;
    //借款类型【1现金借2消费贷3白领贷】
    private Integer borrowType;
    //消费金额
    private BigDecimal consumeAmount;
    //续借划账状态，-1表示划账失败，0新建，1表示成功，2表示正在进行中
    private Integer renewalTransferStatus;
    //续借划账ups编号
    private String renewalTransferUpsNo;
    //续借划账时间
    private Date gmtRenewalTransfer;
    //消费贷划账状态
    private Integer consumeTransferStatus;
    //消费贷商品划账ups编号
    private String consumeTransferUpsNo;
    //消费贷商品划账时间
    private Date gmtConsumeTransfer;
    //手续费poundage划款编号
    private String poundageUpsNo;
    //已还款利息
    private BigDecimal sumRate;
    //当前实际逾期费
    private BigDecimal realOverdue;

}