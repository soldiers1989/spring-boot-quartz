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
@Table(name = "lsd_borrow_cash_overdue")
public class LsdBorrowCashOverdueDo implements Serializable {

    private static final long serialVersionUID = 5971186222223452569L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;
    //
    private Long isDelete;
    //
    private Date gmtCreate;
    // 用户id
    private Long userId;
    // 现金贷id
    private Long borrowCashId;
    // 利息
    private BigDecimal interest;
    //借款表实际产生利息
    private BigDecimal realInterest;
    // 当时应还款金额
    private BigDecimal currentAmount;

}