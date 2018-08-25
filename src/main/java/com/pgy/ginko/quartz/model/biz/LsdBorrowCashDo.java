package com.pgy.ginko.quartz.model.biz;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 借款表
 */
@Data
@ToString
@ApiModel
@Table(name = "lsd_borrow_cash")
public class LsdBorrowCashDo implements Serializable {

    private static final long serialVersionUID = -8148051162590945828L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;
    
    private Long isDelete;
    
    private Date gmtCreate;
    
    private Date gmtModified;
    
    private Date gmtRepay;
    
    private String borrowNo;

    private Long userId;

    protected Integer borrowDays;

    protected BigDecimal amount;

    protected Integer status;

    protected Integer reviewStatus;

    private String rishOrderNo;

    protected BigDecimal arrivalAmount;

    private BigDecimal activityAmount;

    private Date gmtArrival;

    private String upsNo;

    private Date gmtPlanRepayment;

    private BigDecimal poundage;

    private BigDecimal rateAmount;

    private BigDecimal repayAmount;

    private String cardNumber;

    private String cardName;

    private Date gmtClose;

    private String closeReason;

    private Integer overdueStatus;

    private Integer currentOverdueStatus;

    private Long overdueDay;

    private BigDecimal overdueAmount;

    private BigDecimal sumRate;

    private BigDecimal sumOverdue;

    private BigDecimal sumRebate;

    private BigDecimal sumRenewalPoundage;

    private Long renewalNum;

    private String remark;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String province;

    private String city;

    private String county;

    private String address;

    private Integer successNum;

    private BigDecimal poundageRate;

    private BigDecimal baseBankRate;

    private Integer borrowUse;

    private Double consumeAmount;

    private Double overdueRate;

    private BigDecimal creditOverdue;

    private BigDecimal realOverdue;

    private Integer borrowType;

    private Long userCouponId;

    private BigDecimal couponAmount;

}