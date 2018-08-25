package com.pgy.ginko.quartz.model.biz;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author acer
 */
@Data
@ToString
@ApiModel
@Table(name = "lsd_renewal_detail")
public class LsdRenewalDetailDo implements Serializable {

    private static final long serialVersionUID = -4214942825066727028L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;
    private Long isDelete;
    // 申请时间
    private Date gmtCreate;
    private Date gmtModified;
    // 借款ID
    private Long borrowId;
    // 状态【0:新建状态, 1:续期成功，2:处理中 , -1:续期失败】
    private Integer status;
    // 备注
    private String remark;
    // 原预计还款时间
    private Date gmtPlanRepayment;
    // 续期本金
    private BigDecimal renewalAmount;
    // 上期利息
    private BigDecimal priorInterest;
    // 上期滞纳金
    private BigDecimal priorOverdue;
    // 本次续期所还本金
    private BigDecimal capital;
    // 本期手续费
    private BigDecimal currentPoundage;
    // 所用账户余额
    private BigDecimal rebateAmount;
    // 实付金额
    private BigDecimal actualAmount;
    // 用户id
    private Long userId;
    // 支付方式（卡名称）、支付宝、微信
    private String cardName;
    // 卡号
    private String cardNumber;
    // 平台提供给三方支付的交易流水号
    private String payTradeNo;
    // 第三方的交易流水号
    private String tradeNo;
    // 续期天数【7】
    private Integer renewalDay;
    // 借钱手续费率（日）
    private BigDecimal poundageRate;
    // 央行基准利率
    private BigDecimal baseBankRate;
    // 失败原因
    private String failReason;
}