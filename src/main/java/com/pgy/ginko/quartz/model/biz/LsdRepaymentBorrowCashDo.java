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
@Table(name = "lsd_repayment_borrow_cash")
public class LsdRepaymentBorrowCashDo implements Serializable {

    private static final long serialVersionUID = -425228604843853306L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;
    // 是否删除状态，1：删除，0：有效
    private Integer isDelete;
    // 创建时间
    private Date gmtCreate;
    // 最后修改时间
    private Date gmtModified;
    // 还款类型[1主动还款0线下还款]
    private Integer repayType;
    // 还款编号
    private String repayNo;
    // 用户id
    private Long userId;
    // 还款状态【’0’-新建状态,'1'-还款成功,'2':处理中, -1：”还款失败”】
    private Integer status;
    // 还款金额
    private BigDecimal repaymentAmount;
    // 实际支付金额
    private BigDecimal actualAmount;
    // 借钱id
    private Long borrowId;
    // 平台提供给三方支付的交易流水号
    private String payTradeNo;
    // 第三方的交易流水号
    private String tradeNo;
    // 优惠券id
    private Long userCouponId;
    // 优惠券金额
    private BigDecimal couponAmount;
    // 返现金额
    private BigDecimal rebateAmount;
    // 卡号
    private String cardNumber;
    // 卡名称、支付宝、微信
    private String cardName;
    private String remark;

}